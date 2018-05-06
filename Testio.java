package application;


import javax.inject.Inject;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.LBR;
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
public class Testio extends RoboticsAPIApplication {
	@Inject
	private LBR lBR_iiwa_7_R800_1;
    @Inject
    private Controller control;
    @Inject
    private FestoIOGroup festo;
	@Override
	public void initialize() {
		
		control=getController("KUKA_Sunrise_Cabinet_1");
		
	}

	@Override
	public void run() {
		
		
		festo.setOutput1(true);
		ThreadUtil.milliSleep(1000);
		festo.setOutput1(false);
		
		
		
		// your application execution starts here
	//	lBR_iiwa_7_R800_1.move(ptpHome());
	}
}