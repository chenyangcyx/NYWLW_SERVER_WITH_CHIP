package Connect;

import java.net.Socket;
import java.util.ArrayList;
import Global.ConnectPara;
import Global.Utils;
import Handle.ReceiveMessage;
import Handle.SendMessage;

public class Distribute extends Thread
{
	ArrayList<ReceiveMessage> all_thread1=new ArrayList<>();
	ArrayList<SendMessage> all_thread2=new ArrayList<>();
	
	//对于Socket的处理
	public void Handle(Socket so)
	{
		//开启接收消息线程
		Utils.utils.SendSystemMessage("开启消息接收线程");
		ReceiveMessage t1=new ReceiveMessage(so);
		all_thread1.add(t1);
		t1.start();
		//开启发送消息线程
		Utils.utils.SendSystemMessage("开启消息发送线程");
		SendMessage t2=new SendMessage(so);
		all_thread2.add(t2);
		t2.start();
	}
	
	@SuppressWarnings("deprecation")
	public void run()
	{
		while(true)
		{
			int i=0;
			try {
				sleep(60*1000);
			} catch (Exception e) {
				Utils.utils.HandleException(e);
			}
			for(i=0;i<all_thread1.size();i++)
			{
				ReceiveMessage t=all_thread1.get(i);
				if(Utils.utils.GetTimeByLong()-t.GetThreadTime()>=ConnectPara.global_cp.thread_timeout_time)
				{
					SendMessage t2=all_thread2.get(i);
					Utils.utils.SendSystemMessage("线程"+t.getName()+"超时，撤销该线程！剩余"+(all_thread1.size()-1)+"个线程！");
					Utils.utils.SendSystemMessage("线程"+t2.getName()+"超时，撤销该线程！剩余"+(all_thread2.size()-1)+"个线程！");
					all_thread1.remove(i);
					all_thread2.remove(i);
					try {
						t.so.close();
						t2.so.close();
						t.stop();
						t2.stop();
					} catch (Exception e) {
						Utils.utils.HandleException(e);
					}
				}
			}
		}
	}
}
