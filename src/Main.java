import Connect.StartSocket;
import Global.ConnectPara;
import Global.Utils;

public class Main {
	public static void main(String[] args)
	{
		Utils.utils.RecordSystemMessage("启动系统，");
		StartSocket ss=new StartSocket();
		Utils.utils.RecordSystemMessage("监听"+ConnectPara.global_cp.server_port+"端口");
		Utils.utils.SendSystemMessage();
		ss.connect();
	}
}
