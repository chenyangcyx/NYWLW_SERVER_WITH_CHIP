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
					wri.println(str);
					wri.flush();
					Utils.utils.SendSystemMessage("发送消息："+str);
				}
			}
		} catch (Exception e) {
			Utils.utils.HandleException(e);
		}
	}
	
	
}
