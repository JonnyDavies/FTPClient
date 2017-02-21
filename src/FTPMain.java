import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

public class FTPMain extends Application {
    
	//Create a threadpool    
    private static final int NTHREADS = 10;
    private static final Executor exec = Executors.newFixedThreadPool(NTHREADS); 
    private TempClient fc;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("FTP");
        Button btn = new Button();
        btn.setText("Upload");

        Button btn2 = new Button();
        btn2.setText("Download");
        
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
            	         	
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
    	           	     	
               // repeat the above code to do two  
            } 
        });
        
        btn2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
            	         	
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
        });
        
        
        
        // add scroll panes to everything       
        // initialise client socket and get connection with server 
        
        this.fc = new TempClient("localhost", 4446);	
        

        VBox root = new VBox();
        SplitPane splitFS = new SplitPane(); 
        
        HBox filesystem = new HBox();
        HBox menu = new HBox();

        menu.getChildren().addAll(btn, btn2);
        TreeView<Path> local = new TreeView<Path>();   
        TreeView<Path> remote = new TreeView<Path>();  
        TextArea text = new TextArea();
        TextArea text2 = new TextArea();
        
        local.prefWidthProperty().bind(root.widthProperty().divide(2));
        remote.prefWidthProperty().bind(root.widthProperty().divide(2));
        
        splitFS.getItems().addAll(local, remote);
        filesystem.getChildren().addAll(splitFS);
        filesystem.prefWidthProperty().bind(root.widthProperty());
        root.getChildren().addAll(menu, text, filesystem, text2);
        
            
        primaryStage.setScene(new Scene(root, 1200, 600));
        primaryStage.show();
    }

//    public void runClient() throws Exception {
//    	
//    	TempClient fc = new TempClient("localhost", 4446, "C:\\Users\\Jonny\\Desktop\\dog.jpg");
//    }
//    
//    public void runClient2() throws Exception {
//    	
//		TempClient fc = new TempClient("localhost", 4446, "C:\\Users\\Jonny\\Desktop\\cat.jpg");
//    }
    
    public void download() throws Exception 
    {
    	this.fc.receiveFile();
    }
   
    public void upload() throws Exception 
    {
    	this.fc.sendFile("C:\\Users\\Jonny\\Desktop\\dog.jpg");
    }
    
}