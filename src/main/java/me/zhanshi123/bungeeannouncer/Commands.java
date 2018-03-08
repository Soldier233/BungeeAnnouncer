package me.zhanshi123.bungeeannouncer;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class Commands extends Command {
    public Commands() {
        super("ba");
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage(new ComponentBuilder("§a======§6BungeeAnnouncer§a======").create());
        TextComponent reload = new TextComponent("§4 - §b/ba reload 重载插件");
        reload.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ba reload"));
        reload.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§9点我可以自动把命令复制到聊天栏哦").create()));
        TextComponent send= new TextComponent("§4 - §b/ba send <消息名> 立刻播放指定消息");
        reload.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ba send "));
        reload.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§9点我可以自动把命令复制到聊天栏哦").create()));
        sender.sendMessage(send);
        sender.sendMessage(new ComponentBuilder("§c提示:如果你在游戏中，上方的命令都是可以点击的哦").create());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("ba.admin")) {
            if (args.length == 0) {
                sendHelp(sender);
                return;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                Main.getInstance().reloadConfig();
                Main.getInstance().readConfig();
                Main.getInstance().cancelTasks();
                Main.getInstance().registerTasks();
                sender.sendMessage(new ComponentBuilder("§a重载成功").create());
            } else if(args[0].equalsIgnoreCase("send")){
                if(args.length!=2){
                    sendHelp(sender);
                    return;
                }
                Announce announce=Announce.valueOf(args[1]);
                if(announce==null){
                    sender.sendMessage(new ComponentBuilder("§a未找到该消息").create());
                    return;
                }
                Main.getInstance().sendAnnounce(announce);
                sender.sendMessage(new ComponentBuilder("§a已发送").create());
            }
        } else {
            sender.sendMessage(new ComponentBuilder("§a权限不足").create());
        }
    }
}