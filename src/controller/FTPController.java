package controller;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.FTPUser;
import model.NotStayingHere;
import view.FTPRootPane;


public class FTPController {
	
	private FTPRootPane view;
	private FTPUser model;
	private NotStayingHere fc;
    private static final int NTHREADS = 10;
    private static final Executor exec = Executors.newFixedThreadPool(NTHREADS); 
	
	
	
	public FTPController (FTPRootPane view, FTPUser model)
	{
		this.view = view;
		this.model = model;
		this.fc = new NotStayingHere("localhost", 4446);
		this.attachEventHandlers();
	}
	
	
	public void attachEventHandlers()
    {
		this.view.getLocalFSP().addUploadAction(e -> this.uploadEvent());
		this.view.getLocalFSP().addRefreshAction(e -> this.refreshEvent());
		this.view.getRemoteFSP().addDownloadAction(e -> this.downloadEvent());
    }
	
	public void refreshEvent()
	{
		this.view.getLocalFSP().repopulateTree();
	}
		
	public void uploadEvent()
	{
        Runnable task = new Runnable(){
            public void run (){
              try {
				upload();
			} catch (Exception e) {
				e.printStackTrace();
			}
            }
          };   
        exec.execute(task); 
	}
	
	public void downloadEvent()
	{
        Runnable task = new Runnable(){
            public void run (){
              try {
				download();
			} catch (Exception e) {
				e.printStackTrace();
			}
            }
          };     
        exec.execute(task);
	}
	
    public void download() throws Exception 
    {
    	this.fc.receiveFile();
    }
   
    public void upload() throws Exception 
    {
    	this.fc.sendFile(this.view.getLocalFSP().getSelectedItem()); 	
    }
    
    
}
