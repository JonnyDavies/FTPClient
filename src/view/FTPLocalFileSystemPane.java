package view;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Function;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Separator;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class FTPLocalFileSystemPane extends VBox {
	
    private String ROOT_FOLDER = "C:/Users/Jonny/Desktop/LocalFiles"; // TODO: change or make selectable
    private String selectedItem = "";
    private Button b1, b2;
    private TreeView<String> local;
    private TreeItem<Path> treeItem;
    private Label l1, status;
    private HBox bg;
    private ArrayList<String> localFiles;
	
	public FTPLocalFileSystemPane ()
	{
	    local = new TreeView<String>();
	    local.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    
        l1 = new Label("Local File System: ");
        l1.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        l1.setPadding(new Insets(3, 0, 0, 3));
        l1.setStyle("-fx-background-color :  #e6e6e6");
        l1.prefWidthProperty().bind(this.widthProperty());

        
        bg = new HBox();
        b1 = new Button();
        b1.setText("Upload File(s)");
        
        b2 = new Button();
        b2.setText("Refresh Directory");
        bg.getChildren().addAll(b1, b2);
        bg.setAlignment(Pos.CENTER);
        bg.setStyle("-fx-background-color :  #f2f2f2");
        
        status = new Label("Not connected to any server.");  
        
        status.setFont(new Font("Arial", 12));
        status.setPadding(new Insets(0, 0, 0, 3));
        status.setStyle("-fx-background-color :  #e6e6e6");
        status.setStyle("-fx-font-weight: bold");
        status.prefWidthProperty().bind(this.widthProperty());
        
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
	    this.getChildren().addAll(s1, l1, s2, local, s3, status,  s4,  bg);
	}

	
	
	public ArrayList<String> populateTree()
	{
		
		localFiles = new ArrayList<String>();
		
        treeItem = new TreeItem<Path>();
        treeItem.setValue(Paths.get("C:/Users/Jonny/Desktop/LocalFiles")); 
        treeItem.setExpanded(true);
        localFiles.add(treeItem.getValue().toString());


        try {
			createTree(treeItem);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        // sort tree structure by name
        treeItem.getChildren().sort( Comparator.comparing( new Function<TreeItem<Path>, String>() 
        {
            @Override
            public String apply(TreeItem<Path> t) {
                return t.getValue().toString().toLowerCase();
            }
        }));

        return localFiles;
	}
	
   public void createTree(TreeItem<Path> rootItem) throws IOException 
    {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(rootItem.getValue())) 
        {
            for (Path path : directoryStream) 
            {
            	localFiles.add(path.getFileName().toString());
 
            	TreeItem<Path> newItem = new TreeItem<Path>(path);
                newItem.setExpanded(true);
                
                rootItem.getChildren().add(newItem);

                if (Files.isDirectory(path)) 
                {
                    createTree(newItem);
                }
            }
        }
    }
 
	public void addUploadAction(EventHandler<ActionEvent> handler) 
	{
      b1.setOnAction(handler);
    }
	
	public void addRefreshAction(EventHandler<ActionEvent> handler) 
	{
      b2.setOnAction(handler);
    }

	public TreeView<String> getTreeLocal()
	{
		return local;
	}
	
	public void repopulateTree(TreeItem<String> value)
	{	
	    local.setRoot(value);
	}
	
	public String getLocalSelectedItem()
	{
		return this.selectedItem;
	}
	
	public void setStatusLabel (int dirSize, int fileNum)
	{
		this.status.setText(dirSize + " Directory: " + fileNum + " Files.");
		
	}

}
