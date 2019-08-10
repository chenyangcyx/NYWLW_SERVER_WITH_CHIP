package Handle;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import Global.DataStruct;
import Global.Utils;

public class ReceiveMessage extends Thread
{
	public Socket so=null;
	long thread_creating_time=0;
	
	public ReceiveMessage(Socket so)
	{
		this.so=so;
		//设置该线程创建的时间
		RefreshTime();
	}
	
	//获取该线程时间
	public long GetThreadTime()
	{
		return this.thread_creating_time;
	}
	
	//刷新该线程的时间
	public void RefreshTime()
	{
		this.thread_creating_time=Utils.utils.GetTimeByLong();
	}
	
	@SuppressWarnings("deprecation")
	public void run()
	{
		String str=null;
		BufferedReader re;
		DataStruct ds=new DataStruct(0,0,0);
		try
		{
			re = new BufferedReader(new InputStreamReader(so.getInputStream(),"UTF-8"));
			while((str=re.readLine())!=null)
			{
				//记录该消息
				Utils.utils.WriteOriginMessage(str);
				//解析消息，存入数据库
				if(Utils.utils.AnalyzeMessage(str, ds))
					//将设置好的消息写入数据库
					Utils.utils.WriteDataMessage(ds);
				//消息为无用消息
				else
				{
					//发出通知
					Utils.utils.SendSystemMessage("消息为无用消息，立即关闭接收与发送线程！剩余"+(Utils.utils.all_receive_thread.size()-1)+"个线程！");
					
					//关闭发送线程的Socket
					Utils.utils.all_send_thread.get(Utils.utils.all_receive_thread.indexOf(this)).so.close();
					//从List中移除发送线程
					Utils.utils.all_send_thread.remove(Utils.utils.all_receive_thread.indexOf(this));
					//关闭发送线程
					Utils.utils.all_send_thread.get(Utils.utils.all_receive_thread.indexOf(this)).stop();
					
					//关闭本线程的Socket
					this.so.close();
					//从List中移除接收线程
					Utils.utils.all_receive_thread.remove(this);
					//关闭本线程
					this.stop();
				}
				//刷新当前线程的记录时间
				RefreshTime();
			}
		} catch (Exception e)
		{
			Utils.utils.HandleException(e);
		}
	}
}
