package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Path;

public class FTPRemoteFileSystemPane extends VBox
{
	private Button b2;
	
	public FTPRemoteFileSystemPane ()
	{
        TreeView<Path> remote = new TreeView<Path>();  
        Label l2 = new Label("Remote File System: "); 
        this.b2 = new Button();
        this.b2.setText("Download");
	    	    
	    this.getChildren().addAll(l2, remote, b2);
	}
	
	public void addDownloadAction(EventHandler<ActionEvent> handler) {
	       b2.setOnAction(handler);
    }

}
