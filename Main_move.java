package application;


import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import javax.inject.Inject;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.conditionModel.ICondition;
import com.kuka.roboticsAPI.conditionModel.JointTorqueCondition;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.JointEnum;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.ObjectFrame;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.geometricModel.World;
import com.kuka.roboticsAPI.geometricModel.math.Transformation;
import com.kuka.roboticsAPI.motionModel.IMotion;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.MotionBatch;
import com.kuka.roboticsAPI.motionModel.controlModeModel.PositionControlMode;

import application.SocketServerClient;

import com.kuka.common.ThreadUtil;
import com.kuka.generated.ioAccess.*;
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
public class Main_move extends RoboticsAPIApplication {
	@Inject
	private LBR lbr7;
    private Controller controler;
    //@Inject
   // private Tool grip; 
    //@Inject
    //private ObjectFrame tcp;
   @Inject
    private FestoIOGroup festo;
   
    private String str=null;
    private String str_recv=null;
    private int port=30003;
    private int count_x=0;
    private int count_8=0;
    private int var=0;
    private double x_iphx;
    private double y_iphx;
    private double z_iphx;
    private double x_iph8;
    private double y_iph8;
    private double z_iph8;
    private ICondition condition;
    private IMotionContainer motion;
	private String c;

    //private ArrayList<SocketServerClient> arraysocket=new ArrayList<SocketServerClient>();
	@Override
	public void initialize() {
		//controler=(Controller)getContext().getController("KUKA_Sunrise_Cabinet1");
		//grip=createFromTemplate("Griper");
		//grip.attachTo(lbr7.getFlange());
		//tcp=grip.getFrame("/tcp");
		
		controler=getController("KUKA_Sunrise_Cabinet_1");
		JointTorqueCondition j1 = new JointTorqueCondition(JointEnum.J1, -4, 4);
		JointTorqueCondition j2 = new JointTorqueCondition(JointEnum.J2, -4, 4);
		JointTorqueCondition j3 = new JointTorqueCondition( JointEnum.J3, -4, 4);
		JointTorqueCondition j4 = new JointTorqueCondition( JointEnum.J4, -4, 4);
		JointTorqueCondition j5 = new JointTorqueCondition( JointEnum.J5, -4, 4);
		JointTorqueCondition j6 = new JointTorqueCondition(JointEnum.J6, -4, 4);
		JointTorqueCondition j7 = new JointTorqueCondition( JointEnum.J7, -4, 4);
		
		condition = j1.or(j2, j3, j4, j5, j6, j7);
		/****caculate the tray
		 * *****/
		festo.setOutput1(false);

	}

