package view;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.function.Function;

import javax.swing.ImageIcon;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class FTPLocalFileSystemPane extends VBox {
	
    private String ROOT_FOLDER = "C:/Users/Jonny/Desktop/LocalFiles"; // TODO: change or make selectable
    private String selectedItem = "";
    private Button b1, b2;
    private TreeView<Path> local;
    private TreeItem<Path> treeItem;
    private Label l1;
    private HBox bg;
	
	public FTPLocalFileSystemPane ()
	{
	    local = new TreeView<Path>(this.populateTree());
        l1 = new Label("Local File System: "); 
        
        bg = new HBox();
        b1 = new Button();
        b1.setText("Upload");
        b2 = new Button();
        b2.setText("Refresh");
        bg.getChildren().addAll(b1, b2);
        
        
        local.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> selectedItem = newValue.getValue().toString());
        
//        b2 = new Button("Get Selected");
//        b2.setOnAction(e -> System.out.println("Button Press" + selectedItem));
        
	    this.getChildren().addAll(l1, local, bg);
	}

	public TreeItem<Path> populateTree()
	{
		// create root
        treeItem = new TreeItem<Path>();
        treeItem.setValue(Paths.get("C:/Users/Jonny/Desktop/LocalFiles"));     
        treeItem.setExpanded(true);

        // create tree structure
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
            	System.out.println(t.getValue().toString().toLowerCase());
                return t.getValue().toString().toLowerCase();
            }
        }));

        	    
        return treeItem;
	    
	}
	
   public static void createTree(TreeItem<Path> rootItem) throws IOException 
    {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(rootItem.getValue())) 
        {
            for (Path path : directoryStream) 
            {
//                Path p = path.getFileName();
//                System.out.println("This is newItem string -> " + p);
 
            	TreeItem<Path> newItem = new TreeItem<Path>(path);
                newItem.setExpanded(true);
                // newItem.setGraphic(new ImageView(folderExpandImage));
                // System.out.println("This is newItem syso -> " + newItem);
                
                rootItem.getChildren().add(newItem);
                
                // testing this so can see what we need from server file system
                	// if we can turn a string into a path then we're good
                	// as all a path is really just -> "C://Users/Jonny/Desktop/Whatever.txt
                	// So nothing special
                	// but still may have to re-write this function no biggie!
                
               // System.out.println("This is path syso -> " + path);

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
	
	public void repopulateTree()
	{	
	    local.setRoot(this.populateTree());
	}
	
	public String getSelectedItem()
	{
		return this.selectedItem;
	}

}
