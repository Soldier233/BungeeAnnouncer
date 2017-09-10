package me.zhanshi123.bungeeannouncer;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class Commands extends Command 
{
	public Commands() 
	{
		super("ba");
	}

	public void sendHelp(CommandSender sender)
	{
		sender.sendMessage(new ComponentBuilder("��a======��6BungeeAnnouncer��a======").create());
		TextComponent reload=new TextComponent("��4 - ��b/ba reload ���ز��");
		reload.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/ba reload"));
		reload.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("��9���ҿ����Զ�������Ƶ�������Ŷ").create()));
		sender.sendMessage(reload);
		sender.sendMessage(new ComponentBuilder("��c��ʾ:���������Ϸ�У��Ϸ�������ǿ��Ե����Ŷ").create());
	}
	
	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if(sender.hasPermission("ba.admin"))
		{
			if(args.length==0)
			{
				sendHelp(sender);
				return;
			}
			if(args[0].equalsIgnoreCase("reload"))
			{
				Main.getInstance().reloadConfig();
				Main.getInstance().readConfig();
				Main.getInstance().cancelTasks();
				Main.getInstance().registerTasks();
				sender.sendMessage(new ComponentBuilder("��a���سɹ�").create());
			}
		}
		else
		{
			sender.sendMessage(new ComponentBuilder("��aȨ�޲���").create());
		}
	}
}
