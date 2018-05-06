package application;


import javax.inject.Inject;

import app.CallBack;
import app.TcpConnect;
import app.TcpConnect.READ_TYPE;
import app.TcpConnect.ReceiveThread;

import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;
import com.kuka.roboticsAPI.deviceModel.LBR;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import application.SocketServerClient;

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
public class Test_template extends RoboticsAPIApplication {
	@Inject
	private LBR lBR_iiwa_7_R800_1;
	private TcpConnect Server;
	private String Read;
	private ServerSocket serverSock=null;
	private Socket clientSock = null;
	private CallBack listener = null;
	private Socket clientList =null;
	@Override
	public void initialize() {
		
	}

	@Override
	public void run() {
		
		//SocketServerClient serverSock = new SocketServerClient("172.31.1.19",30003);
		SocketServerClient serverSock = new SocketServerClient(30003);
	String str=	serverSock.SendRecvContent("1\r\n");
	    System.out.println("recv string is "+str);
		
		
	}
}