package me.zhanshi123.bungeeannouncer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.md_5.bungee.config.Configuration;

public class ConfigManager {
	Configuration config=null;
	public ConfigManager(Configuration config)
	{
		this.config=config;
		cm=this;
	}
	private static ConfigManager cm=null;
	public static ConfigManager getInstance()
	{
		return cm;
	}
	public void setConfig(Configuration config)
	{
		this.config=config;
		cm=this;
	}
	public List<Announce> getAnnounces()
	{
		List<Announce> as=new ArrayList<Announce>();
		Configuration ac=config.getSection("Messages");
		Collection<String> messages=ac.getKeys();
		for(String s:messages)
		{
			List<String> message=config.getStringList("Messages."+s);
			as.add(new Announce(s,message));
		}
		return as;
	}
	public int getTimer()
	{
		return config.getInt("timer");
	}
	public List<String> getOrder()
	{
		return config.getStringList("Order");
	}
}
