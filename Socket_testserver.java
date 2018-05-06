package application;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.inject.Inject;

import org.omg.CORBA.portable.OutputStream;

import app.CallBack;
import app.TcpServer.READ_TYPE;
//import app.TcpServer.ReceiveThread;

import com.kuka.common.ThreadUtil;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;
import com.kuka.roboticsAPI.deviceModel.LBR;

/**
 * Implementation of a robot application.
 * <p>
 * The application provides a {@link RoboticsAPITask#initialize()} and a 
 * {@link RoboticsAPITask#run()} method, which will be called successively in 
 * the application lifecycle. The application will terminate automatically after 
 * the {@link RoboticsAPITask#run()} method has finished or after stopping the 
 * task. The {@link RoboticsAPITask#dispose()} method will be called, even if an 
 * exception is thrown during initialization or run. 
 * <p>
 * <b>It is imperative to call <code>super.dispose()</code> when overriding the 
 * {@link RoboticsAPITask#dispose()} method.</b> 
 * 
 * @see UseRoboticsAPIContext
 * @see #initialize()
 * @see #run()
 * @see #dispose()
 */
public class Socket_testserver extends RoboticsAPIApplication {
	@Inject
	private LBR lBR_iiwa_7_R800_1;
    int port=30003;
	private ServerSocket serverSock=null;
	private Socket clientSock = null;
	private CallBack listener = null;
	private boolean bEnd = false;
	private READ_TYPE readType = READ_TYPE.READ_LINE;
	private ArrayList<Socket> clientList = new ArrayList<Socket>();
	private boolean bDebug = true;
	private String str_server="";
	private String str="come from robot server";
	@Override
	public void initialize() {
		// initialize your application here
		
	}

	@Override
	public void run() {
		
		
		/*try{
		serverSock = new ServerSocket(port);
		}catch(Exception e)
		{
			System.out.println("Server accept error: "+e);
			
		}
		
			System.out.println("Listenning...");
			try {
				clientSock = serverSock.accept();
				InputStream inputstream=clientSock.getInputStream();
				BufferedReader br=new BufferedReader(new InputStreamReader(inputstream));
				//DataOutputStream outstream = new DataOutputStream(clientSock.getOutputStream());
				//OutputStream outstream=(OutputStream) clientSock.getOutputStream();
				PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
				
				//Writer writer=new OutputStreamWriter(outstream);
				ThreadUtil.milliSleep(1000);
				str_server=br.readLine();
				getLogger().info("the content is "+str_server);
				//outstream.write(str);
				//writer.write(str);
				//writer.flush();
				writer.println(str);
				writer.flush();
				
				serverSock.close();
				
			} catch(Exception e) {
				System.out.println("Server accept error: "+e);
				
		}*/
		SocketServerClient serverSock = new SocketServerClient("172.31.1.20",30003);
			String str=serverSock.SendRecvContent("1\r\n");
			System.out.println("recv string is"+str);
	}
}