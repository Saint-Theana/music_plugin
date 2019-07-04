package com.robot;
import java.util.*;
import com.Tick_Tock.ANDROIDQQ.sdk.*;
import java.util.regex.*;
import org.json.*;
import javax.net.ssl.*;

public class Main implements Plugin
{

	@Override
	public void onMenu(GroupMessage p1)
	{
		// TODO: Implement this method
	}

	public static void main(String[] q){
		
	}
	
	private API api;

	@Override
	public String Version()
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public String author()
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public String name()
	{
		return "音乐机器人";
	}


	
	
	
	@Override
	public void onLoad(API p1)
	{
		this.api = p1;
		try
		{
			Util.trustAllHttpsCertificates();
			HttpsURLConnection.setDefaultHostnameVerifier(Util.hv);
		}
		catch (Exception e)
		{}
	}

	@Override
	public void onMessageHandler(GroupMessage qqmessage)
	{
		MessageFactory factory = new MessageFactory();
		factory.group_uin = qqmessage.group_uin;
		factory.message_type =1;
		if (qqmessage.message.matches("[酷狗网易qQ我]*点歌 .*"))
		{
			
			String[] command_array = qqmessage.message.replaceAll("_","+").split("\\s+");
			String result ="";
			int position = 0;
			int mode = 2;
			if(command_array.length == 2){
				
			}else if (command_array.length == 3){
				position=Integer.parseInt(command_array[2]);
				mode=1;
			}else{
				factory.message="命令不规范";
				this.api.SendGroupMessage(factory);
				return;
			}
			switch(command_array[0].toLowerCase()){
				case("酷狗点歌"):{
					    result = MusicFactory.Kugou_music(command_array[1],position,mode);
					}
					break;
				case("网易点歌"):{
						result = MusicFactory.Netease_music(command_array[1],position,mode);
					}
					break;
				case("酷我点歌"):{
						result = MusicFactory.Kuwo_music(command_array[1],position,mode);
					}
					break;
				case("qq点歌"):{
						result = MusicFactory.Qq_music(command_array[1],position,mode);
					}
					break;
				default:{
						result = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"1\" templateID=\"1\" action=\"web\" brief=\"搜索失败\" sourceMsgId=\"0\" url=\"\" flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item bg=\"#00BFFF\" layout=\"4\"><title color=\"#FFFFFF\" size=\"28\">平台错误</title></item></msg>";
				}
				
				
			}
			result=result.replaceAll("&", "&amp;").replace("@@@", "&");
			factory.message=result;
			this.api.SendGroupMessage(factory);
		}
		
	}
	
	@Override
	public void onMessageHandler(FriendMessage qqmessage)
	{
		
	}

	@Override
	public void onMessageHandler(TempolaryMessage qqmessage)
	{
		
		
		
		
	}

	@Override
	public void onMessageHandler(RequestMessage qqmessage)
	{
		
		
	}

	
	
	
	
	
	
}
