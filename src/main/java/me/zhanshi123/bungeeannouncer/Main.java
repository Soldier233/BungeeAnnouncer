package me.zhanshi123.bungeeannouncer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.zhanshi123.bungeeannouncer.metrics.Metrics;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Main extends Plugin {
    int current = 0;
    public static List<Announce> announces = null;
    List<String> order = null;
    int timer = 120;
    //Pattern pattern = Pattern.compile("^([hH][tT]{2}[pP]:\\/\\/|[hH][tT]{2}[pP][sS]:\\/\\/)(([A-Za-z0-9-~]+)\\.)+([A-Za-z0-9-~\\/])+$");
    Pattern pattern = Pattern.compile(".([hH][tT]{2}[pP]:\\/\\/|[hH][tT]{2}[pP][sS]:\\/\\/)(([A-Za-z0-9-~]+)\\.)+([A-Za-z0-9-~\\/])+.");
    private static Main instance = null;

    public static Main getInstance() {
        return instance;
    }

    public void cancelTasks() {
        getProxy().getScheduler().cancel(instance);
    }

    @Override
    public void onEnable() {
        getLogger().info("§aBC公告正在加载中");
        loadConfig();
        readConfig();
        registerTasks();
        instance = this;
        new Metrics(this);
        getProxy().getPluginManager().registerCommand(this, new Commands());
        getLogger().info("§a加载了§c" + announces.size() + "§a个消息，启用了§c" + order.size() + "§a个消息");
        getLogger().info("§aBC公告已加载完成!");
    }

    public void sendAnnounce(Announce announce) {
        for (String s : announce.getMessages()) {
            Matcher matcher = pattern.matcher(s);
            String website;
            ComponentBuilder builder = new ComponentBuilder("");
            if (matcher.find()) {
                website = matcher.group();
                website=website.substring(1,website.length()-1);
                TextComponent web = new TextComponent(website);
                web.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, website));
                String[] parts = s.split(website);
                if(parts.length!=0){
                    builder.append(ChatColor.translateAlternateColorCodes('&', parts[0]));
                }
                builder.append(web);
                if(parts.length!=0){
                    builder.append(ChatColor.translateAlternateColorCodes('&', parts[1]));
                }
            } else {
                builder.append(ChatColor.translateAlternateColorCodes('&', s));
            }
            getProxy().broadcast(builder.create());
        }
    }

    public void readConfig() {
        announces = ConfigManager.getInstance().getAnnounces();
        order = ConfigManager.getInstance().getOrder();
        timer = ConfigManager.getInstance().getTimer();
        current = 0;
    }

    public void reloadConfig() {
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
        Configuration config = null;
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ConfigManager.getInstance().setConfig(config);
    }

    public void loadConfig() {
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
        Configuration config = null;
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new ConfigManager(config);
    }

    public void registerTasks() {
        getProxy().getScheduler().schedule(this, new Runnable() {
            public void run() {
                String name = order.get(current);
                Announce a = Announce.valueOf(name);
                if (a == null) {
                    getLogger().info("Order中发现一个无法匹配的项目！名为 " + name + " 跳过该项目！");
                } else {
                    sendAnnounce(a);
                }
                current++;
                if (current == order.size()) {
                    current = 0;
                }
            }
        }, timer, timer, TimeUnit.SECONDS);
    }
}