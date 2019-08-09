package Global;

public class MySqlPara
{
	public static MySqlPara global_mp=new MySqlPara();
	
	public final String SQLAddress="jdbc:mysql://localhost:3306/nywlw?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
	public final String SQLAccount="nywlw";
	public final String SQLPassword="nywlwnywlwnywlwnywlw";
	
	public final String OriginalMessage_TableName="originmessage";
	public final String DataMessage_TableName="datamessage";
	public final String ControlMessage_TableName="controlmessage";
}
