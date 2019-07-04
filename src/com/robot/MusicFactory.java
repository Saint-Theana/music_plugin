package com.robot;

import org.json.*;
import java.io.*;

public class MusicFactory
{
	public static String Kugou_music(String song, int position, int mode)
	{
		String message_to_send =null;
		String info = Util.Curl("http://songsearch.kugou.com/song_search_v2?keyword=" + song + "&page=0&pagesize=10&userid=-1&clientver=&platform=WebFilter&tag=em&filter=2&iscorrection=1&privilege_filter=0");
		try
		{
			JSONObject json = new JSONObject(info);
			JSONArray songs_list = json.getJSONObject("data").getJSONArray("lists");
			int songs_list_length = songs_list.length();
			if (mode == 1)
			{


				String File_hash = json.getJSONObject("data").getJSONArray("lists").getJSONObject(position - 1).getString("FileHash");
				JSONObject data = new JSONObject(Util.Curl("http://m.kugou.com/app/i/getSongInfo.php?hash=" + File_hash+"&cmd=playInfo"));
				//String audio_name  = data.getString("audio_name");
				//String album_name = data.getString("album_name");
				String img = data.getString("imgUrl").replaceAll("\\{size\\}","480");
				String author_name = data.getString("singerName");
				String song_name = data.getString("songName");
				String play_url = data.getString("url");
				message_to_send = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"2\" templateID=\"1\" action=\"web\" brief=\"酷狗音乐\" sourceMsgId=\"0\" url=\"\" flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item layout=\"2\"><audio cover=\"" + img + "\" src=\"" + play_url + "\" /><title>" + song_name + "</title><summary>" + author_name + "</summary></item><source name=\"酷狗音乐\" icon=\"http://url.cn/4Asex5p\" url=\"http://url.cn/SXih4O\" action=\"app\" a_actionData=\"com.kugou.android\" i_actionData=\"tencent205141://\" appid=\"205141\" /></msg>";

			}
			else if (mode == 2)
			{
				String xml="<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"1\" templateID=\"1\" action=\"web\" brief=\"点歌列表\" url=\"\" flag=\"3\"><item layout=\"5\"><picture cover=\"https://i.loli.net/2018/10/02/5bb37e1e7d09b.png\"/></item><item layout=\"6\"><summary size=\"20\" color=\"#32CD32\" style=\"1\">";
				String line ="<item><hr/></item>";
				for (int time = 0; time < songs_list_length; time++)
				{
					String File_hash = songs_list.getJSONObject(time).getString("FileHash");
					JSONObject song_detail_json = new JSONObject(Util.Curl("http://m.kugou.com/app/i/getSongInfo.php?hash=" + File_hash+"&cmd=playInfo"));
					String img = song_detail_json.getString("imgUrl").replaceAll("\\{size\\}","480");
					String author_name = song_detail_json.getString("singerName");
					String song_name = song_detail_json.getString("songName");
					xml = xml + String.valueOf(time + 1) + ":" + song_name + "   " + author_name + "@@@#10;";

				}
				message_to_send = xml + "</summary></item></msg>";

			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"1\" templateID=\"1\" action=\"web\" brief=\"搜索失败\" sourceMsgId=\"0\" url=\"\" flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item bg=\"#00BFFF\" layout=\"4\"><title color=\"#FFFFFF\" size=\"28\">"+e.toString()+"</title></item></msg>";
			
		}

		return message_to_send;
	}

	//网易点歌
	public static String Netease_music(String song, int position, int mode)
	{
		String message_to_send =null;
		String param = "hlpretag=<span class=\"s-fc2\">&hlposttag=</span>&s=" + song + "&offset=0&total=true&limit=10&type=1";
		String info = Util.post_with_data("http://music.163.com/api/search/pc", param);
		try
		{
			JSONObject json = new JSONObject(info);
			JSONArray songs_list = json.getJSONObject("result").getJSONArray("songs");
			int songs_list_length = songs_list.length();
			if (mode == 1)
			{
				JSONObject data = songs_list.getJSONObject(position - 1);
				String song_name = data.getString("name");
				String song_id = String.valueOf(data.getInt("id"));
				String author_name = data.getJSONArray("artists").getJSONObject(0).getString("name");
				String img = data.getJSONObject("album").getString("picUrl");
				//String album_name =data.getJSONObject("album").getString("name");
				String play_url = Util.get_redirected_url("http://music.163.com/song/media/outer/url?id=" + song_id + ".mp3");
				if (play_url == "http://music.163.com/404")
				{
					message_to_send = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"1\" templateID=\"1\" action=\"web\" brief=\"点歌失败\" sourceMsgId=\"0\" url=\"\" flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item bg=\"#00BFFF\" layout=\"4\"><title color=\"#FFFFFF\" size=\"28\">该歌曲无外链</title></item></msg>";
				}
				else
				{
				    message_to_send = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"2\" templateID=\"1\" action=\"web\" brief=\"网易音乐\" sourceMsgId=\"0\" url=\"\" flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item layout=\"2\"><audio cover=\"" + img + "\" src=\"" + play_url + "\" /><title>" + song_name + "</title><summary>" + author_name + "</summary></item><source name=\"网易云音乐\" icon=\"https://url.cn/5TxJvzz\" url=\"http://url.cn/5pl4kkd\" action=\"app\" a_actionData=\"com.netease.cloudmusic\" i_actionData=\"tencent100495085://\" appid=\"205141\" /></msg>";
				}
			}
			else if (mode == 2)
			{
				String xml="<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"1\" templateID=\"1\" action=\"web\" brief=\"点歌列表\" url=\"\" flag=\"3\"><item layout=\"5\"><picture cover=\"https://i.loli.net/2018/10/02/5bb37e1e7d09b.png\"/></item><item layout=\"6\"><summary size=\"20\" color=\"#32CD32\" style=\"1\">";
				String line ="<item><hr/></item>";
				for (int time = 0; time < songs_list_length; time++)
				{
					JSONObject data = songs_list.getJSONObject(time);
					String song_name = data.getString("name");
					//String song_id = data.getString("id");
					String author_name = data.getJSONArray("artists").getJSONObject(0).getString("name");
					String img = "";
					if (time  < 8){
					    img = data.getJSONObject("album").getString("picUrl");
					}
					//String album_name =data.getJSONObject("album").getString("name");
					xml = xml + String.valueOf(time + 1) + ":" + song_name + "   " + author_name +  "@@@#10;";

				}
				message_to_send = xml + "</summary></item></msg>";

			}
		}
		catch (JSONException e)
		{
			return "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"1\" templateID=\"1\" action=\"web\" brief=\"搜索失败\" sourceMsgId=\"0\" url=\"\" flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item bg=\"#00BFFF\" layout=\"4\"><title color=\"#FFFFFF\" size=\"28\">"+e.toString()+"</title></item></msg>";

		}

		return message_to_send;
	}

	public static String Kuwo_music(String song, int position, int mode)
	{
		String message_to_send =null;
		String info = Util.Curl("http://search.kuwo.cn/r.s?client=kt&all=" + song + "&pn=0&rn=10&uid=221260053&ver=kwplayer_ar_99.99.99.99&vipver=1&ft=music&cluster=0&strategy=2012&encoding=utf8&rformat=json&vermerge=1&mobi=1");
		try
		{
			JSONObject json = new JSONObject(info);
			JSONArray songs_list = json.getJSONArray("abslist");
			int songs_list_length = songs_list.length();
			if (mode == 1)
			{
				JSONObject data = songs_list.getJSONObject(position - 1);
				String song_name = data.getString("SONGNAME");
				String song_id = data.getString("MUSICRID");
				String author_name = data.getString("ARTIST");
				String img_urls=Util.Curl("http://artistpicserver.kuwo.cn/pic.web?type=big_artist_pic&pictype=url&content=list&&id=0&name=" + author_name.replaceAll("\\s+","+") + "&rid=" + song_id + "&from=pc&json=1&version=1&width=1366&height=1366");
				String img = "";
				if (img_urls.equals("NO_PIC") == false){
				    img = new JSONObject(img_urls.replaceAll("wpurl", "bkurl")).getJSONArray("array").getJSONObject(0).getString("bkurl");
				}
				//String album_name =data.getJSONObject("album").getString("name");
				String play_url = Util.Curl("http://antiserver.kuwo.cn/anti.s?type=convert_url&rid=" + song_id + "&format=mp3&response=url");
				message_to_send = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"2\" templateID=\"1\" action=\"web\" brief=\"酷我音乐\" sourceMsgId=\"0\" url=\"\" flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item layout=\"2\"><audio cover=\"" + img + "\" src=\"" + play_url + "\" /><title>" + song_name + "</title><summary>" + author_name + "</summary></item><source name=\"酷我音乐\" icon=\"http://image.uc.cn/s/wemedia/s/upload/160511165947fb959cc3345c44bf7411b686815764\" url=\"http://url.cn/QVkZGv\" action=\"app\" i_actionData=\"tencent100243533://\" appid=\"100243533\" /></msg>";	
			}
			else if (mode == 2)
			{
				String xml="<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"1\" templateID=\"1\" action=\"web\" brief=\"点歌列表\" url=\"\" flag=\"3\"><item layout=\"5\"><picture cover=\"https://i.loli.net/2018/10/02/5bb37e1e7d09b.png\"/></item><item layout=\"6\"><summary size=\"20\" color=\"#32CD32\" style=\"1\">";
				String line ="<item><hr/></item>";
				for (int time = 0; time < songs_list_length; time++)
				{
					JSONObject data = songs_list.getJSONObject(time);
					String song_name = data.getString("SONGNAME");
					String song_id = data.getString("MUSICRID");
					String author_name = data.getString("ARTIST");
					String img_urls=Util.Curl("http://artistpicserver.kuwo.cn/pic.web?type=big_artist_pic&pictype=url&content=list&&id=0&name=" + author_name + "&rid=" + song_id + "&from=pc&json=1&version=1&width=1366&height=1366");
					String img = "";
					if (!img_urls.equals("NO_PIC")){
						img = new JSONObject(img_urls.replaceAll("wpurl", "bkurl")).getJSONArray("array").getJSONObject(0).getString("bkurl");
					}
					//String album_name =data.getJSONObject("album").getString("name");
					xml = xml + String.valueOf(time + 1) + ":" + song_name + "   " + author_name +  "@@@#10;";

				}
				message_to_send = xml + "</summary></item></msg>";
			}
		}
		catch (JSONException e)
		{
			return "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"1\" templateID=\"1\" action=\"web\" brief=\"搜索失败\" sourceMsgId=\"0\" url=\"\" flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item bg=\"#00BFFF\" layout=\"4\"><title color=\"#FFFFFF\" size=\"28\">"+e.toString()+"</title></item></msg>";
		}
		return message_to_send;
	}
	
	
	
	

	public static String Qq_music(String song, int position, int mode)
	{
		String message_to_send =null;
		String info = Util.curl_with_referer("https://c.y.qq.com/soso/fcgi-bin/search_for_qq_cp?g_tk=5381&uin=0&format=jsonp&inCharset=utf-8&outCharset=utf-8&notice=0&platform=h5&needNewCode=1&w="+song+"&zhidaqu=1&catZhida=1&t=0&flag=1&ie=utf-8&sem=1&aggr=0&perpage=10&n=10&p=0&remoteplace=txt.mqq.all&_=1520833663464","https://c.y.qq.com").replaceAll("^callback[(]","").replaceAll("[)]$","");
		try
		{
			JSONArray songs_list = new JSONObject(info).getJSONObject("data").getJSONObject("song").getJSONArray("list");
			int songs_list_length = songs_list.length();
			if (mode == 1)
			{
				JSONObject data = songs_list.getJSONObject(position - 1);
				String song_name = data.getString("songname");
				String song_id = data.getString("songmid");
				String author_name = data.getJSONArray("singer").getJSONObject(0).getString("name");
				String album_id =data.getString("albummid");
				String img = "http://imgcache.qq.com/music/photo/mid_album_500/"+album_id.split("")[album_id.split("").length -2]+"/"+album_id.split("")[album_id.split("").length -1]+"/"+album_id+".jpg";
				String vkey=getvkey();
				String play_url = "http://mobileoc.music.tc.qq.com/M500"+song_id+".mp3?vkey="+vkey+"&guid=FUCK&uin=0&fromtag=8";
				message_to_send = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"2\" templateID=\"1\" action=\"web\" brief=\"QQ音乐\" sourceMsgId=\"0\" url=\"\" flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item layout=\"2\"><audio cover=\"" + img + "\" src=\"" + play_url + "\" /><title>" + song_name + "</title><summary>" + author_name + "</summary></item><source name=\"QQ音乐\" icon=\"https://url.cn/57Fdlv4\" url=\"http://url.cn/5aSZ8Gc\" action=\"app\" a_actionData=\"com.tencent.qqmusic\" i_actionData=\"tencent100497308://\" appid=\"100497308\" /></msg>";	
			}
			else if (mode == 2)
			{
				String xml="<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"1\" templateID=\"1\" action=\"web\" brief=\"点歌列表\" url=\"\" flag=\"3\"><item layout=\"5\"><picture cover=\"https://i.loli.net/2018/10/02/5bb37e1e7d09b.png\"/></item><item layout=\"6\"><summary size=\"20\" color=\"#32CD32\" style=\"1\">";
				//String line ="<item><hr/></item>";
				for (int time = 0; time < songs_list_length; time++)
				{
					JSONObject data = songs_list.getJSONObject(time);
					String song_name = data.getString("songname");
					//String song_id = data.getString("songmid");
					String author_name = data.getJSONArray("singer").getJSONObject(0).getString("name");
					//String album_id =data.getString("albummid");
					//String img = "http://imgcache.qq.com/music/photo/mid_album_500/"+album_id.split("")[album_id.split("").length -2]+"/"+album_id.split("")[album_id.split("").length -1]+"/"+album_id+".jpg";
					//String play_url = "http://ws.stream.qqmusic.qq.com/C100"+song_id+".m4a?fromtag=0&guid=126548448";
					//String album_name =data.getJSONObject("album").getString("name");
					xml = xml + String.valueOf(time + 1) + ":" + song_name + "   " + author_name +  "@@@#10;";

				}
				message_to_send = xml + "</summary></item></msg>";
			}
		}
		catch (JSONException e)
		{
			return "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><msg serviceID=\"1\" templateID=\"1\" action=\"web\" brief=\"搜索失败\" sourceMsgId=\"0\" url=\"\" flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item bg=\"#00BFFF\" layout=\"4\"><title color=\"#FFFFFF\" size=\"28\">"+e.toString()+"</title></item></msg>";
		}

		return message_to_send;
	}
	
	
	private static String getvkey()
	{
		String result = Util.curl_with_referer("https://"+Util.http_dns("c.y.qq.com")+"/base/fcgi-bin/fcg_music_express_mobile3.fcg?g_tk=556936094&loginUin=0&hostUin=0&format=json&platform=yqq&needNewCode=0&cid=205361747&uin=0&songmid=003a1tne1nSz1Y&filename=C400003a1tne1nSz1Y.m4a&guid=FUCK","https://y.qq.com/portal/profile.html");
		try
		{
			JSONObject json_root = new JSONObject(result);
			return json_root.getJSONObject("data").getJSONArray("items").getJSONObject(0).getString("vkey");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}

	}
}

