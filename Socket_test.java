package application;


import javax.inject.Inject;

import app.CallBack;
import app.TcpConnect;
import app.TcpConnect.READ_TYPE;

import com.kuka.common.ThreadUtil;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.task.ITaskLogger;

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
public class Socket_test extends RoboticsAPIApplication {
	@Inject
	private LBR lBR7;
    private Socket socket;
//	private TcpConnect Server;
    public  String str_server="";
	public String str="come from robot\r\n";
	//private String Read;
	@Inject 
	ITaskLogger log;
	 private int count=0;
	 
	
	@Override
	public void initialize() {
		// initialize your application here
		
		/*CallBack listener = new CallBack(){
			@Override
			public void ReceiveContent(Socket sock,String text) {
				System.out.println("Receive text: " + text + ", len:" + text.length());
				Read = text;
			}
			
		};*/
		/*CallBack listenerB = new CallBack(){
			@Override
			public void ReceiveContent(Socket sock,String text) {
				System.out.println("Receive text: " + text + ", len:" + text.length());
				Read = text;
			}
		
		};*/
		
		/*Server = new TcpConnect("172.168.1.19",59152, listener);
		//Server= new TcpConnect(59152,listener);
		Server.setReadType(READ_TYPE.READ_LINE);
		Server.start();
         System.out.println("hello world");		
		//BeltServer = new TcpConnect(30003, listenerB);
		//BeltServer.setReadType(READ_TYPE.READ_LINE);
		//BeltServer.start();
		*/

		
		
		
	}

	@Override
	public void run() {
		//ThreadUtil.milliSleep(50);
		
	for(count=0;count<4;count++)
	{
		Frame ori=getApplicationData().getFrame("/take_pre").copyWithRedundancy();
		ori.setX(ori.getX()-count*87);
		ori.setY(ori.getY());
		ori.setZ(ori.getZ());
		lBR7.move(lin(ori));
		
	}
		
		
		
		
		
		
	}
}