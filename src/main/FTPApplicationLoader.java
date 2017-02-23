package main;

import controller.FTPController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import model.FTPUser;
import view.FTPRootPane;

public class FTPApplicationLoader extends Application{
	
	private FTPRootPane view;
	private FTPController controller;
	private FTPUser model;

	public void init() 
	{      
		this.view = new FTPRootPane(); 		    
	    this.model = new FTPUser(); 
	    this.controller = new FTPController(view, model);
	}
		
	public void start(Stage window) throws Exception 
	{
		Scene mainWindow = new Scene(view);		
		window.setTitle("FTP");
		window.setScene(mainWindow);
		window.show();   
    }
	
    public static void main(String[] args)
    {
        launch(args);
    }
}
