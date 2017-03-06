package controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.controlsfx.control.Notifications;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.paint.Color;
import model.FTPTransaction;
import model.FTPClient;
import model.FTPClientAdditional;
import view.FTPRootPane;


public class FTPController 
{

  private FTPRootPane view;
  @SuppressWarnings("unused")
  private FTPTransaction model;
  private FTPClient fc;
  private static final int NTHREADS = 10;
  private static final Executor exec = Executors.newFixedThreadPool(NTHREADS);
  private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
  private String host;
  private String port;

  public FTPController(FTPRootPane view, FTPTransaction model) 
  {
    this.view = view;
    this.model = model;
    this.attachEventHandlers();
    this.refreshLocalView();
  }


  public void attachEventHandlers() 
  {
    this.view.addConnectAction(e -> this.connectEvent());
    this.view.getLocalFSP().addUploadAction(e -> this.uploadEvent());
    this.view.getLocalFSP().addRefreshAction(e -> this.refreshEvent());
    this.view.getRemoteFSP().addDownloadAction(e -> this.downloadEvent());
    this.view.getRemoteFSP().addRefreshRemoteAction(e -> this.refreshRemoteEvent());
  }

 
  public void connectEvent() 
  {

    host = this.view.getHost();
    port = this.view.getPort();
    String password = this.view.getPassword();
    
    this.fc = new FTPClient(host, Integer.parseInt(port));

    if (this.fc.getConnectionStatus()) 
    {
      this.view.getSP().makeCommandStatusUpdate("Attempting to connect to server ...");
      this.view.getSP().makeGeneralStatusUpdate("Connection established with server.");
      this.view.getSP().makeGeneralStatusUpdate("Autheticating client with server ....");

      try 
      {
        boolean res = this.fc.isPasswordValid(password);
        if (res) 
        {
          // we connected so refresh remote
          this.view.getSP().makeSuccessStatusUpdate("Successfully authenticted with server.");
          this.refreshRemoteEvent();
        } 
        else 
        {
          this.view.getSP().makeErrorStatusUpdate("Authentication failed.");
        }
      } 
      catch (IOException e) 
      {
        e.printStackTrace();
      }
    } 
    else 
    {
      this.view.getSP().makeErrorStatusUpdate("Failed to establish a connection.");
    }
  }

  public void refreshEvent() 
  {
    this.refreshLocalViewStatus();
  }

  public void uploadEvent() 
  {

    ObservableList<TreeItem<String>> file =
        this.view.getLocalFSP().getTreeLocal().getSelectionModel().getSelectedItems();

    
    Runnable task = new Runnable() 
    {
      public void run() 
      {
        try 
        {
          upload(file.get(0).getValue().toString());
        } 
        catch (Exception e) 
        {
          e.printStackTrace();
        }
      }
    };
    exec.execute(task);


    for (int i = 1; i < file.size(); i++) 
    {

      String fileSend = file.get(i).getValue().toString();

      exec.execute(new Runnable() 
      {
        public void run() 
        {
          try 
          {
            additionalUpload(fileSend);
          } 
          catch (Exception e) 
          {

            e.printStackTrace();
          }
        }
      });
    }
  }

  public void downloadEvent() 
  {
    

    ObservableList<TreeItem<String>> file =
        this.view.getRemoteFSP().getTreeRemote().getSelectionModel().getSelectedItems();

    Runnable task = new Runnable() 
    {
      public void run() 
      {
        try 
        {
          download(file.get(0).getValue().toString());
        }
        catch (Exception e) 
        {
          e.printStackTrace();
        }
      }
    };
    exec.execute(task);


    for (int i = 1; i < file.size(); i++)
    {
      String fileSend = file.get(i).getValue().toString();

      exec.execute(new Runnable() 
      {
        public void run() 
        {
          try 
          {
            additionalDownload(fileSend);
          } 
          catch (Exception e) 
          {
            e.printStackTrace();
          }
        }
      });
    }
  }

  public void refreshRemoteEvent() 
  {
    Runnable task = new Runnable() 
    {
      public void run() 
      {
        try 
        {
          remote();
        } 
        catch (Exception e) 
        {
          e.printStackTrace();
        }
      }
    };
    exec.execute(task);
  }

