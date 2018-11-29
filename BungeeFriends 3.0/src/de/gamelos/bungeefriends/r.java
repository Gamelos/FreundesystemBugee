package de.gamelos.bungeefriends;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class r extends Command{

	public r(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
			if(Main.remessage.containsKey(sender)){
			String name = Main.remessage.get(sender);
			String msg = "";
			
			for(int i = 0;i<args.length;i++){
				if(i > 0){
					msg = msg + " " + args[i];
				}else{
					msg = args[i];
				}
			}
			ProxiedPlayer p = (ProxiedPlayer) sender;
			
			if(BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(SpielerUUID.getUUIDaboutid(name))) != null){
				if(SQLFriends.friendlist(SpielerUUID.getUUIDaboutid(name)).contains(p.getUniqueId().toString())){
					if((!Settings.playerExists(SpielerUUID.getUUIDaboutid(name))||Settings.getmsg(SpielerUUID.getUUIDaboutid(name)))){
			sender.sendMessage(Main.Prefix+MySQLRang.getchatprefix(p.getUniqueId().toString())+sender.getName()+" §7-> "+MySQLRang.getchatprefix(SpielerUUID.getUUIDaboutid(name))+name +ChatColor.DARK_GRAY+":§6 "+ msg);
			BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(SpielerUUID.getUUIDaboutid(name))).sendMessage(Main.Prefix+MySQLRang.getchatprefix(p.getUniqueId().toString())+sender.getName()+" §7-> "+MySQLRang.getchatprefix(SpielerUUID.getUUIDaboutid(name))+name  +ChatColor.DARK_GRAY+":§6 "+ msg);
			if(Main.remessage.containsKey(BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(SpielerUUID.getUUIDaboutid(name))))){
				Main.remessage.remove(BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(SpielerUUID.getUUIDaboutid(name))));
			Main.remessage.put(BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(SpielerUUID.getUUIDaboutid(name))), sender.getName());
			
			}else{
				Main.remessage.put(BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(SpielerUUID.getUUIDaboutid(name))), sender.getName());
			}
					}else{
						sender.sendMessage(Main.Prefix+ChatColor.RED+"Dieser Spieler nimmt keine Privaten Nachrichten an");
					}
				}else{
					sender.sendMessage(Main.Prefix+ChatColor.RED+"Du bist mit disem Spieler nicht befreundet");
				}
			}else{
				sender.sendMessage(Main.Prefix+ChatColor.RED+"Der Spieler ist nicht Online");
			}
			}else{
				sender.sendMessage(Main.Prefix+ChatColor.RED+"Es gibt keinen Spieler den du Antworten kannst");
			}
		
	}

}
