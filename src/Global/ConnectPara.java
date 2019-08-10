package Global;

public class ConnectPara
{
	public static ConnectPara global_cp=new ConnectPara();
	
	//本服务器端口
	public final int server_port=5000;
	//查询要发送的信息的时间间隔
	public final int check_interval=200;
	//接收线程超时的时间
	public final long thread_timeout_time=5*60*1000;
	
	//消息重发次数
	public final int message_resend_time=5;
}