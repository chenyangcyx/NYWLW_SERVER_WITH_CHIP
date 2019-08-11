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
				//刷新当前线程的记录时间
				RefreshTime();
			}
		} catch (Exception e)
		{
			Utils.utils.HandleException(e);
		}
	}
}
