package de.gamelos.bungeefriends;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class party extends Command {

	public party(String name) {
		super(name);
	}

	public static HashMap<ProxiedPlayer,ArrayList<ProxiedPlayer>> requests = new HashMap<>();
	
	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		ProxiedPlayer p  = (ProxiedPlayer) sender;
		if(args.length >= 1){
			
			if(args[0].equals("invite")) {
				
				if(args.length!=2) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Nutze /party invite <Spielername>");
					return;
				}
				
				String name = args[1];
				ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(name);
				
				if(PartyID.playerExists(p.getUniqueId().toString())) {
				String senderid = PartyID.getID(p.getUniqueId().toString());
				if(!(SQLParty.getLeader(senderid).equals(p.getName())||SQLParty.StringToList(SQLParty.getModlist(senderid)).contains(p.getName()))){
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du bist nicht Leader der Party!");
					return;
				}
				}
				
				if(pp==null) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler ist nicht Online!");
					return;
				}
				
				if(p.getName().equals(pp.getName())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du darfst dich nicht selber einladen!");
					return;
				}
				
				if(Settings.getparty(pp.getUniqueId().toString())==false) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler nimmt keine Partyanfragen an!");
					return;
				}
				
				if(PartyID.playerExists(pp.getUniqueId().toString())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler ist bereits in einer Party!");
					return;
				}
				
				if(requests.containsKey(pp)) {
					if(requests.get(pp).contains(p)) {
						p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du hast diesem Spieler bereits eine Einladung geschickt!");
						return;
					}
				}
				
				if(PartyID.playerExists(p.getUniqueId().toString())) {
					List<String> list = SQLParty.StringToList(SQLParty.getPartylist(PartyID.getID(p.getUniqueId().toString())));
					if(list.size()>=9) {
						p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Deine Party ist voll");
						return;
					}
				}
				
				
				if(requests.containsKey(pp)) {
					ArrayList<ProxiedPlayer> list = requests.get(pp);
					list.add(p);
					requests.put(pp, list);
				}else {
					ArrayList<ProxiedPlayer> list = new ArrayList<>();
					list.add(p);
					requests.put(pp, list);
				}
				
				p.sendMessage(Main.PartyPrefix+ChatColor.GREEN+"Du hast den Spieler erfolgreich eingeladen!");
				pp.sendMessage(Main.PartyPrefix+p.getName()+" läd dich in seine party ein!");
				
				TextComponent message = new TextComponent(Main.PartyPrefix+"Du hast die Optionen: ");
				TextComponent accept = new TextComponent("[Akzeptieren]");
				accept.setColor(ChatColor.GREEN);
				accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept "+p.getName()));
				accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GREEN+"Nimmt die Partyanfrage an").create()));
				message.addExtra(accept);
				TextComponent free = new TextComponent(" ");
				message.addExtra(free);
				TextComponent deny = new TextComponent("[Ablehnen]");
				deny.setColor(ChatColor.RED);
				deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party deny "+p.getName()));
				deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED+"Lehnt die Partyanfrage ab").create()));
				message.addExtra(deny);
				pp.sendMessage(message);
			
				
			}else if(args[0].equals("accept")) {
				
				if(args.length!=2) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Nutze /party accept <Spielername>");
					return;
				}
				
				String name = args[1];
				ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(name);
				
				if(pp==null) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler ist nicht Online!");
					return;
				}
				
				if(!requests.containsKey(p)) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du hast keine Enladung dieses Spielers bekommen!");
					return;
				}
				
				if(PartyID.playerExists(p.getUniqueId().toString())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du bist bereits in einer anderen Party!");
					return;
				}
				
				if(!requests.get(p).contains(pp)) {
						p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du hast keine Enladung dieses Spielers bekommen!");
						return;
				}
				
				if(PartyID.playerExists(pp.getUniqueId().toString())) {
					if(SQLParty.StringToList(SQLParty.getPartylist(PartyID.getID(pp.getUniqueId().toString()))).size() >=9) {
						p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Diese Party ist voll!");
						return;
					}
				}
				
				ArrayList<ProxiedPlayer> list = requests.get(p);
				list.remove(pp);
				p.sendMessage(Main.PartyPrefix+ChatColor.GREEN+"Du hast die Enladung erfolgreich angenommen!");
				
				if(PartyID.playerExists(pp.getUniqueId().toString())) {
					String id = PartyID.getID(pp.getUniqueId().toString());
					List<String> partylist = SQLParty.StringToList(SQLParty.getPartylist(id));
					partylist.add(p.getName());
					for(String s : partylist) {
						ProxiedPlayer ppp = BungeeCord.getInstance().getPlayer(s);
						if(ppp!=null) {
							ppp.sendMessage(Main.PartyPrefix+MySQLRang.getchatprefix(p.getUniqueId().toString())+p.getName()+ChatColor.GRAY+" ist der Party beigetreten!");
						}
					}
					PartyID.setID(p.getUniqueId().toString(), id);
					SQLParty.setPartylist(id, SQLParty.ListToString(partylist));
				}else {
				String id = randomid();
				List<String> partylist = new ArrayList<>();
				partylist.add(pp.getName());
				partylist.add(p.getName());
				for(String s : partylist) {
					ProxiedPlayer ppp = BungeeCord.getInstance().getPlayer(s);
					if(ppp!=null) {
						ppp.sendMessage(Main.PartyPrefix+MySQLRang.getchatprefix(p.getUniqueId().toString())+p.getName()+ChatColor.GRAY+" ist der Party beigetreten!");
					}
				}
				PartyID.createPlayer(pp.getUniqueId().toString(), id);
				PartyID.createPlayer(p.getUniqueId().toString(), id);
				SQLParty.createParty(id, pp.getName(), SQLParty.ListToString(partylist), "true", "null");
				
				}
				
				
				
			}else if(args[0].equals("join")) {
				
				if(args.length!=2) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Nutze /party join <Spielername>");
					return;
				}
				
				String name = args[1];
				ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(name);
				if(pp==null) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler hat keine Party");
					return;
				}
				
				if(!PartyID.playerExists(pp.getUniqueId().toString())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler hat keine Party");
					return;
				}
				
				if(PartyID.playerExists(p.getUniqueId().toString())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du bist bereits in einer anderen Party");
					return;	
				}
				
				String id = PartyID.getID(pp.getUniqueId().toString());
				if(SQLParty.getPrivate(id)) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler hat keine Öffentliche Party");
					return;	
				}
				
				if(PartyID.playerExists(pp.getUniqueId().toString())) {
					if(SQLParty.StringToList(SQLParty.getPartylist(PartyID.getID(pp.getUniqueId().toString()))).size() >=9) {
						p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Diese Party ist voll!");
						return;
					}
				}
				
				List<String> list = SQLParty.StringToList(SQLParty.getPartylist(id));
				list.add(p.getName());
				SQLParty.setPartylist(id, SQLParty.ListToString(list));
				PartyID.setID(p.getUniqueId().toString(), id);
				p.sendMessage(Main.PartyPrefix+ChatColor.GREEN+"Du bist der Party erfolgreich beigtreten!");
				for(String s : list) {
					ProxiedPlayer ppp = BungeeCord.getInstance().getPlayer(s);
					if(ppp!=null) {
						ppp.sendMessage(Main.PartyPrefix+MySQLRang.getchatprefix(p.getUniqueId().toString())+p.getName()+ChatColor.GRAY+" ist der Party beigetreten!");
					}
				}
				
				
			}else if(args[0].equals("kick")) {
				
				if(args.length!=2) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Nutze /party kick <Spielername>");
					return;
				}
				
				if(!PartyID.playerExists(p.getUniqueId().toString())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du bist in keiner Party!");
					return;
				}
				
				String id = PartyID.getID(p.getUniqueId().toString());
				if(!SQLParty.idExists(id)) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du bist in keiner Party!");
					return;	
				}
				
				if(!SQLParty.getLeader(id).equals(p.getName())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du bist nicht Leader der Party!");
					return;	
				}
				
				String name = args[1];
				ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(name);
				
				if(pp==null) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler ist nicht Online!");
					return;	
				}
				
				if(!PartyID.playerExists(pp.getUniqueId().toString())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler ist nicht in deiner Party!");
					return;	
				}
				
				if(!SQLParty.StringToList(SQLParty.getPartylist(id)).contains(pp.getName())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler ist nicht in deiner Party!");
					return;	
				}
				
				if(SQLParty.getLeader(id).equals(pp.getName())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du darfst keinen Leader Kicken!");
					return;		
				}
				
				
				if(SQLParty.StringToList(SQLParty.getModlist(id)).contains(pp.getName())) {
					List<String> list = SQLParty.StringToList(SQLParty.getModlist(id));
					list.remove(pp.getName());
					SQLParty.setModlist(id, SQLParty.ListToString(list));
				}
				
				List<String> l = SQLParty.StringToList(SQLParty.getPartylist(id));
				l.remove(pp.getName());
				PartyID.removeSpieler(pp.getUniqueId().toString());
				
				if(l.size()<=1) {
					for(String ss:l) {
						ProxiedPlayer pppp = BungeeCord.getInstance().getPlayer(ss);
						if(pppp!=null) {
							pppp.sendMessage(Main.PartyPrefix+ChatColor.RED+"Die Party wurde wegen zu wenigen Mitgliedern aufgelöst!");
					PartyID.removeSpieler(pppp.getUniqueId().toString());
						}
					}
					SQLParty.removeParty(id);
					return;
				}
				SQLParty.setPartylist(id, SQLParty.ListToString(l));
				for(String s:l) {
					ProxiedPlayer ppp = BungeeCord.getInstance().getPlayer(s);
					if(ppp!=null) {
						ppp.sendMessage(Main.PartyPrefix+MySQLRang.getchatprefix(pp.getUniqueId().toString())+pp.getName()+ChatColor.GRAY+" hat die Party verlassen!");
					}
				}
				p.sendMessage(Main.PartyPrefix+ChatColor.GREEN+"Du hast den Spieler erfolgreich gekickt!");
				pp.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du wurdest aus der Party gekickt!");
				
			}else if(args[0].equals("promote")) {
				
				if(args.length!=2) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Nutze /party promote <Spielername>");
					return;
				}
				
				if(!PartyID.playerExists(p.getUniqueId().toString())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du bist in keiner Party!");
					return;
				}
				
				if(!SQLParty.getLeader(PartyID.getID(p.getUniqueId().toString())).equals(p.getName())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du bist nicht Leader der Party!");
					return;
				}
				
				String name = args[1];
				ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(name);
				if(pp==null) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler ist nicht Online!");
					return;
				}
				
				if(!PartyID.playerExists(pp.getUniqueId().toString())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler ist nicht in deiner Party!");
					return;
				}
				
				String id = PartyID.getID(pp.getUniqueId().toString());
				
				if(!SQLParty.idExists(id)) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler ist nicht in deiner Party!");
					return;
				}
				
				if(!SQLParty.StringToList(SQLParty.getPartylist(id)).contains(p.getName())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler ist nicht in deiner Party!");
					return;
				}
				
				if(pp==p) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du kannst dich nicht selber Promoten!");
					return;
				}
				
				List<String> modlist = SQLParty.StringToList(SQLParty.getModlist(id));
				List<String> partylist = SQLParty.StringToList(SQLParty.getPartylist(id));
				String leader = p.getName();
				
				
				if(modlist.contains(pp.getName())) {
					modlist.remove(pp.getName());
					modlist.add(leader);
					SQLParty.setModlist(id, SQLParty.ListToString(modlist));
					SQLParty.setLeader(id, pp.getName());
					for(String s:partylist) {
						ProxiedPlayer ppp = BungeeCord.getInstance().getPlayer(s);
						if(ppp!=null) {
							ppp.sendMessage(Main.PartyPrefix+MySQLRang.getchatprefix(pp.getUniqueId().toString())+pp.getName()+ChatColor.GRAY+" ist nun PartyLeader!");
						}
					}
				}else {
					modlist.add(pp.getName());
					SQLParty.setModlist(id, SQLParty.ListToString(modlist));
					for(String s:partylist) {
						ProxiedPlayer ppp = BungeeCord.getInstance().getPlayer(s);
						if(ppp!=null) {
							ppp.sendMessage(Main.PartyPrefix+MySQLRang.getchatprefix(pp.getUniqueId().toString())+pp.getName()+ChatColor.GRAY+" ist nun PartyModerator!");
						}
					}
				}
				
			}else if(args[0].equals("reduce")) {
				
				if(args.length!=2) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Nutze /party reduce <Spielername>");
					return;
				}
				
				if(!PartyID.playerExists(p.getUniqueId().toString())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du bist in keiner Party!");
					return;
				}
				
				if(!SQLParty.getLeader(PartyID.getID(p.getUniqueId().toString())).equals(p.getName())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du bist nicht Leader der Party!");
					return;
				}
				
				String name = args[1];
				ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(name);
				if(pp==null) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler ist nicht Online!");
					return;
				}
				
				if(!PartyID.playerExists(pp.getUniqueId().toString())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler ist nicht in deiner Party!");
					return;
				}
				
				String id = PartyID.getID(pp.getUniqueId().toString());
				
				if(!SQLParty.idExists(id)) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler ist nicht in deiner Party!");
					return;
				}
				
				if(!SQLParty.StringToList(SQLParty.getPartylist(id)).contains(p.getName())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler ist nicht in deiner Party!");
					return;
				}
				
				if(pp==p) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du kannst dich nicht selber Promoten!");
					return;
				}
				
				List<String> modlist = SQLParty.StringToList(SQLParty.getModlist(id));
				List<String> partylist = SQLParty.StringToList(SQLParty.getPartylist(id));
				
				
				if(modlist.contains(pp.getName())) {
					modlist.remove(pp.getName());
					SQLParty.setModlist(id, SQLParty.ListToString(modlist));
					for(String s:partylist) {
						ProxiedPlayer ppp = BungeeCord.getInstance().getPlayer(s);
						if(ppp!=null) {
							ppp.sendMessage(Main.PartyPrefix+MySQLRang.getchatprefix(pp.getUniqueId().toString())+pp.getName()+ChatColor.GRAY+" ist nun PartyModerator!");
						}
					}
				}else {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler ist bereits nur Party Mitglied");
				}
				
			}else if(args[0].equals("deny")) {
				if(args.length!=2) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Nutze /party deny <Spielername>");
					return;
				}
				
				String name = args[1];
				ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(name);
				
				if(pp==null) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Dieser Spieler ist nicht Online!");
					return;
				}
				
				if(!requests.containsKey(p)) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du hast keine Enladung dieses Spielers bekommen!");
					return;
				}
				
				if(!requests.get(p).contains(pp)) {
						p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du hast keine Enladung dieses Spielers bekommen!");
						return;
				}
				
				ArrayList<ProxiedPlayer> list = requests.get(p);
				list.remove(pp);
				p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du hast die Enladung erfolgreich abgelehnt!");
			
				
			}else if(args[0].equals("list")) {
				if(!PartyID.playerExists(p.getUniqueId().toString())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du bist in keiner Party!");
					return;
				}
				
				String id = PartyID.getID(p.getUniqueId().toString());
				
				if(!SQLParty.idExists(id)) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du bist in keiner Party!");
					return;
				}
				int size = 0;
				List<String> partylist = SQLParty.StringToList(SQLParty.getPartylist(id));
				size = partylist.size();
				List<String> modlist = SQLParty.StringToList(SQLParty.getModlist(id));
				String leader = SQLParty.getLeader(id);
				
				partylist.remove(leader);
				for(String s:modlist) {
					partylist.remove(s);
				}
				
				p.sendMessage(ChatColor.GRAY+"PartyID: "+ChatColor.YELLOW+id);
				p.sendMessage(ChatColor.GRAY+"Size: "+ChatColor.YELLOW+size);
				if(SQLParty.getPrivate(id)) {
					p.sendMessage(ChatColor.GRAY+"Type: "+ChatColor.YELLOW+"PRIVATE");
				}else {
					p.sendMessage(ChatColor.GRAY+"Type: "+ChatColor.YELLOW+"PUBLIC");
					p.sendMessage(ChatColor.GRAY+"Spielmodus: "+SQLParty.getType(id));
				}
				p.sendMessage(" ");
				p.sendMessage(ChatColor.GRAY+"Leader: "+ChatColor.AQUA+leader);
				p.sendMessage(ChatColor.GRAY+"Moderator: "+ChatColor.RED+modliste(modlist));
				p.sendMessage(ChatColor.GRAY+"Member: "+ChatColor.GREEN+memberlist(partylist));
				
			
			}else if(args[0].equals("leave")) {
				
				if(!PartyID.playerExists(p.getUniqueId().toString())) {
					p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du bist in keiner Party!");
					return;
				}
				String id = PartyID.getID(p.getUniqueId().toString());
				List<String> modlist = SQLParty.StringToList(SQLParty.getModlist(id));
				List<String> partylist = SQLParty.StringToList(SQLParty.getPartylist(id));
				for(String s:partylist) {
					ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(s);
					pp.sendMessage(Main.PartyPrefix+MySQLRang.getchatprefix(p.getUniqueId().toString())+p.getName()+ChatColor.GRAY+" hat die Party verlassen!");
				}
				
				
				if(SQLParty.getLeader(id).equals(p.getName())) {
					PartyID.removeSpieler(p.getUniqueId().toString());
					partylist.remove(p.getName());
					if(partylist.size()<=1) {
						for(String ss:partylist) {
							ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(ss);
							if(pp!=null) {
						pp.sendMessage(Main.PartyPrefix+ChatColor.RED+"Die Party wurde wegen zu wenigen Mitgliedern aufgelöst!");
						PartyID.removeSpieler(pp.getUniqueId().toString());
							}
						}
						SQLParty.removeParty(id);
						return;
					}
					SQLParty.setPartylist(id, SQLParty.ListToString(partylist));
					
					String leader = partylist.get(0);
					
					if(modlist.contains(leader)) {
						modlist.remove(leader);
						SQLParty.setModlist(id, SQLParty.ListToString(modlist));
					}
					
					for(String s:partylist) {
						ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(s);
						pp.sendMessage(Main.PartyPrefix+ChatColor.AQUA+leader+ChatColor.GRAY+" ist neuer Partyleader!");
					}
					
					if(modlist.contains(p.getName())) {
						modlist.remove(p.getName());
						SQLParty.setModlist(id, SQLParty.ListToString(modlist));
					}
					SQLParty.setLeader(id, leader);
					
				}else {
					PartyID.removeSpieler(p.getUniqueId().toString());
					if(modlist.contains(p.getName())) {
						modlist.remove(p.getName());
						SQLParty.setModlist(id, SQLParty.ListToString(modlist));
					}
					partylist.remove(p.getName());
					if(partylist.size()<=1) {
						for(String ss:partylist) {
							ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(ss);
							if(pp!=null) {
								pp.sendMessage(Main.PartyPrefix+ChatColor.RED+"Die Party wurde wegen zu wenigen Mitgliedern aufgelöst!");
						PartyID.removeSpieler(pp.getUniqueId().toString());
							}
						}
						SQLParty.removeParty(id);
						return;
					}
					SQLParty.setPartylist(id, SQLParty.ListToString(partylist));
				}
				
				
			}else {
				p.sendMessage("§7========§5Partysystem-Befehle§7========");
				p.sendMessage(ChatColor.DARK_PURPLE+"/party list --> Zeigt alle in der Party an");
				p.sendMessage(ChatColor.DARK_PURPLE+"/party invite <Spieler> --> Läd einen Spieler ein");
				p.sendMessage(ChatColor.DARK_PURPLE+"/party join <Spieler> --> Joint einer öffentlichen Party");
				p.sendMessage(ChatColor.DARK_PURPLE+"/party accept <Spieler> --> Nimmt eine Partyanfrage an");
				p.sendMessage(ChatColor.DARK_PURPLE+"/party deny <Spieler> --> Lehnt eine Partyanfrage ab");
				p.sendMessage(ChatColor.DARK_PURPLE+"/party leave --> Verlässt eine Party");
				p.sendMessage(ChatColor.DARK_PURPLE+"/party kick --> Kickt einen Spieler aus deiner Party");
				p.sendMessage(ChatColor.DARK_PURPLE+"/party promote <Spieler> --> Promotet einen Spieler deiner Party");
				p.sendMessage("§7======================================");
			}
			
			
		}else {
			p.sendMessage("§7========§5Partysystem-Befehle§7========");
			p.sendMessage(ChatColor.DARK_PURPLE+"/party list --> Zeigt alle in der Party an");
			p.sendMessage(ChatColor.DARK_PURPLE+"/party invite <Spieler> --> Läd einen Spieler ein");
			p.sendMessage(ChatColor.DARK_PURPLE+"/party join <Spieler> --> Joint einer öffentlichen Party");
			p.sendMessage(ChatColor.DARK_PURPLE+"/party accept <Spieler> --> Nimmt eine Partyanfrage an");
			p.sendMessage(ChatColor.DARK_PURPLE+"/party deny <Spieler> --> Lehnt eine Partyanfrage ab");
			p.sendMessage(ChatColor.DARK_PURPLE+"/party leave --> Verlässt eine Party");
			p.sendMessage(ChatColor.DARK_PURPLE+"/party kick --> Kickt einen Spieler aus deiner Party");
			p.sendMessage(ChatColor.DARK_PURPLE+"/party promote <Spieler> --> Promotet einen Spieler deiner Party");
			p.sendMessage("§7======================================");
		}
	}
	
	public static String memberlist(List<String> partylist) {
		if(partylist.size()>0) {
		return SQLParty.ListToString(partylist).substring(0, SQLParty.ListToString(partylist).length()-1);
		}else {
			return "-";
		}
	}
	
	public static String modliste(List<String> modlist) {
		if(modlist.size()>0) {
		return 	SQLParty.ListToString(modlist).substring(0, SQLParty.ListToString(modlist).length()-1);
		}else {
			return "-";
		}
	}
	
	public static String randomid() {
		String s = "";
		Random r = new Random();
		int i = r.nextInt(99999999);
		if(!PartyID.idExists(""+i)) {
			s = ""+i;
			return s;	
		}else {
			return randomid();
		}
	}

}
