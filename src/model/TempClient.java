package model;


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
	private DataInputStream dis;
	private boolean connected;
	

	
	
	public TempClient(String host, int port, String file)
	{
		try 
		{
			s = new Socket(host, port);
			connected = true;
			this.dos = new DataOutputStream(s.getOutputStream());
			this.dis = new DataInputStream(s.getInputStream());

		    // this.sendFile(file);
			 
		    this.receiveFile(file);
		} 
		catch (Exception e) 
		{	
			connected = false;
			// e.printStackTrace();
		}		
	}
	

	public boolean getConnectionStatus()
	{
		return this.connected;
	}
	
    public void sendFile(String file) throws IOException 
	{
		
		// action
		this.dos.writeUTF("Upload");
		
		// filename?	
        this.dos.writeUTF(file);

		// actual file
        FileInputStream fin = new FileInputStream("C:\\Users\\Jonny\\Desktop\\LocalFiles\\" + file);
            
        int ch;
        
        do
        {
            ch = fin.read();
            this.dos.writeUTF(String.valueOf(ch));
        }
        while(ch!=-1);
        
        fin.close();
        
		this.dos.writeUTF("Additional");


	}
    
    public void receiveFile(String file) throws IOException 
	{
		// action	
		this.dos.writeUTF("Download");
		
		String []s = file.split("\\\\");
        this.dos.writeUTF(s[s.length - 1]);
		
		String msgFromServer = this.dis.readUTF();
		
		if(msgFromServer.compareTo("READY") == 0)
        {
            System.out.println("Receiving File ...");
            
            FileOutputStream fout = new FileOutputStream("C:\\Users\\Jonny\\Desktop\\LocalFiles\\" + file);
            
            int ch;
            String temp;
            
            do
            {
                temp = this.dis.readUTF();
                ch = Integer.parseInt(temp);
                if(ch!=-1)
                {
                    fout.write(ch);                    
                }
            }
            while(ch!=-1);
            
            fout.close();
            
    		this.dos.writeUTF("Additional");
		
        }	
	}

	
}
