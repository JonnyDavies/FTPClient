package model;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;


public class FTPClientAdditional {

  private static Socket s;
  private DataOutputStream dos;
  private DataInputStream dis;
  private boolean connected;

  public FTPClientAdditional(String host, int port, String file) {
    try {
      s = new Socket(host, port);
      connected = true;
      this.dos = new DataOutputStream(s.getOutputStream());
      this.dis = new DataInputStream(s.getInputStream());
    } catch (Exception e) {
      connected = false;
    }
  }

  public boolean getConnectionStatus() {
    return this.connected;
  }

  public void sendFile(String file) throws IOException {

    // action
    this.dos.writeUTF("Upload");

    // filename?
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

  public long fileSize(String file) throws IOException {
    long size = 0;
    // action
    this.dos.writeUTF("Size");

    String[] s = file.split("\\\\");
    this.dos.writeUTF(s[s.length - 1]);

    String msgFromServer = this.dis.readUTF();

    if (msgFromServer.compareTo("READY") == 0) {

      size = this.dis.readLong();
    }
    return size;
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

  public void closeDownAdditional() {
    try {
      this.dos.writeUTF("Additional");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