  public void download(String file) throws Exception 
  {
    // add transaction to pane
    Platform.runLater(new Runnable() 
    {
      public void run() 
      {
        view.getSP().makeCommandStatusUpdate("Attempting to download " + file + " from server...");
      }
    });


    Date d = new Date();
    SimpleDateFormat ft = new SimpleDateFormat("E dd/MM hh:mm:ss ");

    // get individual size of file
    long bytes = this.fc.fileSize(file);

    FTPTransaction tran = new FTPTransaction("C:\\Users\\Jonny\\Desktop\\LocalFiles\\" + file,
        "<<--", "\\RemoteFiles\\" + file, bytes, "In progress ...", ft.format(d));
    this.fc.addTransactions(tran);

    ObservableList<FTPTransaction> transactionsList =
        FXCollections.observableArrayList(this.fc.getTransactions());
    this.view.setItemsTableView(transactionsList);

    this.fc.receiveFile(file);

    // update status here??
    tran.setStatus("Complete");
    transactionsList = FXCollections.observableArrayList(this.fc.getTransactions());

    view.setItemsTableView(transactionsList);

    Platform.runLater(new Runnable() 
    {
      public void run() 
      {
        view.refreshTableView();
      }
    });

    Platform.runLater(new Runnable() 
    {
      public void run() 
      {
        view.getSP().makeGeneralStatusUpdate(file + " was successfully downloaded from server.");
        createDownloadNotification(file);
      }
    });
  }


  /** STAYING IT IS **/
  public void additionalDownload(String file) throws Exception {
    Platform.runLater(new Runnable() {
      public void run() {
        view.getSP().makeCommandStatusUpdate("Attempting to download " + file + " from server...");
      }
    });

    FTPClientAdditional fc = new FTPClientAdditional(host, Integer.parseInt(port), file);

    Date d = new Date();
    SimpleDateFormat ft = new SimpleDateFormat("E dd/MM hh:mm:ss");

    // get individual size of file
    long bytes = fc.fileSize(file);

    FTPTransaction tran = new FTPTransaction("C:\\Users\\Jonny\\Desktop\\LocalFiles\\" + file,
        "<<--", "\\RemoteFiles\\" + file, bytes, "In progress ...", ft.format(d));
    this.fc.addTransactions(tran);

    ObservableList<FTPTransaction> transactionsList =
        FXCollections.observableArrayList(this.fc.getTransactions());
    this.view.setItemsTableView(transactionsList);

    fc.receiveFile(file);

    tran.setStatus("Complete");
    transactionsList = FXCollections.observableArrayList(this.fc.getTransactions());

    view.setItemsTableView(transactionsList);

    Platform.runLater(new Runnable() {
      public void run() {
        view.refreshTableView();
      }
    });

    fc.closeDownAdditional();

    Platform.runLater(new Runnable() {
      public void run() {
        view.getSP().makeGeneralStatusUpdate(file + " was successfully downloaded from server.");
        createDownloadNotification(file);
      }
    });
  }



  public void upload(String file) throws Exception {
    Platform.runLater(new Runnable() {
      public void run() {
        view.getSP().makeCommandStatusUpdate("Attempting to upload " + file + " to server...");
      }
    });

    File f = new File("C:\\Users\\Jonny\\Desktop\\LocalFiles\\" + file);
    long bytes = f.length();

    Date d = new Date();
    SimpleDateFormat ft = new SimpleDateFormat("E dd/MM hh:mm:ss ");
    FTPTransaction tran = new FTPTransaction("C:\\Users\\Jonny\\Desktop\\LocalFiles\\" + file,
        " -->>", "\\RemoteFiles\\" + file, bytes, "In progress ...", ft.format(d));

    this.fc.addTransactions(tran);

    ObservableList<FTPTransaction> transactionsList =
        FXCollections.observableArrayList(this.fc.getTransactions());
    this.view.setItemsTableView(transactionsList);


    this.fc.sendFile(file);

    tran.setStatus("Complete");
    transactionsList = FXCollections.observableArrayList(this.fc.getTransactions());

    view.setItemsTableView(transactionsList);

    Platform.runLater(new Runnable() {
      public void run() {
        view.refreshTableView();
      }
    });

    Platform.runLater(new Runnable() {
      public void run() {
        view.getSP().makeGeneralStatusUpdate(file + " was uploaded downloaded to server.");
        createUploadNotification(file);
      }
    });
  }

