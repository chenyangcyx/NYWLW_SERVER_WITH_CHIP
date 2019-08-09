package Global;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class Utils
{
	//全局静态类的静态成员
	public static Utils utils=new Utils();
	
	Connection conn = null;
	
	//默认构造函数
	Utils()
	{
		try
	    {
			// 打开链接
			SendSystemMessage("开始连接数据库");
	    	// 注册 JDBC 驱动
		    Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(MySqlPara.global_mp.SQLAddress,MySqlPara.global_mp.SQLAccount,MySqlPara.global_mp.SQLPassword);
			if(!conn.isClosed())
				SendSystemMessage("连接数据库成功！");
			else
				SendSystemMessage("连接数据库失败！");
		}
	    catch (Exception e)
	    {
	    	HandleException(e);
		}
	}
	
	//获取当前系统时间
	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public String GetCurrentTime()
	{
        return df.format(GetTimeByLong());
	}
	
	//获取Long类型的时间，距离1970年1月1日起的毫秒数
	public long GetTimeByLong()
	{
		return System.currentTimeMillis();
	}
	
	//写入originmessage
	public void WriteOriginMessage(String mess)
	{
		PreparedStatement pre=null;
		try{
			pre=conn.prepareStatement("insert into "+MySqlPara.global_mp.OriginalMessage_TableName+" values(?,?)");
			pre.setString(1, GetCurrentTime());
			pre.setString(2, mess);
			pre.executeUpdate();
			SendSystemMessage("原始消息："+mess);
		} catch (Exception e) {
			Utils.utils.HandleException(e);
		}
	}
	
	//写入datamessage
	public void WriteDataMessage(DataStruct ds)
	{
		PreparedStatement pre=null;
		try{
			pre=conn.prepareStatement("insert into "+MySqlPara.global_mp.DataMessage_TableName+" values(?,?,?,?)");
			pre.setString(1, GetCurrentTime());
			pre.setString(2, String.valueOf(ds.getWendu()));
			pre.setString(3, String.valueOf(ds.getShidu()));
			pre.setString(4, String.valueOf(ds.getGuangzhao()));
			pre.executeUpdate();
			SendSystemMessage("写入数据库"+MySqlPara.global_mp.DataMessage_TableName+"：温度："+ds.getWendu()+"，湿度："+ds.getShidu()+"，光照强度："+ds.getGuangzhao());
		} catch (Exception e) {
			Utils.utils.HandleException(e);
		}
	}
	
	//查询需要发送的指令
	public String SearchNeedInstruction()
	{
		String return_str=null;
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet res1=stmt.executeQuery("select * from "+MySqlPara.global_mp.ControlMessage_TableName+" where ifsend=0 order by time desc");
			res1.last();
			if(res1.getRow()>0)
			{
				//获取最新的控制信息
				res1.first();
				return_str=res1.getString("message");
				//刷新该表
				stmt.execute("update "+MySqlPara.global_mp.ControlMessage_TableName+" set ifsend=1");
			}
		} catch (Exception e) {
			Utils.utils.HandleException(e);
		}
		return return_str;
	}
	
	//解析从单片机发来的字符串信息
	public boolean AnalyzeMessage(String str,DataStruct ds)
	{
		if(!str.contains("temperature:"))
			return false;
		int wendu=0,shidu=0,guangzhao=0;
		wendu=Integer.parseInt(str.substring(str.indexOf("temperature:")+"temperature:".length(), str.indexOf(",humidity:")));
		shidu=Integer.parseInt(str.substring(str.indexOf("humidity:")+"humidity:".length(), str.indexOf(",illumination:")));
		guangzhao=Integer.parseInt(str.substring(str.indexOf("illumination:")+"illumination:".length(), str.indexOf("}")));
		ds.setWendu(wendu);
		ds.setShidu(shidu);
		ds.setGuangzhao(guangzhao);
		SendSystemMessage("消息解析完成！");
		return true;
	}
	
	//发送系统消息
	public void SendSystemMessage(String str)
	{
		System.out.println(GetCurrentTime());
		System.out.println(str);
		System.out.println();
	}
	
	//统一异常处理
	public void HandleException(Exception e)
	{
		SendSystemMessage("捕获异常："+e.toString().substring(0, e.toString().indexOf(":")));
	}
}