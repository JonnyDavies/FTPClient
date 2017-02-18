

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class FTPClient {
	
	private Socket s;
	
	public FTPClient(String host, int port, String file) {
		try 
		{
			s = new Socket(host, port);
			sendFile(file);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}		
	}
	
	public void sendFile(String file) throws IOException {
		
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		FileInputStream fis = new FileInputStream(file);
		
		double size = file.length();
		System.out.println(size);

		
		byte[] buffer = new byte[4096];
		
		while (fis.read(buffer) > 0) {
			dos.write(buffer);
		}
		
		fis.close();
		dos.close();	
	}
	
	public static void main(String[] args) 
	{
		FTPClient fc = new FTPClient("localhost", 4446, "C:\\Users\\Jonny\\Desktop\\cat.jpg");
	}

}
