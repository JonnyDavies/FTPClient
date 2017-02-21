

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
	
	public TempClient(String host, int port)
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
	
	
	public void startSocketListener()
    {
      FirstLineService service = new FirstLineService();
      service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
        @Override
        public void handle(WorkerStateEvent t) {
            System.out.println("done:" + t.getSource().getValue());
        }
      });
      service.start();   
	}
	    
    private static class FirstLineService extends Service<String> 
    {
      
      protected Task<String> createTask() {
                
         return new Task<String>() {
              protected String call() 
                  throws IOException, MalformedURLException {     
                try (
                			                		
                    // 10.34.98.62 Uni IP
                    // 192.168.1.20 ethernet
                
                	DataInputStream dis = new DataInputStream(s.getInputStream());
                		
                )  
                {  
				int read = 0;
				while (true)
				{
              
	                	Random rdm = new Random();
	    		    	
	    		    	// store these files for when send them back	    	
	                	FileOutputStream fos = new FileOutputStream("ClientTest" + rdm.nextInt(10) + ".jpg");
	    				
	    				byte[] buffer = new byte[4096];
	    				
	    				int filesize = 136356; // Send file size in separate msg
	    				int totalRead = 0;
	    				int remaining = filesize;
	    				
	    				while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0)
	    				{
	    					totalRead += read;
	    					remaining -= read;
	    					System.out.println("read " + totalRead + " bytes.");
	    					fos.write(buffer, 0, read);
	    				}
	    				
	    				fos.close();
	    				// dis.close();
                    
    				}
                }
                catch (UnknownHostException e) 
                {
                    System.err.println("Don't know about host");
                    System.exit(1);
                    
                } 
                catch (IOException e) 
                {
                    System.err.println("Couldn't get I/O for the connection to ");
                    System.exit(1);
                }              
                return "Test";
              }
          };
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
		// dos.flush();
		// dos.close();
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
