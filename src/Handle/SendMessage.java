package Handle;

import java.io.PrintStream;
import java.net.Socket;
import Global.ConnectPara;
import Global.Utils;

public class SendMessage extends Thread
{
	public Socket so=null;
	PrintStream wri=null;
	
	public SendMessage(Socket so)
	{
		this.so=so;
		try {
			wri=new PrintStream(new PrintStream(so.getOutputStream()));
		} catch (Exception e) {
			Utils.utils.HandleException(e);
		}
	}
	
	public void run()
	{
		String str=null;
		try
		{
			while(true)
			{
				sleep(ConnectPara.global_cp.check_interval);
				str=Utils.utils.SearchNeedInstruction();
				if(str!=null)
				{
					//消息多次发送以防丢失
					for(int i=0;i<ConnectPara.global_cp.message_resend_time;i++)
					{
						wri.println(str);
						wri.flush();
					}
					Utils.utils.RecordSystemMessage("发送消息："+str);
					Utils.utils.SendSystemMessage();
				}
			}
		} catch (Exception e) {
			Utils.utils.HandleException(e);
		}
	}
	
	//回收该线程所占用资源
	@SuppressWarnings("deprecation")
	public void RecycleThisThread()
	{
		try {
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
