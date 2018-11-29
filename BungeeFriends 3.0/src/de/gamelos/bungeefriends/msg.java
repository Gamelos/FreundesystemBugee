package de.gamelos.bungeefriends;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class msg extends Command{

	public msg(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length <= 1){
			sender.sendMessage(Main.Prefix+ChatColor.RED+"Nutze /msg <Spieler> <Nachricht>");
			return;
		}	
			
			String name = args[0];
			String msg = "";
			
			for(int i = 1;i<args.length;i++){
				if(i > 1){
					msg = msg + " " + args[i];
				}else{
					msg = args[i];
				}
			}
			ProxiedPlayer p = (ProxiedPlayer) sender;
			
			if(BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(SpielerUUID.getUUIDaboutid(name))) == null){
				sender.sendMessage(Main.Prefix+ChatColor.RED+"Der Spieler ist nicht Online");
				return;
			}
			if(!SQLFriends.friendlist(SpielerUUID.getUUIDaboutid(name)).contains(p.getUniqueId().toString())){
				sender.sendMessage(Main.Prefix+ChatColor.RED+"Du bist mit disem Spieler nicht befreundet");
				return;
			}
			
			String uuid = SpielerUUID.getUUIDaboutid(name.toUpperCase());
			
			if(Settings.playerExists(uuid)) {
				if(Settings.getmsg(uuid)==false) {
					sender.sendMessage(Main.Prefix+ChatColor.RED+"Dieser Spieler nimmt keine Privaten Nachrichten an");
					return;
				}
			}
			
			
			sender.sendMessage(Main.Prefix+MySQLRang.getchatprefix(p.getUniqueId().toString())+p.getName()+" §7-> "+MySQLRang.getchatprefix(SpielerUUID.getUUIDaboutid(name))+SpielerUUID.getSpielername(SpielerUUID.getUUIDaboutid(name)) +ChatColor.DARK_GRAY+":§6 "+ msg);
			BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(SpielerUUID.getUUIDaboutid(name.toUpperCase()))).sendMessage(Main.Prefix+MySQLRang.getchatprefix(p.getUniqueId().toString())+sender.getName()+" §7-> "+MySQLRang.getchatprefix(SpielerUUID.getUUIDaboutid(name))+SpielerUUID.getSpielername(SpielerUUID.getUUIDaboutid(name)) +ChatColor.DARK_GRAY+":§6 "+ msg);
			
			
			if(Main.remessage.containsKey(BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(SpielerUUID.getUUIDaboutid(name))))){
				Main.remessage.remove(BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(SpielerUUID.getUUIDaboutid(name))));
			Main.remessage.put(BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(SpielerUUID.getUUIDaboutid(name))), sender.getName());
			}else{
				Main.remessage.put(BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(SpielerUUID.getUUIDaboutid(name))), sender.getName());
			}

			
		
		
	}

}
