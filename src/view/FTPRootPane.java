package view;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.FTPTransaction;

public class FTPRootPane extends VBox {
  private Button b1;
  private Label l1, l2, l3;
  private TextField t1, t2;
  private PasswordField p1;
  private FTPLocalFileSystemPane lfsp;
  private FTPRemoteFileSystemPane rfsp;
  private FTPStatusPane ftpsp;
  private TableView<FTPTransaction> transaction;
  private ObservableList<FTPTransaction> transactionList;

  @SuppressWarnings("unchecked")
  public FTPRootPane() {
    l1 = new Label("Host: ");
    l1.setFont(Font.font("Arial", FontWeight.BOLD, 13));
    l1.setPadding(new Insets(4, 0, 0, 0));

    t1 = new TextField();

    l2 = new Label("Password: ");
    l2.setFont(Font.font("Arial", FontWeight.BOLD, 13));
    l2.setPadding(new Insets(4, 0, 0, 0));

    p1 = new PasswordField();

    l3 = new Label("Port: ");
    l3.setFont(Font.font("Arial", FontWeight.BOLD, 13));
    l3.setPadding(new Insets(4, 0, 0, 0));

    t2 = new TextField();

    b1 = new Button();
    b1.setText("Connect");

    // ========== Top Menu bar ============ //
    HBox menu = new HBox();
    menu.setSpacing(10);
    menu.setPadding(new Insets(3, 0, 0, 3));
    menu.getChildren().addAll(this.l1, this.t1, this.l2, this.p1, this.l3, this.t2, this.b1);
    // ========== Top Menu bar ============ //


    // ========== Status pane ============ //
    ftpsp = new FTPStatusPane();
    ftpsp.prefWidthProperty().bind(this.widthProperty());
    ftpsp.prefHeightProperty().bind(this.heightProperty().divide(3));

    // ========== Status pane ============ //

    // ========== File Systems View ============ //
    SplitPane splitFS = new SplitPane();

    lfsp = new FTPLocalFileSystemPane();
    rfsp = new FTPRemoteFileSystemPane();

    lfsp.prefWidthProperty().bind(this.widthProperty().divide(2));
    rfsp.prefWidthProperty().bind(this.widthProperty().divide(2));
    splitFS.getItems().addAll(lfsp, rfsp);

    HBox filesystem = new HBox();
    filesystem.getChildren().addAll(splitFS);
    filesystem.prefWidthProperty().bind(this.widthProperty());
    // ========== File Systems View ============ //

    // ========== Bottom Transaction Result Pane ============ //
    VBox vb2 = new VBox();
    Label l = new Label("Transaction Result ");
    l.setPadding(new Insets(6, 0, 0, 3));
    l.setStyle("-fx-background-color :  #e6e6e6");
    l.setFont(Font.font("Arial", FontWeight.BOLD, 13));
    l.prefWidthProperty().bind(this.widthProperty());


    Separator s1 = new Separator();
    s1.setOrientation(Orientation.HORIZONTAL);
    s1.setStyle("-fx-background-color :  #737373");

    Separator s2 = new Separator();
    s2.setOrientation(Orientation.HORIZONTAL);
    s2.setStyle("-fx-background-color :  #737373");


    transaction = new TableView<FTPTransaction>();

    TableColumn<FTPTransaction, String> lclf =
        new TableColumn<FTPTransaction, String>("Local file");
    lclf.prefWidthProperty().bind(transaction.widthProperty().divide(5));
    lclf.setCellValueFactory(new PropertyValueFactory<FTPTransaction, String>("localFile"));
    lclf.setStyle("-fx-alignment: CENTER");

    TableColumn<FTPTransaction, String> dir = new TableColumn<FTPTransaction, String>("Direction");
    dir.prefWidthProperty().bind(transaction.widthProperty().divide(10));
    dir.setCellValueFactory(new PropertyValueFactory<FTPTransaction, String>("direction"));
    dir.setStyle("-fx-alignment: CENTER");

    TableColumn<FTPTransaction, String> rmtf =
        new TableColumn<FTPTransaction, String>("Remote File");
    rmtf.prefWidthProperty().bind(transaction.widthProperty().divide(5));
    rmtf.setCellValueFactory(new PropertyValueFactory<FTPTransaction, String>("remoteFile"));
    rmtf.setStyle("-fx-alignment: CENTER");

    TableColumn<FTPTransaction, Long> size = new TableColumn<FTPTransaction, Long>("Size");
    size.prefWidthProperty().bind(transaction.widthProperty().divide(10));
    size.setCellValueFactory(new PropertyValueFactory<FTPTransaction, Long>("size"));
    size.setStyle("-fx-alignment: CENTER");

    TableColumn<FTPTransaction, String> stus = new TableColumn<FTPTransaction, String>("Status");
    stus.prefWidthProperty().bind(transaction.widthProperty().divide(5));
    stus.setCellValueFactory(new PropertyValueFactory<FTPTransaction, String>("status"));
    stus.setStyle("-fx-alignment: CENTER");

    TableColumn<FTPTransaction, String> time = new TableColumn<FTPTransaction, String>("Time");
    time.prefWidthProperty().bind(transaction.widthProperty().divide(5));
    time.setCellValueFactory(new PropertyValueFactory<FTPTransaction, String>("time"));
    time.setStyle("-fx-alignment: CENTER");

    transaction.setItems(transactionList);
    transaction.getColumns().addAll(lclf, dir, rmtf, size, stus, time);

    vb2.getChildren().addAll(s1, l, s2, transaction);
    vb2.prefHeightProperty().bind(this.heightProperty().divide(3));



    // ========== Bottom Transaction Result Pane ============ //

    Separator s3 = new Separator();
    s3.setOrientation(Orientation.HORIZONTAL);
    s3.setStyle("-fx-background-color :  #737373");

    Separator s4 = new Separator();
    s4.setOrientation(Orientation.HORIZONTAL);
    s4.setStyle("-fx-background-color :  #737373");


    this.getChildren().addAll(s3, menu, s4, ftpsp, filesystem, vb2);
    this.setStyle("-fx-background-color :  #e6e6e6");
    this.setPrefSize(1200, 600);

  }

  public FTPRemoteFileSystemPane getRemoteFSP() {
    return this.rfsp;
  }

  public FTPLocalFileSystemPane getLocalFSP() {
    return this.lfsp;
  }

  public FTPStatusPane getSP() {
    return this.ftpsp;
  }

  public void addConnectAction(EventHandler<ActionEvent> handler) {
    b1.setOnAction(handler);
  }

  public String getHost() {
    return t1.getText();
  }

  public String getPort() {
    return t2.getText();
  }

  public String getPassword() {
    return p1.getText();
  }

  public synchronized void setItemsTableView(ObservableList<FTPTransaction> transactionList) {
    transaction.setItems(transactionList);
  }

  public synchronized void refreshTableView() {
    transaction.getColumns().get(0).setVisible(false);
    transaction.getColumns().get(0).setVisible(true);
  }


}
