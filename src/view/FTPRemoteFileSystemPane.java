package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class FTPRemoteFileSystemPane extends VBox
{
	private Button b1, b2;
	private HBox bg;
	private TreeView<String> remote;
	private String selectedItem = "";
	private Label status;
	
	
	public FTPRemoteFileSystemPane ()
	{
        remote = new TreeView<String>();  
	    remote.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Label l2 = new Label("Remote File System: ");  
        l2.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        l2.setPadding(new Insets(3, 0, 0, 3));
        l2.setStyle("-fx-background-color :  #e6e6e6");
        l2.prefWidthProperty().bind(this.widthProperty());

        bg = new HBox();
        b1 = new Button();
        b1.setText("Download File(s)");

        
        b2 = new Button();
        b2.setText("Refresh Directory");

        bg.getChildren().addAll(b1, b2);
        bg.setAlignment(Pos.CENTER);
        bg.setStyle("-fx-background-color :  #f2f2f2");
        
        status = new Label("Not connected to any server.");  
        status.setFont(new Font("Arial", 12));
        status.setPadding(new Insets(0, 0, 0, 3));
        status.setStyle("-fx-background-color :  #e6e6e6");
        status.prefWidthProperty().bind(this.widthProperty());
        status.setStyle("-fx-font-weight: bold");
        
        
        Separator s1 = new Separator();
        s1.setOrientation(Orientation.HORIZONTAL);
        s1.setStyle("-fx-background-color :  #737373");
        
        Separator s2 = new Separator();
        s2.setOrientation(Orientation.HORIZONTAL);
        s2.setStyle("-fx-background-color :  #737373");

        Separator s3 = new Separator();
        s3.setOrientation(Orientation.HORIZONTAL);
        s3.setStyle("-fx-background-color :  #737373");
        
        Separator s4 = new Separator();
        s4.setOrientation(Orientation.HORIZONTAL);
        s4.setStyle("-fx-background-color :  #737373");
        

        this.setSpacing(2);	  
        this.setStyle( "-fx-background-color :  #e6e6e6");
	    this.getChildren().addAll(s1, l2, s2, remote, s3, status, s4, bg);
	}
	
	public void addDownloadAction(EventHandler<ActionEvent> handler) {
	       b1.setOnAction(handler);
    }
	
	public void addRefreshRemoteAction(EventHandler<ActionEvent> handler) {
	       b2.setOnAction(handler);
    }

	public void populateRemoteTree(TreeItem<String> value)
	{
		 this.remote.setRoot(value);
	}
	
	public String getRemoteSelectedItem()
	{
		return this.selectedItem;
	}

	public TreeView<String> getTreeRemote()
	{
		return remote;
	}
	
	public Label getRemoteStatus()
	{
		return status;
	}
	
	public void setRemoteStatus(String status)
	{
		this.status.setText(status);
	}
	
	public void setStatusLabel (int dirSize, int fileNum)
	{
		this.status.setText(dirSize + " Directory: " + fileNum + " Files.");
		
	}
	

}
