package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class FTPClient {


  private static Socket s;
  private DataOutputStream dos;
  private DataInputStream dis;
  private ArrayList<String> remoteFiles;
  private boolean connected;

  private ArrayList<FTPTransaction> transaction;
  private long byteSizeRemoteFiles;


  public FTPClient(String host, int port) {
    try {
      s = new Socket(host, port);
      connected = true;
      this.dos = new DataOutputStream(s.getOutputStream());
      this.dis = new DataInputStream(s.getInputStream());
      this.transaction = new ArrayList<FTPTransaction>();

    } catch (Exception e) {
      connected = false;
    }
  }

  public boolean getConnectionStatus() {
    return this.connected;
  }

  public boolean isPasswordValid(String password) throws IOException {
    boolean authenticated = false;
    // action
    this.dos.writeUTF("Password");

    // send user input password
    this.dos.writeUTF(password);


    String authResult = this.dis.readUTF();

    if (authResult.compareTo("ACCEPTED") == 0) {

      authenticated = true;

    } else {

    }

    return authenticated;

  }


  public void sendFile(String file) throws IOException {

    // action
    this.dos.writeUTF("Upload");

    // filename
    this.dos.writeUTF(file);

    // actual file
    FileInputStream fin = new FileInputStream("C:\\Users\\Jonny\\Desktop\\LocalFiles\\" + file);


    int ch;

    do {
      ch = fin.read();
      this.dos.writeUTF(String.valueOf(ch));
    } while (ch != -1);

    fin.close();

    String uploadComplete = this.dis.readUTF();

    if (uploadComplete.compareTo("UPLOAD COMPLETE") == 0) {
      return;
    }

    return;
  }

  public void receiveFile(String file) throws IOException {
    // action
    this.dos.writeUTF("Download");

    String[] s = file.split("\\\\");
    this.dos.writeUTF(s[s.length - 1]);

    String msgFromServer = this.dis.readUTF();

    if (msgFromServer.compareTo("READY") == 0) {

      FileOutputStream fout =
          new FileOutputStream("C:\\Users\\Jonny\\Desktop\\LocalFiles\\" + file);

      int ch;
      String temp;

      long size = this.dis.readLong();

      do {
        temp = this.dis.readUTF();
        ch = Integer.parseInt(temp);

        if (ch != -1) {
          fout.write(ch);

        }
      } while (ch != -1);

      fout.close();
    }
  }

  public long fileSize(String file) throws IOException {
    long size = 0;

    this.dos.writeUTF("Size");

    String[] s = file.split("\\\\");
    this.dos.writeUTF(s[s.length - 1]);

    String msgFromServer = this.dis.readUTF();

    if (msgFromServer.compareTo("READY") == 0) {
      size = this.dis.readLong();
    }
    return size;
  }



  public ArrayList<String> remoteFile() throws IOException {

    remoteFiles = new ArrayList<String>();

    // action
    this.dos.writeUTF("Refresh");

    String msgFromServer = this.dis.readUTF();

    if (msgFromServer.compareTo("READY") == 0) {
      int size = this.dis.readInt();

      for (int i = 0; i < size; i++) {
        remoteFiles.add(this.dis.readUTF());
      }
    }
    byteSizeRemoteFiles = this.dis.readLong();
    return remoteFiles;
  }


  public ArrayList<FTPTransaction> getTransactions() {
    return this.transaction;
  }

  public synchronized void addTransactions(FTPTransaction tran) {
    this.transaction.add(tran);
  }

  public long getByteSizeRemoteFiles() {
    return byteSizeRemoteFiles;
  }
}