  /** STAYING IT IS NOT **/
  public void additionalUpload(String file) throws Exception {
    Platform.runLater(new Runnable() {
      public void run() {
        view.getSP().makeCommandStatusUpdate("Attempting to upload " + file + " to server...");
      }
    });

    FTPClientAdditional fc = new FTPClientAdditional(host, Integer.parseInt(port), file);

    Date d = new Date();
    SimpleDateFormat ft = new SimpleDateFormat("E dd/MM hh:mm:ss ");
    // get individual size of file
    File f = new File("C:\\Users\\Jonny\\Desktop\\LocalFiles\\" + file);
    long bytes = f.length();

    FTPTransaction tran = new FTPTransaction("C:\\Users\\Jonny\\Desktop\\LocalFiles\\" + file,
        " -->>", "\\RemoteFiles\\" + file, bytes, "In progress ...", ft.format(d));
    this.fc.addTransactions(tran);

    ObservableList<FTPTransaction> transactionsList =
        FXCollections.observableArrayList(this.fc.getTransactions());
    this.view.setItemsTableView(transactionsList);

    fc.sendFile(file);

    tran.setStatus("Complete");
    transactionsList = FXCollections.observableArrayList(this.fc.getTransactions());

    view.setItemsTableView(transactionsList);

    Platform.runLater(new Runnable() {
      public void run() {
        view.refreshTableView();
      }
    });

    fc.closeDownAdditional();

    Platform.runLater(new Runnable() {
      public void run() {
        view.getSP().makeGeneralStatusUpdate(file + " was uploaded downloaded to server.");
        createUploadNotification(file);
      }
    });

  }

  public void createDownloadNotification(String file) {
    Notifications.create().graphic(null).title("Transfer Complete")
        .text("The file " + file + " has been successfully downloaded from the server.").show();
  }

  public void createUploadNotification(String file) {
    Notifications.create().graphic(null).title("Transfer Complete")
        .text("The file " + file + " has been successfully uploaded to the server.").show();
  }


  public void remote() throws Exception {
    ArrayList<String> files = this.fc.remoteFile();

    Platform.runLater(new Runnable() {
      public void run() {
        refreshRemoteView(files);
      }
    });
  }

  public void refreshRemoteView(ArrayList<String> files) {
    this.view.getSP().makeCommandStatusUpdate("Refreshing Remote File System ...");
    this.view.getSP().makeGeneralStatusUpdate("Remote File System up to date.");

    TreeItem<String> treeRoot = new TreeItem<String>(files.get(0), new Label("", fontAwesome
        .create(FontAwesome.Glyph.FOLDER_OPEN).color(Color.GOLDENROD)));
    treeRoot.setExpanded(true);

    this.view.getRemoteFSP().setStatusLabel(1, files.size() - 1, this.fc.getByteSizeRemoteFiles());


    for (int i = 1; i < files.size(); i++) {
      String ext = files.get(i);
      String[] e = ext.split("\\.");

      switch (e[1]) {
        case "mp3":
          treeRoot.getChildren()
              .add(new TreeItem<String>(files.get(i),
                  new Label("", fontAwesome.create(FontAwesome.Glyph.FILE_AUDIO_ALT)
                      .color(Color.BLUEVIOLET))));
          break;
        case "pdf":
          treeRoot.getChildren()
              .add(new TreeItem<String>(files.get(i),
                  new Label("", fontAwesome.create(FontAwesome.Glyph.FILE_PDF_ALT)
                      .color(Color.DARKORANGE))));
          break;
        case "java":
        case "sql":
        case "c":
          treeRoot.getChildren()
              .add(new TreeItem<String>(files.get(i),
                  new Label("", fontAwesome.create(FontAwesome.Glyph.FILE_CODE_ALT)
                      .color(Color.DARKCYAN))));
          break;
        case "jpg":
        case "png":
          treeRoot.getChildren().add(new TreeItem<String>(files.get(i), new Label("", fontAwesome
              .create(FontAwesome.Glyph.FILE_PHOTO_ALT).color(Color.PURPLE))));
          break;
        case "txt":
          treeRoot.getChildren().add(new TreeItem<String>(files.get(i), new Label("", fontAwesome
              .create(FontAwesome.Glyph.FILE_TEXT_ALT).color(Color.DARKBLUE))));
          break;
        case "doc":
          treeRoot.getChildren().add(new TreeItem<String>(files.get(i), new Label("", fontAwesome
              .create(FontAwesome.Glyph.FILE_WORD_ALT).color(Color.BLUE))));
          break;
        case "mp4":
          treeRoot.getChildren()
              .add(new TreeItem<String>(files.get(i),
                  new Label("", fontAwesome.create(FontAwesome.Glyph.FILE_VIDEO_ALT)
                      .color(Color.MEDIUMSEAGREEN))));
          break;
        case "zip":
          treeRoot.getChildren().add(new TreeItem<String>(files.get(i), new Label("", fontAwesome
              .create(FontAwesome.Glyph.FILE_ZIP_ALT).color(Color.SLATEBLUE))));
          break;
        case "ppt":
          treeRoot.getChildren()
              .add(new TreeItem<String>(files.get(i),
                  new Label("", fontAwesome.create(FontAwesome.Glyph.FILE_POWERPOINT_ALT)
                      .color(Color.LIME))));
          break;
        case "xls":
          treeRoot.getChildren().add(new TreeItem<String>(files.get(i), new Label("", fontAwesome
              .create(FontAwesome.Glyph.FILE_EXCEL_ALT).color(Color.RED))));
          break;
        default:
          treeRoot.getChildren().add(new TreeItem<String>(files.get(i), new Label("", fontAwesome
              .create(FontAwesome.Glyph.FILE_ALT).color(Color.CADETBLUE))));
          break;
      }
    }
    this.view.getRemoteFSP().populateRemoteTree(treeRoot);
  }

