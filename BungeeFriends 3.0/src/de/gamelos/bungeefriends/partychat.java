package de.gamelos.bungeefriends;
import java.util.List;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class partychat extends Command {

	public partychat(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		ProxiedPlayer p = (ProxiedPlayer) sender;
		ServerInfo info = ((ProxiedPlayer)sender).getServer().getInfo();
		if(!info.getName().contains("CityBuild")){
		if(args.length > 0){
			if(PartyID.playerExists(p.getUniqueId().toString())){
			String msg = "";
			
			for(int i = 0;i<args.length;i++){
				if(i > 0){
					msg = msg + " " + args[i];
				}else{
					msg = args[i];
				}
			}
			List<String> list = SQLParty.StringToList(SQLParty.getPartylist(PartyID.getID(p.getUniqueId().toString())));
			for(String name : list) {
				if(BungeeCord.getInstance().getPlayer(name)!=null) {
					ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(name);
					pp.sendMessage(Main.PartyPrefix+MySQLRang.getchatprefix(p.getUniqueId().toString())+p.getName()+ChatColor.GRAY+" >> "+msg);
				}
			}
			}else{
				sender.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du bist in keiner Party");
			}
		}else{
			sender.sendMessage(Main.PartyPrefix+ChatColor.RED+"Nutze /p <Nachricht>");
		}
		}else{
String msg = "";
			
			for(int i = 0;i<args.length;i++){
				if(i > 0){
					msg = msg + " " + args[i];
				}else{
					msg = args[i];
				}
			}
			Main.sendtoserver("data", "plot/"+sender.getName()+"/"+msg, p.getServer().getInfo());
		}
	}

}
