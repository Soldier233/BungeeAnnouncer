package me.zhanshi123.bungeeannouncer;

import java.util.List;

import net.md_5.bungee.api.ProxyServer;

public class Announce {
	List<String> messages=null;
	String name=null;
	public Announce(String name,List<String> messages)
	{
		this.name=name;
		this.messages=messages;
	}
	public List<String> getMessages()
	{
		return messages;
	}
	public String getName()
	{
		return name;
	}
	public static Announce valueOf(String s)
	{
		Announce ann=null;
		for(Announce a:Main.announces)
		{
			if(a.getName().equalsIgnoreCase(s))
			{
				ann=a;
				break;
			}
		}
		
		return ann;
	}
}
