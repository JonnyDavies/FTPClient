package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.FTPUser;
import model.NotStayingHere;
import model.TempClient;
import view.FTPRootPane;


public class FTPController {
	
	private FTPRootPane view;
	private FTPUser model;
	private NotStayingHere fc;
    private static final int NTHREADS = 10;
    private static final Executor exec = Executors.newFixedThreadPool(NTHREADS); 
    private ExecutorService	executor  = Executors.newFixedThreadPool(10, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
          Thread t = new Thread(r);
          t.setDaemon(true);
          return t;
        }
    });
    private final Image rootIconFolder = new Image(getClass().getResourceAsStream("folder.png"));
    private final Image rootIconDoc = new Image(getClass().getResourceAsStream("doc.png"));

    
	public FTPController (FTPRootPane view, FTPUser model)
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
				
		System.out.println(this.view.getHost());
		System.out.println(this.view.getPort());
		System.out.println(this.view.getPassword());
		
		/** we need validation for the port text field **/
		 
		String host     = this.view.getHost();
		String port     = this.view.getPort();
		String password = this.view.getPassword();
		
		
		// implement password
		this.fc = new NotStayingHere(host, Integer.parseInt(port)); 
		
		if(this.fc.getConnectionStatus())
		{
			System.out.println("Connected!");
			this.view.getSP().makeCommandStatusUpdate("Attempting to connect to server ...");
	    	this.view.getSP().makeGeneralStatusUpdate("Connection established with server.");
	    	this.view.getSP().makeGeneralStatusUpdate("Autheticating client with server ....");
			
			// NOW AUTHENTICATE
			try 
			{
				boolean res = this.fc.isPasswordValid(password);
				if(res)
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
			System.out.println("Not Connected :(");
			this.view.getSP().makeErrorStatusUpdate("Failed to establish a connection.");

		}
		
	}
	
	public void refreshEvent()
	{
		this.refreshLocalViewStatus();
		
	}
		
	public void uploadEvent()
	{
	    
    	ObservableList<TreeItem<String>> file = this.view.getLocalFSP().getTreeLocal().getSelectionModel().getSelectedItems();
    	
    	
    	// IF ONES CURRENTLY BEING SENT USE THE OTHER THREADS, PUT THIS CHECK IN. Or if nothings selected 
    	
        Runnable task = new Runnable(){
            public void run (){
            	try {
				upload(file.get(0).getValue().toString());
			} catch (Exception e) {
			e.printStackTrace();
			}
            }
          };   
        exec.execute(task); 
        
        
        for(int i = 1; i < file.size(); i++){
        	
        	String fileSend = file.get(i).getValue().toString();
        	
        	exec.execute(new Runnable(){
                   public void run (){
                     try {
                   additionalUpload(fileSend);
        			} catch (Exception e) {
 
      				e.printStackTrace();
       			}
                   }
            }); 
        }
       
        
	}
    	
   
         

	
	public void downloadEvent()
	{
		 // String file = "Test";
		
	    
    	ObservableList<TreeItem<String>> file = this.view.getRemoteFSP().getTreeRemote().getSelectionModel().getSelectedItems();
		
    	   Runnable task = new Runnable(){
               public void run (){
               	try {
               		download(file.get(0).getValue().toString());
   			} catch (Exception e) {
   			e.printStackTrace();
   			}
               }
             };   
           exec.execute(task); 
           
           
           for(int i = 1; i < file.size(); i++){
           	
           	String fileSend = file.get(i).getValue().toString();
           	
           	exec.execute(new Runnable(){
                      public void run (){
                        try {
                        additionalDownload(fileSend);
           			} catch (Exception e) {
    
         				e.printStackTrace();
          			}
                      }
               }); 
           }
	        
     }

	
	public void refreshRemoteEvent()
	{
        Runnable task = new Runnable(){
            public void run (){
              try {
				remote();
			} catch (Exception e) {
				e.printStackTrace();
			}
            }
          };     
        exec.execute(task);
	}
	
    public void download(String file) throws Exception 
    {
   		// add transaction to pane
//	    Platform.runLater(new Runnable() {
//           public void run() {
//       		view.getSP().makeCommandStatusUpdate("Attempting to download " + file + " from server...");
//           }
//         });
	    
    	this.fc.receiveFile(file);
   		
    }
    
    
	/** 		STAYING IT IS NOT 		**/
    public void additionalDownload(String file) throws Exception 
    {
//	   Platform.runLater(new Runnable() {
//           public void run() {
//       		view.getSP().makeCommandStatusUpdate("Attempting to download " + file + " from server...");
//           }
//         });
//	   
    	TempClient fc = new TempClient("localhost", 4446, file);
    }
    
    

    public void upload(String file) throws Exception 
    {
//        Platform.runLater(new Runnable() {
//            public void run() {
//        	    view.getSP().makeCommandStatusUpdate("Attempting to upload " + file + " to server...");
//            }
//          });
        
        this.fc.sendFile(file);
    }
    
    
	/** 		STAYING IT IS NOT 		**/
    public void additionalUpload(String file) throws Exception 
    {
	   // this.view.getSP().makeCommandStatusUpdate("Attempting to upload " + file + " to server...");
    	TempClient fc = new TempClient("localhost", 4446, file);
    }
    
    
    public void remote() throws Exception 
    {
    	ArrayList<String> files = this.fc.remoteFile();
    	
	    Platform.runLater(new Runnable() {
          public void run() {
  			refreshRemoteView(files);                        
          }
        });
    }

    
    /** TEMP FUNCITON **/ 
    public void refreshRemoteView(ArrayList<String> files)
    {   
    	this.view.getSP().makeCommandStatusUpdate("Refreshing Remote File System ...");
    	this.view.getSP().makeGeneralStatusUpdate("Remote File System up to date.");
    
    	TreeItem<String> treeRoot = new TreeItem<String>(files.get(0),new ImageView(rootIconFolder));
    	treeRoot.setExpanded(true);
    	
    	this.view.getRemoteFSP().setStatusLabel(1, files.size()- 1);


    	for(int i = 1; i < files.size(); i++)
    	{	
    		treeRoot.getChildren().add(new TreeItem<String>(files.get(i), new ImageView( rootIconDoc)));
    	}

        this.view.getRemoteFSP().populateRemoteTree(treeRoot);
    }
    
    public void refreshLocalViewStatus()
    {
    	// update status pane
    	this.view.getSP().makeCommandStatusUpdate("Refreshing Local File System ...");
    	this.view.getSP().makeGeneralStatusUpdate("Local File System up to date.");
    	this.refreshLocalView();
    }
    
    public void refreshLocalView()
    {  
    	
    	ArrayList<String> files = this.view.getLocalFSP().populateTree();
    	this.view.getLocalFSP().setStatusLabel(1, files.size()- 1);
    	
    	TreeItem<String> treeRoot = new TreeItem<String>(files.get(0),new ImageView(rootIconFolder));
    	treeRoot.setExpanded(true);

    	for(int i = 1; i < files.size(); i++)
    	{	
    		treeRoot.getChildren().add(new TreeItem<String>(files.get(i), new ImageView(rootIconDoc)));
    	}

        this.view.getLocalFSP().repopulateTree(treeRoot);        
    }

    
    
}
