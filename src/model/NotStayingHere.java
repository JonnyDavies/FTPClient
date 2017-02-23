package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class NotStayingHere {
	

	private static Socket s;
	private DataOutputStream dos;
	private DataInputStream dis;
	
	public NotStayingHere(String host, int port)
	{
		try 
		{
			s = new Socket(host, port);
			this.dos = new DataOutputStream(s.getOutputStream());
			this.dis = new DataInputStream(s.getInputStream());

			// this.startSocketListener();
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
		
		// filename?	
		String []s = file.split("\\\\");
        this.dos.writeUTF(s[s.length - 1]);

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
	}
	
	public void receiveFile() throws IOException 
	{
			
		this.dos.writeUTF("Download");
		
		String msgFromServer = this.dis.readUTF();
		
		if(msgFromServer.compareTo("READY")==0)
        {
            System.out.println("Receiving File ...");
            
            
            Random rdm = new Random();
            FileOutputStream fout = new FileOutputStream("testfileFromServer" + rdm.nextInt(100) + ".jpg");
            
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
		
        }	
	}
}