  public void refreshLocalViewStatus() {
    this.view.getSP().makeCommandStatusUpdate("Refreshing Local File System ...");
    this.view.getSP().makeGeneralStatusUpdate("Local File System up to date.");
    this.refreshLocalView();
  }

  public void refreshLocalView() {

    ArrayList<String> files = this.view.getLocalFSP().populateTree();

    TreeItem<String> treeRoot = new TreeItem<String>(files.get(0), new Label("", fontAwesome
        .create(FontAwesome.Glyph.FOLDER_OPEN).color(Color.GOLDENROD)));
    treeRoot.setExpanded(true);

    for (int i = 1; i < files.size(); i++) {
      String ext = files.get(i);
      String[] e = ext.split("\\.");

      switch (e[1]) {
        case "mp3":
          treeRoot.getChildren()
              .add(new TreeItem<String>(files.get(i),
                  new Label("", fontAwesome.create(FontAwesome.Glyph.FILE_AUDIO_ALT)
                      .color(Color.BLUEVIOLET))));
          break;
        case "pdf":
          treeRoot.getChildren()
              .add(new TreeItem<String>(files.get(i),
                  new Label("", fontAwesome.create(FontAwesome.Glyph.FILE_PDF_ALT)
                      .color(Color.DARKORANGE))));
          break;
        case "java":
        case "sql":
        case "c":
          treeRoot.getChildren()
              .add(new TreeItem<String>(files.get(i),
                  new Label("", fontAwesome.create(FontAwesome.Glyph.FILE_CODE_ALT)
                      .color(Color.DARKCYAN))));
          break;
        case "jpg":
        case "png":
          treeRoot.getChildren().add(new TreeItem<String>(files.get(i), new Label("", fontAwesome
              .create(FontAwesome.Glyph.FILE_PHOTO_ALT).color(Color.PURPLE))));
          break;
        case "txt":
          treeRoot.getChildren().add(new TreeItem<String>(files.get(i), new Label("", fontAwesome
              .create(FontAwesome.Glyph.FILE_TEXT_ALT).color(Color.DARKBLUE))));
          break;
        case "doc":
          treeRoot.getChildren().add(new TreeItem<String>(files.get(i), new Label("", fontAwesome
              .create(FontAwesome.Glyph.FILE_WORD_ALT).color(Color.BLUE))));
          break;
        case "mp4":
          treeRoot.getChildren()
              .add(new TreeItem<String>(files.get(i),
                  new Label("", fontAwesome.create(FontAwesome.Glyph.FILE_VIDEO_ALT)
                      .color(Color.MEDIUMSEAGREEN))));
          break;
        case "zip":
          treeRoot.getChildren().add(new TreeItem<String>(files.get(i), new Label("", fontAwesome
              .create(FontAwesome.Glyph.FILE_ZIP_ALT).color(Color.SLATEBLUE))));
          break;
        case "ppt":
          treeRoot.getChildren()
              .add(new TreeItem<String>(files.get(i),
                  new Label("", fontAwesome.create(FontAwesome.Glyph.FILE_POWERPOINT_ALT)
                      .color(Color.LIME).useGradientEffect())));
          break;
        case "xls":
          treeRoot.getChildren().add(new TreeItem<String>(files.get(i), new Label("", fontAwesome
              .create(FontAwesome.Glyph.FILE_EXCEL_ALT).color(Color.RED))));
          break;
        default:
          treeRoot.getChildren().add(new TreeItem<String>(files.get(i), new Label("", fontAwesome
              .create(FontAwesome.Glyph.FILE_ALT).color(Color.CADETBLUE))));
          break;
      }
    }

    long byteSize = 0;

    for (int i = 1; i < files.size(); i++) {
      File f = new File("C:\\Users\\Jonny\\Desktop\\LocalFiles\\" + files.get(i));
      byteSize += f.length();
    }

    this.view.getLocalFSP().setStatusLabel(1, files.size() - 1, byteSize);
    this.view.getLocalFSP().repopulateTree(treeRoot);
  }



}
