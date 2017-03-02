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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class NotStayingHere {
	

	private static Socket s;
	private DataOutputStream dos;
	private DataInputStream dis;
	private ArrayList<String> remoteFiles;
	private boolean connected;
	private HashMap<String,Integer> fileTransactions;

	
	public NotStayingHere(String host, int port)
	{
		try 
		{
			s = new Socket(host, port);
			connected = true;
			this.dos = new DataOutputStream(s.getOutputStream());
			this.dis = new DataInputStream(s.getInputStream());
			fileTransactions = new HashMap<String,Integer>();

		} 
		catch (Exception e) 
		{
			 connected = false;
		}		
	}
	
	public boolean getConnectionStatus()
	{
		return this.connected;
	}
	
	public boolean isPasswordValid(String password) throws IOException
	{
		boolean authenticated = false;
		// action
		this.dos.writeUTF("Password");
		
		// send user input password 
		this.dos.writeUTF(password);
		
		
		String authResult = this.dis.readUTF();
		
		if(authResult.compareTo("ACCEPTED") == 0)
		{
			// put status of authenticated etc
			// refresh file view etc
			System.out.println("Password passed authentication <:D");
			authenticated = true;

		}
		else
		{
			// auth failed status
			// connection terminated status - we'll say this but in reality we will stay connected as it throws an error
			System.out.println("Password failed authentication :(");
		}
		
		return authenticated;
		
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
            int test = 0;
            
            do
            {
                temp = this.dis.readUTF();
                ch = Integer.parseInt(temp);
                if(ch!=-1)
                {
                    fout.write(ch);     

                }
                test++;
                System.out.println(test);
            }
            while(ch!=-1);
            
            fout.close();
		
        }	
	}
	
	
	public ArrayList<String> remoteFile() throws IOException 
	{
			
		    // could even pass the directory in here??? Prob not whatever
			remoteFiles = new ArrayList<String>();
			
			// action
			this.dos.writeUTF("Refresh");
			
			String msgFromServer = this.dis.readUTF();
			System.out.println(msgFromServer);
			if(msgFromServer.compareTo("READY") == 0)
	        {
				int size =  this.dis.readInt();
				System.out.println(size);
				
				for(int i = 0; i < size; i++)
				{
					remoteFiles.add(this.dis.readUTF());
				}
		 
	        }
						
			return remoteFiles;	
	 }
	
	public void addFileTransaction(String filename)
	{
		  fileTransactions.put(filename, 0);
	}
	
//	public void addFileTransactionProgress(String filename, Integer byteSoFar)
//	{
//		  fileTransactions.
//	}
}
