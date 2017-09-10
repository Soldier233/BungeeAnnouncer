package me.zhanshi123.bungeeannouncer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.zhanshi123.bungeeannouncer.metrics.Metrics;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Main extends Plugin
{
	int current=0;
	public static List<Announce> announces=null;
	List<String> order=null;
	int timer=120;
	
	private static Main instance=null;
	
	public static Main getInstance()
	{
		return instance;
	}
	
	public void cancelTasks()
	{
		getProxy().getScheduler().cancel(instance);
	}
	
	@Override
	public void onEnable()
	{
		getLogger().info("��aBC�������ڼ�����");
		loadConfig();
		readConfig();
		registerTasks();
		instance=this;
		new Metrics(this);
		getProxy().getPluginManager().registerCommand(this, new Commands());
		getLogger().info("��a�����ˡ�c"+announces.size()+"��a����Ϣ�������ˡ�c"+order.size()+"��a����Ϣ");
		getLogger().info("��aBC�����Ѽ������!");
	}
	
	public void readConfig()
	{
		announces=ConfigManager.getInstance().getAnnounces();
		order=ConfigManager.getInstance().getOrder();
		timer=ConfigManager.getInstance().getTimer();
		current=0;
	}
	
	public void reloadConfig()
	{
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Configuration config=null;
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new InputStreamReader(new FileInputStream(file),"UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ConfigManager.getInstance().setConfig(config);
	}
	
	public void loadConfig()
	{
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Configuration config=null;
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new InputStreamReader(new FileInputStream(file),"UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		new ConfigManager(config);
	}
	
	public void registerTasks()
	{
		getProxy().getScheduler().schedule(this, new Runnable()
		{
			public void run()
			{
				String name=order.get(current);
				Announce a=Announce.valueOf(name);
				if(a==null)
				{
					getLogger().info("Order�з���һ���޷�ƥ�����Ŀ����Ϊ "+name+" ��������Ŀ��");
				}
				else
				{
					for(String s:a.getMessages())
					{
						ComponentBuilder builder = new ComponentBuilder("");
						builder.append(ChatColor.translateAlternateColorCodes('&', s));
						getProxy().broadcast(builder.create());
					}
				}
				current++;
				if(current==order.size())
				{
					current=0;
				}
			}
		},timer, timer,TimeUnit.SECONDS);
	}
}
