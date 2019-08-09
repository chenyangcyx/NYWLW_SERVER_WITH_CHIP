import Connect.StartSocket;
import Global.ConnectPara;
import Global.Utils;

public class Main {
	public static void main(String[] args)
	{
		Utils.utils.SendSystemMessage("启动系统，服务器接收程序");
		StartSocket ss=new StartSocket();
		Utils.utils.SendSystemMessage("监听"+ConnectPara.global_cp.server_port+"端口");
		ss.connect();
	}
}