	@Override
	public void run() {
		IMotionContainer mb;
		Frame ori_8=getApplicationData().getFrame("/take_iphone/take_8").copyWithRedundancy();
		Frame ori_x=getApplicationData().getFrame("/take_iphone/take_x").copyWithRedundancy();
		Frame ori_finished=getApplicationData().getFrame("/finished_pos").copyWithRedundancy();

		lbr7.move(ptp(getApplicationData().getFrame("/start")).setJointVelocityRel(.5));
		while(true) 
		{
		   
			
		   {
			    System.out.println("process start,listen host commond... ");
		        SocketServerClient socket_server=new SocketServerClient(30003);
			    str_recv=socket_server.SendRecvContent("1");
			    System.gc();
		     }
	  
	           str_recv=str_recv.trim();
			if(str_recv.matches("1"))
				var=1;
			else if(str_recv.matches("2"))
				var=2;
			
			switch(var)
			{
			case 1:
				count_8+=1;
				
				/****走到预取料位置***/
				ori_8.setX(ori_8.getX()-(count_8-1)*86);
				ori_8.setY(ori_8.getY());
				ori_8.setZ(ori_8.getZ()+70);
				lbr7.moveAsync(lin(ori_8).setCartVelocity(100));
				
				/****走到取料位置****/
				//lbr7.move(lin(getApplicationData().getFrame("/take_8")).setCartVelocity(90));
				ori_8.setZ(ori_8.getZ()-70);
				lbr7.move(lin(ori_8).setCartVelocity(100));
				/***吸嘴打开，取料***/
				festo.setOutput1(true);
				ThreadUtil.milliSleep(100);
				/****走到预取料位置****/
				ori_8.setZ(ori_8.getZ()+70);
				lbr7.moveAsync(lin(ori_8).setCartVelocity(100));
				ori_8.setZ(ori_8.getZ()-70);
				ori_8.setX(ori_8.getX()+(count_8-1)*86);
				if(count_8==4)
					count_8=0;
				/*****过渡点*****/
				lbr7.moveAsync(lin(getApplicationData().getFrame("/pre_trans")).setCartVelocity(100));
				/*******走到预放料位置****/
				lbr7.moveAsync(lin(getApplicationData().getFrame("/pre_8")).setCartVelocity(100));
				/******走到放料位置****/
				lbr7.move(lin(getApplicationData().getFrame("/put_8")).setCartVelocity(100));
				lbr7.move(lin(getApplicationData().getFrame("/adjust")).setCartVelocity(100));
				
				/******放料****/
				festo.setOutput1(false);
				
				ThreadUtil.milliSleep(1500);
				lbr7.moveAsync(lin(getApplicationData().getFrame("/pre_8")).setCartVelocity(100));
				lbr7.move(lin(getApplicationData().getFrame("/stand_pos")).setCartVelocity(100)); 
				/****放料,机器人发送放料成功****/
				
				{
				    System.out.println("puting product is ok");
					SocketServerClient socket_client_1=new SocketServerClient("172.31.1.20",30004);
					str_recv=socket_client_1.SendRecvContent("1");
					ThreadUtil.milliSleep(1000);
					System.gc();
				}
				/****等待，机器人端打开，监听  APP发送过来的取走指令****/
				{
					System.out.println("listen host command...");
					SocketServerClient socket_server_1=new SocketServerClient(30003);
					str_recv=socket_server_1.SendRecvContent("1");
					System.gc();
				}
			    	str_recv=str_recv.trim();
				if(str_recv.matches("9"))
				{
					/******走到预取料位置*****/
					
					lbr7.moveAsync(lin(getApplicationData().getFrame("/pre_8")).setCartVelocity(100));
					/*****走到取料位置*****/
					lbr7.move(lin(getApplicationData().getFrame("/post_take8")).setCartVelocity(100));
					/*********取料******/
					festo.setOutput1(true);
					ThreadUtil.milliSleep(100);
					lbr7.moveAsync(lin(getApplicationData().getFrame("/pre_8")).setCartVelocity(100));
					lbr7.moveAsync(lin(getApplicationData().getFrame("/post_trans")).setCartVelocity(100));
					lbr7.move(lin(getApplicationData().getFrame("/finished_pos")).setCartVelocity(100));
					 mb=lbr7.move(positionHold(new PositionControlMode(),-1,null).breakWhen(condition));
					if(mb.hasFired(condition))
					{
     					festo.setOutput1(false);
					}
					ThreadUtil.milliSleep(1500);
					ori_finished.setX(ori_finished.getX());
					ori_finished.setY(ori_finished.getY());
					ori_finished.setZ(ori_finished.getZ()+70);
					lbr7.moveAsync(lin(ori_finished).setCartVelocity(100));
					ori_finished.setZ(ori_finished.getZ()-70);
					
						
						lbr7.move(ptp(getApplicationData().getFrame("/start")).setJointVelocityRel(.3));
						{
						SocketServerClient socket_client=new SocketServerClient("172.31.1.20",30004);
						str_recv=socket_client.SendRecvContent("1");
						}
						System.out.println("process done");
				     	ThreadUtil.milliSleep(10);
					
				}
				
				    break;
				
			case 2:
                count_x+=1;
				/****走到预取料位置***/
				ori_x.setX(ori_x.getX()-(count_x-1)*86);
				ori_x.setY(ori_x.getY());
				ori_x.setZ(ori_x.getZ()+70);
				lbr7.moveAsync(lin(ori_x).setCartVelocity(100));
				
				/****走到取料位置****/
				//lbr7.move(lin(getApplicationData().getFrame("/take_x")).setCartVelocity(90));
				ori_x.setZ(ori_x.getZ()-70);
				lbr7.move(lin(ori_x).setCartVelocity(100));
				
				/***吸嘴打开，取料***/
				festo.setOutput1(true);
				ThreadUtil.milliSleep(100);
				/****走到预取料位置****/
				ori_x.setZ(ori_x.getZ()+70);
				lbr7.moveAsync(lin(ori_x).setCartVelocity(100));
				ori_x.setZ(ori_x.getZ()-70);
				ori_x.setX(ori_x.getX()+(count_x-1)*86);

				if(count_x==4)
				     count_x=0;	
				/*****过渡点*****/
				lbr7.moveAsync(lin(getApplicationData().getFrame("/pre_trans")).setCartVelocity(100));
				/*******走到预放料位置****/
				lbr7.moveAsync(lin(getApplicationData().getFrame("/pre_x")).setCartVelocity(100));
				/******走到放料位置****/
				lbr7.move(lin(getApplicationData().getFrame("/put_x")).setCartVelocity(100));
				//lbr7.move(lin(getApplicationData().getFrame("/adjust")).setCartVelocity(100));
				
				/******放料****/
				festo.setOutput1(false);
				
				ThreadUtil.milliSleep(1500);
				lbr7.moveAsync(lin(getApplicationData().getFrame("/pre_x")).setCartVelocity(100));
				lbr7.move(lin(getApplicationData().getFrame("/stand_pos")).setCartVelocity(100));
 
				/****放料,机器人发送放料成功****/
				
				{
				    System.out.println("puting product is ok");
					SocketServerClient socket_client_1=new SocketServerClient("172.31.1.20",30004);
					str_recv=socket_client_1.SendRecvContent("1");
					ThreadUtil.milliSleep(1000);
					System.gc();
				}
				/****等待，机器人端打开，监听  APP发送过来的取走指令****/
				{
					System.out.println("listen host command...");
					SocketServerClient socket_server_1=new SocketServerClient(30003);
					str_recv=socket_server_1.SendRecvContent("1");
					System.gc();
				}
				str_recv=str_recv.trim();
				if(str_recv.matches("9"))
				{
					/******走到预取料位置*****/
					
					lbr7.moveAsync(lin(getApplicationData().getFrame("/pre_x")).setCartVelocity(100));
					/*****走到取料位置*****/
					lbr7.move(lin(getApplicationData().getFrame("/post_takex")).setCartVelocity(100));
					/*********取料******/
					festo.setOutput1(true);
					ThreadUtil.milliSleep(100);
					lbr7.moveAsync(lin(getApplicationData().getFrame("/pre_x")).setCartVelocity(100));
					lbr7.moveAsync(lin(getApplicationData().getFrame("/post_trans")).setCartVelocity(100));
					lbr7.move(lin(getApplicationData().getFrame("/finished_pos")).setCartVelocity(100));
					//System.out.println("hahhahhaha");
					 mb=lbr7.move(positionHold(new PositionControlMode(),-1,null).breakWhen(condition));
						if(mb.hasFired(condition))
						{
							festo.setOutput1(false);
						}
						ThreadUtil.milliSleep(1500);
						ori_finished.setX(ori_finished.getX());
						ori_finished.setY(ori_finished.getY());
						ori_finished.setZ(ori_finished.getZ()+70);
						lbr7.moveAsync(lin(ori_finished).setCartVelocity(100));
						ori_finished.setZ(ori_finished.getZ()-70);
						
							
						lbr7.move(ptp(getApplicationData().getFrame("/start")).setJointVelocityRel(.3));
							{
						SocketServerClient socket_client=new SocketServerClient("172.31.1.20",30004);
						str_recv=socket_client.SendRecvContent("1");
						System.out.println("process done");
					    ThreadUtil.milliSleep(10);
							}
				}
				
				    break;
			default:
				
			//case 3:
				
			}
			
			
		}
		
		
		}
	
	@Override
	public void dispose()
	{
	 super.dispose();
	}
	
		// your application execution starts here
		//lBR_iiwa_7_R800_1.move(ptpHome());
	}
