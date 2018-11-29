package de.gamelos.bungeefriends;
import java.sql.SQLRecoverableException;
import java.util.List;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class friend extends Command {

	public friend(String name) {
		super(name);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		ProxiedPlayer p = (ProxiedPlayer) sender;
		if(args.length >= 1){
			if(args[0].equals("add")) {
				if(args.length != 2){
					p.sendMessage(Main.Prefix+ChatColor.RED+"Nutze /friend add <Spielername>");
					return;
				}
				String spielername = args[1].toUpperCase();
				
				if(!SpielerUUID.idexists(spielername)) {
					p.sendMessage(Main.Prefix+ChatColor.RED+"Dieser Spieler war noch nie Online!");
					return;
				}
			
				
				if(p.getName().equalsIgnoreCase(args[1])) {
					p.sendMessage(Main.Prefix+ChatColor.RED+"Du darfst dir selber keine Freundschaftsanfrage senden!");
					return;
				}
				
				
				
				String uuid = SpielerUUID.getUUIDaboutid(args[1].toUpperCase());
				
				if(Settings.playerExists(uuid)) {
				if(Settings.getfriend(BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(uuid)).getUniqueId().toString())==false) {
					p.sendMessage(Main.Prefix+ChatColor.RED+"Du darfst diesem Spieler keine Freundschaftsanfrage senden!");
					return;
				}
				}
				
				if(SQLFriends.getFreundesliste(p.getUniqueId().toString()).contains(uuid)) {
					p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist mit diesem Spieler bereits befreundet!");
					return;
				}
				
				if(SQLFriends.Anfragenliste(uuid).contains(p.getUniqueId().toString())) {
					p.sendMessage(Main.Prefix+ChatColor.RED+"Du hast diesem Spieler bereits eine Anfrage gesendet!");
					return;
				}
				
				List<String> list = SQLFriends.Anfragenliste(uuid);
				list.add(p.getUniqueId().toString());
				SQLFriends.setanfragen(uuid, list);
				
				p.sendMessage(Main.Prefix+ChatColor.GREEN+"Du hast diesem Spieler erfolgreich eine Freundschaftsanfrage geschickt");
				ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(uuid));
				if(pp != null){
					pp.sendMessage(Main.Prefix+MySQLRang.getchatprefix(p.getUniqueId().toString())+p.getName()+ChatColor.GRAY+" möchte dein Freund sein!");
					
					TextComponent message = new TextComponent(Main.Prefix+"Du hast die Optionen: ");
					TextComponent accept = new TextComponent("[Akzeptieren]");
					accept.setColor(ChatColor.GREEN);
					accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept "+p.getName()));
					accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GREEN+"Nehme die Freundschaft an").create()));
					message.addExtra(accept);
					TextComponent free = new TextComponent(" ");
					message.addExtra(free);
					TextComponent deny = new TextComponent("[Ablehnen]");
					deny.setColor(ChatColor.RED);
					deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend deny "+p.getName()));
					deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED+"Lehnt die Freundschaft ab").create()));
					message.addExtra(deny);
					pp.sendMessage(message);
					
				}
				
				
			}else if(args[0].equals("accept")) {
				if(args.length != 2){
					p.sendMessage(Main.Prefix+ChatColor.RED+"Nutze /friend accept <Spielername>");
					return;
				}
				if(!SpielerUUID.idexists(args[1].toUpperCase())) {
					p.sendMessage(Main.Prefix+ChatColor.RED+"Dieser Spieler war noch nie Online!");
					return;
				}
				String uuid = SpielerUUID.getUUIDaboutid(args[1].toUpperCase());
				if(!SQLFriends.Anfragenliste(p.getUniqueId().toString()).contains(uuid)) {
					p.sendMessage(Main.Prefix+ChatColor.RED+"Du hast keine Freundschaftsanfrage dieses Spielers!");
					return;
				}
				
				if(SQLFriends.getFreundesliste(p.getUniqueId().toString()).contains(uuid)) {
					p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist mit diesem Spieler bereits befreundet!");
					return;
				}
				
				p.sendMessage(Main.Prefix+ChatColor.GREEN+"Du hast die Freundschaftsanfrage erfolgreich angenommen!");
				ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(uuid));
				if(pp != null){
					pp.sendMessage(Main.Prefix+ChatColor.GREEN+"Du bist nun mit "+MySQLRang.getchatprefix(p.getUniqueId().toString())+p.getName()+ChatColor.GREEN+" befreundet!");
				}
				List<String> list = SQLFriends.Anfragenliste(p.getUniqueId().toString());
				list.remove(uuid);
				SQLFriends.setanfragen(p.getUniqueId().toString(), list);
				List<String> friendlist1 = SQLFriends.friendlist(uuid);
				friendlist1.add(p.getUniqueId().toString());
				SQLFriends.setfriendlist(uuid, friendlist1);
				List<String> friendlist2 = SQLFriends.friendlist(p.getUniqueId().toString());
				friendlist2.add(uuid);
				SQLFriends.setfriendlist(p.getUniqueId().toString(), friendlist2);
			}else if(args[0].equals("acceptAll")) {
				if(args.length != 1){
					p.sendMessage(Main.Prefix+ChatColor.RED+"Nutze /friend acceptAll");
					return;
				}
				
				List<String> requests = SQLFriends.Anfragenliste(p.getUniqueId().toString());
				
				for(String uuid : requests) {
					String name = SpielerUUID.getSpielername(uuid);
					ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(name);
					if(!SQLFriends.getFreundesliste(p.getUniqueId().toString()).contains(uuid)) {
						p.sendMessage(Main.Prefix+ChatColor.GREEN+"Du hast die Freundschaftsanfrage von "+MySQLRang.getchatprefix(uuid)+name+ChatColor.GREEN+" erfolgreich angenommen!");	
						if(pp != null){
							pp.sendMessage(Main.Prefix+ChatColor.GREEN+"Du bist nun mit "+MySQLRang.getchatprefix(p.getUniqueId().toString())+p.getName()+ChatColor.GREEN+" befreundet!");
						}
						List<String> list = SQLFriends.Anfragenliste(p.getUniqueId().toString());
						list.remove(uuid);
						SQLFriends.setanfragen(p.getUniqueId().toString(), list);
						List<String> friendlist1 = SQLFriends.friendlist(uuid);
						friendlist1.add(p.getUniqueId().toString());
						SQLFriends.setfriendlist(uuid, friendlist1);
						List<String> friendlist2 = SQLFriends.friendlist(p.getUniqueId().toString());
						friendlist2.add(uuid);
						SQLFriends.setfriendlist(p.getUniqueId().toString(), friendlist2);
					}
				}
			
				
				
			}else if(args[0].equals("deny")) {
				
				if(args.length != 2){
					p.sendMessage(Main.Prefix+ChatColor.RED+"Nutze /friend deny <Spielername>");
					return;
				}
				if(!SpielerUUID.idexists(args[1].toUpperCase())) {
					p.sendMessage(Main.Prefix+ChatColor.RED+"Dieser Spieler war noch nie Online!");
					return;
				}
				String uuid = SpielerUUID.getUUIDaboutid(args[1].toUpperCase());
				if(!SQLFriends.Anfragenliste(p.getUniqueId().toString()).contains(uuid)) {
					p.sendMessage(Main.Prefix+ChatColor.RED+"Du hast keine Freundschaftsanfrage dieses Spielers!");
					return;
				}
				p.sendMessage(Main.Prefix+ChatColor.RED+"Du hast die Freundschaftsanfrage erfolgreich abgelehnt!");
				List<String> list = SQLFriends.Anfragenliste(p.getUniqueId().toString());
				list.remove(uuid);
				SQLFriends.setanfragen(p.getUniqueId().toString(), list);
				
			}else if(args[0].equals("list")) {
				
				if(args.length != 1){
					p.sendMessage(Main.Prefix+ChatColor.RED+"Nutze /friend list");
					return;
				}
				
				List<String> list = SQLFriends.friendlist(p.getUniqueId().toString());
				p.sendMessage(Main.Prefix+" Eine Liste deiner Freunde:");
				for(String ss : list) {
					p.sendMessage(MySQLRang.getchatprefix(ss)+SpielerUUID.getSpielername(ss));
				}
				
				
			}else if(args[0].equals("toggle")) {
				
				if(args.length != 2){
					p.sendMessage(Main.Prefix+ChatColor.RED+"Nutze /friend toggle <msg,jump,requests,party>");
					return;
				}
				
				if(args[1].equalsIgnoreCase("msg")) {
					if(Settings.getmsg(p.getUniqueId().toString())) {
						Settings.setmsg(p.getUniqueId().toString(), "false");
						p.sendMessage(Main.Prefix+ChatColor.RED+"Es können dir nun keine privaten Nachrichten mehr geschreiben werden!");
					}else {
						Settings.setmsg(p.getUniqueId().toString(), "true");
						p.sendMessage(Main.Prefix+ChatColor.GREEN+"Es können dir nun wieder privaten Nachrichten geschreiben werden!");
					}
				}else if(args[1].equalsIgnoreCase("jump")) {
					if(Settings.getjump(p.getUniqueId().toString())) {
						Settings.setjump(p.getUniqueId().toString(), "false");
						p.sendMessage(Main.Prefix+ChatColor.RED+"Spieler können dir nun nicht mehr nachspringen!");
					}else {
						Settings.setjump(p.getUniqueId().toString(), "true");
						p.sendMessage(Main.Prefix+ChatColor.GREEN+"Spieler können dir nun wieder nachspringen!");
					}
				}else if(args[1].equalsIgnoreCase("requests")) {
					if(Settings.getfriend(p.getUniqueId().toString())) {
						Settings.setfriend(p.getUniqueId().toString(), "false");
						p.sendMessage(Main.Prefix+ChatColor.RED+"Man kann dir nun keine Freundschaftsanfragen mehr schicken!");
					}else {
						p.sendMessage(Main.Prefix+ChatColor.GREEN+"Man kann dir nun wieder Freundschaftsanfragen schicken!");
						Settings.setfriend(p.getUniqueId().toString(), "true");
					}
				}else if(args[1].equalsIgnoreCase("party")) {
					if(Settings.getparty(p.getUniqueId().toString())) {
						p.sendMessage(Main.Prefix+ChatColor.RED+"Man kann dir nun keine Partyanfragen mehr schicken!");
						Settings.setparty(p.getUniqueId().toString(), "false");
					}else {
						Settings.setparty(p.getUniqueId().toString(), "true");
						p.sendMessage(Main.Prefix+ChatColor.GREEN+"Man kann dir nun wieder Partyanfragen schicken!");
					}
				}
				
			}else if(args[0].equals("remove")) {
				if(args.length != 2){
					p.sendMessage(Main.Prefix+ChatColor.RED+"Nutze /friend remove <Spielername>");
					return;
				}
				if(!SpielerUUID.idexists(args[1].toUpperCase())) {
					p.sendMessage(Main.Prefix+ChatColor.RED+"Dieser Spieler war noch nie Online!");
					return;
				}
				String uuid = SpielerUUID.getUUIDaboutid(args[1].toUpperCase());
				if(!SQLFriends.friendlist(p.getUniqueId().toString()).contains(uuid)) {
					p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist mit diesem Spieler nicht befreundet!");
					return;	
				}
				p.sendMessage(Main.Prefix+ChatColor.RED+"Die Freundschaft wurde erfolgreich aufgelöst!");
				List<String> list = SQLFriends.friendlist(uuid);
				List<String> list2 = SQLFriends.friendlist(p.getUniqueId().toString());
				list.remove(p.getUniqueId().toString());
				list2.remove(uuid);
				SQLFriends.setfriendlist(uuid, list);
				SQLFriends.setfriendlist(p.getUniqueId().toString(), list2);
				
				
				
			}else if(args[0].equals("jump")) {
				if(args.length != 2){
					p.sendMessage(Main.Prefix+ChatColor.RED+"Nutze /friend jump <Spielername>");
					return;
				}
				if(!SpielerUUID.idexists(args[1].toUpperCase())) {
					p.sendMessage(Main.Prefix+ChatColor.RED+"Dieser Spieler war noch nie Online!");
					return;
				}
				String uuid = SpielerUUID.getUUIDaboutid(args[1].toUpperCase());
				
				if(Settings.playerExists(uuid)) {
				if(Settings.getjump(uuid)==false) {
					p.sendMessage(Main.Prefix+ChatColor.RED+"Du darfst nicht zu diesem Spieler springen!");
					return;
				}
				}
				
				if(!SQLFriends.friendlist(p.getUniqueId().toString()).contains(uuid)) {
					p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist mit diesem Spieler nicht befreundet!");
					return;	
				}
				ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(uuid));
				if(pp == null){
				p.sendMessage(Main.Prefix+ChatColor.RED+"Dieser Spieler ist nicht Online!");
				return;
				}
				
				ProxiedPlayer a = ProxyServer.getInstance().getPlayer(args[1]);
				ServerInfo info = a.getServer().getInfo();
					p.sendMessage(Main.Prefix+"Springe zu "+MySQLRang.getchatprefix(a.getUniqueId().toString())+a.getName()+ChatColor.GRAY+" auf den Server "+ChatColor.YELLOW+info.getName());
					p.connect(info);
			}else{
				p.sendMessage("§7========§5Freundesystem-Befehle§7========");
				p.sendMessage(ChatColor.YELLOW+"/friend list --> Zeigt deine Freunde an");
				p.sendMessage(ChatColor.YELLOW+"/friend add <Spieler> --> Stellt eine Freundschaftsanfrage");
				p.sendMessage(ChatColor.YELLOW+"/friend accept<Spieler>-->Nimt eine Freundschaftsanfrage an");
				p.sendMessage(ChatColor.YELLOW+"/friend remove <Spieler> --> Entfernt einen Freund");
				p.sendMessage(ChatColor.YELLOW+"/friend jump <Spieler> --> Springt zu einen Spieler");
				p.sendMessage(ChatColor.YELLOW+"/friend toggle jump --> Aktiviert/Deaktiviert das Nachspringen");
				p.sendMessage(ChatColor.YELLOW+"/friend toggle msg --> Aktiviert/Deaktiviert das Schreiben von Nachrichten");
				p.sendMessage(ChatColor.YELLOW+"/friend toggle request --> Aktiviert/Deaktiviert Freundschaftsanfragen");
				p.sendMessage(ChatColor.YELLOW+"/friend toggle party --> Aktiviert/Deaktiviert das Einladen in Partys");
				p.sendMessage("§7======================================");
			}
			}else{
				p.sendMessage("§7========§5Freundesystem-Befehle§7========");
				p.sendMessage(ChatColor.YELLOW+"/friend list --> Zeigt deine Freunde an");
				p.sendMessage(ChatColor.YELLOW+"/friend add <Spieler> --> Stellt eine Freundschaftsanfrage");
				p.sendMessage(ChatColor.YELLOW+"/friend accept<Spieler>-->Nimt eine Freundschaftsanfrage an");
				p.sendMessage(ChatColor.YELLOW+"/friend remove <Spieler> --> Entfernt einen Freund");
				p.sendMessage(ChatColor.YELLOW+"/friend jump <Spieler> --> Springt zu einen Spieler");
				p.sendMessage(ChatColor.YELLOW+"/friend toggle jump --> Aktiviert/Deaktiviert das Nachspringen");
				p.sendMessage(ChatColor.YELLOW+"/friend toggle msg --> Aktiviert/Deaktiviert das Schreiben von Nachrichten");
				p.sendMessage(ChatColor.YELLOW+"/friend toggle request --> Aktiviert/Deaktiviert Freundschaftsanfragen");
				p.sendMessage(ChatColor.YELLOW+"/friend toggle party --> Aktiviert/Deaktiviert das Einladen in Partys");
				p.sendMessage("§7======================================");
			}
	}

}
