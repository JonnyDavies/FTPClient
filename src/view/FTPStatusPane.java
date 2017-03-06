package view;


import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class FTPStatusPane extends ScrollPane {

  private TextFlow text;
  private String status;

  public FTPStatusPane() {
    text = new TextFlow();
    text.setStyle("-fx-background-color :  #ffffff");
    text.setDisable(false);
    
    Text t1 = new Text("Initialising application.");
    text.getChildren().add(t1);

    this.makeSuccessStatusUpdate("Application intialised.");
    Text t2 =
        new Text("\nRetrieving directory listing for C:\\Users\\Jonny\\Desktop\\LocalFiles ...");
    Text t3 = new Text("\nDirectory listing received for C:\\Users\\Jonny\\Desktop\\LocalFiles.");

    text.getChildren().addAll(t2, t3);
    text.prefWidthProperty().bind(this.widthProperty());
    this.setContent(text);
    this.setStyle("-fx-background-color :  #ffffff");
    this.vvalueProperty().bind(text.heightProperty());

  }

  public String getStatusText() {
    return status;
  }

  public void setStatusStringInBox(Text t) {
    text.getChildren().add(t);
  }

  public void makeGeneralStatusUpdate(String status) {
    Text newStatus = new Text("\nStatus: " + status);
    // newStatus.setStyle("-fx-font-weight: bold");
    this.setStatusStringInBox(newStatus);
  }

  public void makeErrorStatusUpdate(String status) {
    Text newStatus = new Text("\nError: " + status);
    newStatus.setFill(Color.RED);
    this.setStatusStringInBox(newStatus);
  }

  public void makeCommandStatusUpdate(String status) {
    Text newStatus = new Text("\nCommand: " + status);
    newStatus.setFill(Color.BLUE);
    this.setStatusStringInBox(newStatus);
  }

  public void makeSuccessStatusUpdate(String status) {
    Text newStatus = new Text("\nSuccess: " + status);
    newStatus.setFill(Color.GREEN);
    this.setStatusStringInBox(newStatus);
  }
}
