package Connect;

import java.net.ServerSocket;
import java.net.Socket;
import Global.ConnectPara;
import Global.Utils;

public class StartSocket
{
	Distribute dis=new Distribute();
	ServerSocket ser;
	
	public StartSocket()
	{
		//开始监听本服务器的指定端口
		try
		{
			ser=new ServerSocket(ConnectPara.global_cp.server_port);
		} catch (Exception e)
		{
			Utils.utils.HandleException(e);
		}
		dis.start();
	}
	
	public void connect()
	{
		while(true)
		{
			try
			{
				//开始接收Socket
				Socket so=ser.accept();
				Utils.utils.RecordSystemMessage("接收到Socket连接，");
				dis.Handle(so);
			}
			catch(Exception e)
			{
				Utils.utils.HandleException(e);
			}
		}
	}
}
