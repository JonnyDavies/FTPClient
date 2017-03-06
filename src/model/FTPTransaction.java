package model;


public class FTPTransaction {

  private String localFile;
  private String direction;
  private String remoteFile;
  private Long size;
  private String status;
  private String time;
  private int id;
  private static int numberOfTransactions = 0;

  public FTPTransaction() {
    this.localFile = "";
    this.direction = "->";
    this.remoteFile = "";
    this.size = 0L;
    this.status = "";
    this.time = "";
    this.id = ++numberOfTransactions;
  }

  public FTPTransaction(String localFile, String direction, String remoteFile, Long size,
      String status, String time) {
    this.localFile = localFile;
    this.direction = direction;
    this.remoteFile = remoteFile;
    this.size = size;
    this.status = status;
    this.time = time;
    this.id = ++numberOfTransactions;
  }

  public void setLocalFile(String localFile) {
    this.localFile = localFile;
  }

  public String getLocalFile() {
    return this.localFile;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  public String getDirection() {
    return this.direction;
  }

  public void setRemoteFile(String remoteFile) {
    this.remoteFile = remoteFile;
  }

  public String getRemoteFile() {
    return this.remoteFile;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public Long getSize() {
    return this.size;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return this.status;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getTime() {
    return this.time;
  }


  public int getId() {
    return this.id;
  }


  public String toString() {
    return "FTPTransaction[ Local File = " + localFile + "Direction = " + direction
        + " Remote File = " + remoteFile + " Size = " + size + " Status=" + status + " Time = "
        + time + " Id = " + id + "]";
  }


}
