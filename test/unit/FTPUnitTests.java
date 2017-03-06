package unit;

import static org.junit.Assert.*;

import org.junit.Test;

import model.FTPTransaction;

public class FTPUnitTests {



  /*** ~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~ FTP Unit Tests ***~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~**/

  // The following Unit tests are related to testing the requirement of sending and receiving
  // multiple files

  FTPTransaction ftpu1 =
      new FTPTransaction("Test Local", "-->>", "Test Remote", 12345L, "In progress...", "12:03:59");

  FTPTransaction ftpu2 =
      new FTPTransaction("Testing L", "<<--", "Testing R", 89L, "Complete", "07:56:00");

  FTPTransaction ftpu3 = new FTPTransaction();

  @Test
  public void FTP01() {
    long size = ftpu1.getSize();
    assertEquals(size, 12345L);
  }

  @Test
  public void FTP02() {
    String local = ftpu1.getLocalFile();
    assertEquals(local, "Test Local");
  }

  @Test
  public void FTP03() {
    String remote = ftpu1.getRemoteFile();
    assertEquals(remote, "Test Remote");
  }

  @Test
  public void FTP04() {
    String dir = ftpu1.getDirection();
    assertEquals(dir, "-->>");
  }

  @Test
  public void FTP05() {
    String status = ftpu1.getStatus();
    assertEquals(status, "In progress...");
  }

  @Test
  public void FTP06() {
    String time = ftpu1.getTime();
    assertEquals(time, "12:03:59");
  }

  @Test
  public void FTP07() {
    long size = ftpu2.getSize();
    assertEquals(size, 89L);
  }

  @Test
  public void FTP08() {
    String local = ftpu2.getLocalFile();
    assertEquals(local, "Testing L");
  }

  @Test
  public void FTP09() {
    String remote = ftpu2.getRemoteFile();
    assertEquals(remote, "Testing R");
  }

  @Test
  public void FTP10() {
    String dir = ftpu2.getDirection();
    assertEquals(dir, "<<--");
  }

  @Test
  public void FTP11() {
    String status = ftpu2.getStatus();
    assertEquals(status, "Complete");
  }

  @Test
  public void FTP12() {
    String time = ftpu2.getTime();
    assertEquals(time, "07:56:00");
  }

  // FTP13-18 testing the default constructor of FTPTransaction  
  @Test
  public void FTP13() {
    long staus = ftpu3.getSize();
    assertEquals(staus, 0L);
  }

  @Test
  public void FTP14() {
    String local = ftpu3.getLocalFile();
    assertEquals(local, "");
  }

  @Test
  public void FTP15() {
    String remote = ftpu3.getRemoteFile();
    assertEquals(remote, "");
  }

  @Test
  public void FTP16() {
    String dir = ftpu3.getDirection();
    assertEquals(dir, "->");
  }

  @Test
  public void FTP17() {
    String staus = ftpu3.getStatus();
    assertEquals(staus, "");
  }

  @Test
  public void FTP18() {
    String time = ftpu3.getTime();
    assertEquals(time, "");
  }
}
