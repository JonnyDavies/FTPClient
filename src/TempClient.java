

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class TempClient {
	
	private static Socket s;
	private DataOutputStream dos;
	
	public TempClient(String host, int port, String file)
	{
		try 
		{
			s = new Socket(host, port);
			this.dos = new DataOutputStream(s.getOutputStream());
			this.sendFile(file);
		} 
		catch (Exception e) 
		{
			  e.printStackTrace();
		}		
	}
	
	

	
	
    public void sendFile(String file) throws IOException 
	{
		
		// action
		this.dos.writeUTF("Upload");
		
		// file name
		// this.dos.writeUTF("Upload");
		
		// actual file
        FileInputStream fin = new FileInputStream(file);
        
        int ch;
        
        do
        {
            ch = fin.read();
            this.dos.writeUTF(String.valueOf(ch));
        }
        while(ch!=-1);
        
        fin.close();
		
		// s.shutdownOutput();

		dos.close();
		s.close();
	}

	
}
