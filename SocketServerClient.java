package application;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import com.kuka.common.ThreadUtil;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
/*******
 * @data 2018-04-12
 * @author Jonas.Shang
 *
 */
public class SocketServerClient   {
		private ServerSocket serverSock=null;
		private Socket serversock = null;
		private Socket clientsocket=null;
		private String strIP = "";
		private int iPort;
		private String sendcontent="";
		private boolean flag=false;
		private String str="";
		private String recvcontent="";
		
	 public SocketServerClient(String strIP,int port)
	 {
		 
		 this.strIP=strIP;
		 this.iPort=port;
		
		 try
		 {
			  clientsocket=new Socket(strIP,iPort);
			  flag=false;
			  
			  
			  /*while(!(ischeck().matches("success")))
			  {
				  clientsocket.close();
				  clientsocket=null;
				  clientsocket=new Socket(strIP,iPort);
				  ThreadUtil.milliSleep(1000);
			  }*/
			  System.out.println("sucess to connect server");
		 }catch(IOException e){
			 e.printStackTrace();
		 }
	 }
	 
	 public SocketServerClient(int port)
	 {
		 this.iPort=port;
	
		 try
		 {
			    serverSock=new ServerSocket(iPort);
			    flag=true;
			    /*while(!(ischeck().matches("success")))
			    {
			    	serverSock.close();
			    	serverSock=null;
			    	serverSock=new ServerSocket(iPort);
			    }*/
			
			 System.out.println("sucess to connect client");
			 System.out.println("sucess to listenning...");
		 }catch(IOException e){
			 e.printStackTrace();
		 }
	 }
	 
	 public String ischeck()
	 {
		 //String str="";
		 /***robot作为客户端***/
		 if(flag==false)
		 {
		 try{
		    OutputStream outputstream=clientsocket.getOutputStream();
			Writer writer=new OutputStreamWriter(outputstream);
			InputStream inputstream=clientsocket.getInputStream();
			BufferedReader recv=new BufferedReader(new InputStreamReader(inputstream));
			
			writer.write("success\r\n");
			writer.flush();
			ThreadUtil.milliSleep(1000);
			//Thread.Util(100);
			str=recv.readLine();
			System.out.println("client-server state is "+str);
		 }catch(IOException e)
		 {
			 e.printStackTrace();
		 }
		 
	     }
		 /***robot 作为服务端***/
		 else if(flag==true)
	     {
	    	 try{
	    		    serversock = serverSock.accept();
					InputStream inputstream=serversock.getInputStream();
					BufferedReader recv=new BufferedReader(new InputStreamReader(inputstream));
					PrintWriter writer = new PrintWriter(serversock.getOutputStream());
					str=recv.readLine();
					System.out.println("server-client state is "+str);
				    //Thread.sleep(100);
				    ThreadUtil.milliSleep(100);	
					writer.println("success\r\n");
					writer.flush();
					
	    	 }catch(IOException e)
	    	 {
	    		 e.printStackTrace();
	    	 }
	     }
		 
		 return str;
	 }
	 
	 
	 
	 
	
		
	 public String SendRecvContent(String str) 
	 {
		 //String recvcontent="";
		 /***robot作为客户端***/
		 if(flag==false)
		 {
		 try{
		    OutputStream outputstream=clientsocket.getOutputStream();
			Writer writer=new OutputStreamWriter(outputstream);
			InputStream inputstream=clientsocket.getInputStream();
			BufferedReader recv=new BufferedReader(new InputStreamReader(inputstream));
			
			writer.write(str);
			writer.flush();
			ThreadUtil.milliSleep(1000);
		    System.out.println("success send");
			//recvcontent=recv.readLine();
			//System.out.println("client recv is "+recvcontent);
			//System.out.println("recv is "+recvcontent);
			/*while((recvcontent!="1")||(recvcontent!="2")||(recvcontent!="3")||(recvcontent!="4")||(recvcontent!="R"))
			{
				writer.write(str);
				writer.flush();
				ThreadUtil.milliSleep(100);
				
				recvcontent="";
				recvcontent=recv.readLine();
				
				ThreadUtil.milliSleep(100);			
			}*/
		    clientsocket.close();
			
		 }catch(IOException e)
		 {
			 e.printStackTrace();
		 }finally{
			 try{
			clientsocket.close();
			 }catch(IOException e)
			 {
				 e.printStackTrace();
			 }
			 
		 }
		 
	     }
		 /***robot 作为服务端***/
		 else if(flag==true)
	     {
	    	 try{
	    		    serversock = serverSock.accept();
					InputStream inputstream=serversock.getInputStream();
					BufferedReader recv=new BufferedReader(new InputStreamReader(inputstream));
					PrintWriter writer = new PrintWriter(serversock.getOutputStream());
					recvcontent=recv.readLine();
				    System.out.println("success to receive");
					System.out.println(" recv is "+recvcontent);
				    ThreadUtil.milliSleep(100);	
					writer.println(str);
					writer.flush();
					/*while((recvcontent!="1")||(recvcontent!="2")||(recvcontent!="3")||(recvcontent!="4")||(recvcontent!="R"))
					{
						writer.write(str);
						writer.flush();
						ThreadUtil.milliSleep(100);	
						
						recvcontent="";
						recvcontent=recv.readLine();
						ThreadUtil.milliSleep(100);	
						 			
					}*/
					 serverSock.close();
					 serversock.close();
		    		
	    	 }catch(IOException e)
	    	 {
	    		
	    		 e.printStackTrace();
	    	 }finally
	    	 {
	    		 try{
	    		 serversock.close();
	    		 serverSock.close();
	    		 }catch(IOException e)
	    		 {
	    			 e.printStackTrace();
	    			 
	    		 }
	    	 }
	     }
		 return recvcontent;
	 }
	
}
