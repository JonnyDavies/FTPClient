package view;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;

public class FTPRootPane extends VBox
{
	private Button b1;
	private Label l1, l2, l3;
	private TextField t1, t2;
	private PasswordField p1;
	private FTPLocalFileSystemPane lfsp;
	private FTPRemoteFileSystemPane rfsp;
	
	public FTPRootPane()
	{   
        l1 = new Label("Host: ");
        t1 = new TextField();
   
        l2 = new Label("Password: ");
        p1 = new PasswordField();
        
        l3 = new Label("Port: ");
        t2 = new TextField();
        
        b1 = new Button();
        b1.setText("Connect");

        // ========== Top Menu bar ============ //
        HBox menu = new HBox();
        menu.setSpacing(10);
        menu.getChildren().addAll(this.l1, this.t1, this.l2, this.p1, this.l3, this.t2, this.b1);
        // ========== Top Menu bar ============ //

        
        // ========== Status bar ============ //
        VBox vb1 = new VBox();
        TextArea text = new TextArea();
        vb1.getChildren().addAll(text);
        // ========== Status bar ============ //
        
       
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
        Label l = new Label("Transaction Result: ");  
        TextArea text2 = new TextArea();
        vb2.getChildren().addAll(l, text2);
        // ========== Bottom Transaction Result Pane ============ //

        
        this.getChildren().addAll(menu, vb1, filesystem, vb2);
        this.setPrefSize(1200, 600);
		
	}
	
	public  FTPRemoteFileSystemPane getRemoteFSP()
	{
		return this.rfsp;
	}

	public  FTPLocalFileSystemPane getLocalFSP()
	{
		return this.lfsp;
	}

}
