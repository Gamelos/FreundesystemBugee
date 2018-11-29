package de.gamelos.bungeefriends;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class Main extends Plugin implements Listener{

	public static String Prefix = ChatColor.GRAY+"["+ChatColor.RED+"Friends"+ChatColor.GRAY+"] ";
	public static String PartyPrefix = ChatColor.GRAY+"["+ChatColor.DARK_PURPLE+"Party"+ChatColor.GRAY+"] ";
	public static MySQL mysql;
	public static HashMap<ProxiedPlayer, String>remessage = new HashMap<>();
	
	@Override
	public void onEnable() {
		System.out.println("[Friends] Das Plugin wurde geladen!");
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new friend("friend"));
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new msg("msg"));
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new r("r"));
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new party("party"));
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new partychat("p"));
		ProxyServer.getInstance().getPluginManager().registerListener(this, this);
		BungeeCord.getInstance().registerChannel("info");
		ConnectMySQL();
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		System.out.println("[Friends] Das Plugin wurde deaktiviert!");
		super.onDisable();
	}

	private void ConnectMySQL(){
		mysql = new MySQL(de.gamelos.system.Main.gethost(), de.gamelos.system.Main.getuser(), de.gamelos.system.Main.getdatabase(), de.gamelos.system.Main.getpassword());
		mysql.update("CREATE TABLE IF NOT EXISTS Raenge(UUID varchar(64), RANGNAME varchar(1000), PREFIX varchar(1000), TIME varchar(1000));");
		mysql.update("CREATE TABLE IF NOT EXISTS Online(UUID varchar(64), Servername varchar(1000));");
		mysql.update("CREATE TABLE IF NOT EXISTS SpielerUUID(UUID varchar(64), Spielername varchar(1000), id varchar(1000));");
		mysql.update("CREATE TABLE IF NOT EXISTS Frienddata(UUID varchar(64), Freundesliste varchar(1000), Anfragen varchar(1000));");
		mysql.update("CREATE TABLE IF NOT EXISTS Settingsdata(UUID varchar(64), msg varchar(1000), party varchar(1000), requests varchar(1000), jump varchar(1000), status varchar(1000));");
		mysql.update("CREATE TABLE IF NOT EXISTS Settings(UUID varchar(64), jump varchar(1000), msg varchar(1000), party varchar(1000), friend varchar(1000), status varchar(1000));");
		mysql.update("CREATE TABLE IF NOT EXISTS Party(ID varchar(64), PARTYLIST varchar(1000), LEADER varchar(1000), MODLIST varchar(1000), PRIVATE varchar(1000), TYPE varchar(1000));");
		mysql.update("CREATE TABLE IF NOT EXISTS Partyid(UUID varchar(64), ID varchar(1000));");
		mysql.update("CREATE TABLE IF NOT EXISTS Freundesysteminfo(UUID varchar(64), Seite varchar(1000), LastOnline varchar(1000));");
	}
	
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onmsg(PluginMessageEvent e){
		if(!e.getTag().equalsIgnoreCase("BungeeCord"))
			return;
		DataInputStream stream = new DataInputStream(new ByteArrayInputStream(e.getData()));
		try {
			String channel = stream.readUTF();
			if(channel.equals("data")){
				String input = stream.readUTF();
				ProxiedPlayer p = ((ProxiedPlayer)e.getReceiver());
				System.out.println(input);
				if(input.equalsIgnoreCase("promote")){
					String server = p.getServer().getInfo().getName();
					System.out.println("promote/"+p.getName()+"/"+server);
					sendtoserver("data", "promote/"+p.getName()+"/"+server, BungeeCord.getInstance().getServerInfo("Lobby-1"));
				}else if(input.equalsIgnoreCase("servername")){
					String server = p.getServer().getInfo().getName();
					sendtoserver("data", "servername/"+server, BungeeCord.getInstance().getServerInfo(server));
				}else {
				String[] a = input.split("/");
				String name = a[1];
				String cmd = a[0];
				
				if(cmd.equals("invite")) {
					dispatchCommand("party invite "+name, p);
				}else if(cmd.equals("jump")) {
					dispatchCommand("friend jump "+name, p);
				}else if(cmd.equals("claninvite")) {	
					dispatchCommand("clan invite "+name, p);
				}else if(cmd.equals("remove")) {	
					dispatchCommand("friend remove "+name, p);
				}else if(cmd.equals("clanreduce")) {
					dispatchCommand("clan reduce "+name, p);
				}else if(cmd.equals("clanpromote")) {
					dispatchCommand("clan promote "+name, p);
				}else if(cmd.equals("clanremove")) {
					dispatchCommand("clan remove "+name, p);
				}else if(cmd.equals("partyreduce")) {
					dispatchCommand("party reduce "+name, p);
				}else if(cmd.equals("partypromote")) {
					dispatchCommand("party promote "+name, p);
				}else if(cmd.equals("partyremove")) {
					dispatchCommand("party kick "+name, p);
				}else if(cmd.equals("friendremove")) {
					dispatchCommand("friend deny "+name, p);
				}else if(cmd.equals("friendaccept")) {	
					dispatchCommand("friend accept "+name, p);
				}else if(cmd.equals("partyjoin")) {	
					dispatchCommand("party join "+name, p);
				}else if(cmd.equals("partycreate")) {
					
					if(PartyID.playerExists(p.getUniqueId().toString())) {
						p.sendMessage(Main.PartyPrefix+ChatColor.RED+"Du bist bereits in einer anderen Party!");
						return;
					}
					
					String gamemode = input.split("/")[1];
					String partyid = party.randomid();
					List<String> list = new ArrayList<>();
					list.add(p.getName());
					SQLParty.createParty(partyid, p.getName(), SQLParty.ListToString(list), "false", gamemode);
					PartyID.setID(p.getUniqueId().toString(), partyid);
					p.sendMessage(PartyPrefix+ChatColor.GREEN+"Du hast erfolgreich eine Öffentliche Party erstellt!");
					
				}
			}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void dispatchCommand(String cmd, ProxiedPlayer p) {
		BungeeCord.getInstance().getPluginManager().dispatchCommand(p, cmd);
	}
	
	public static void sendtoserver(String channel, String msg, ServerInfo server){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		try {
			output.writeUTF(channel);
			output.writeUTF(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		server.sendData("info", stream.toByteArray());
	}
	
	@EventHandler
	public void onlogin(PostLoginEvent e) {
		ProxiedPlayer p = e.getPlayer();
		Online.createPlayer(p.getName(),"null");
	}
	
	@EventHandler
	public void onPostLogout(PlayerDisconnectEvent e){
		
		ProxiedPlayer p = e.getPlayer();
		Online.removeSpieler(p.getName());
		
	}
	
	@EventHandler
	public void onswitch(ServerSwitchEvent e){
		ProxiedPlayer p = e.getPlayer();
		Online.setServer(p.getName(), ""+p.getServer().getInfo().getName());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onlogin1(PostLoginEvent e) {
		ProxiedPlayer p = e.getPlayer();
		List<String> list = SQLFriends.friendlist(p.getUniqueId().toString());
		
		p.sendMessage(Main.Prefix+ChatColor.GOLD+"Du hast noch "+ChatColor.YELLOW+SQLFriends.Anfragenliste(p.getUniqueId().toString()).size()+ChatColor.GOLD+" Freundschaftsanfragen offen!");
		
		for(String ss:list) {
			String name = SpielerUUID.getSpielername(ss);
			if(BungeeCord.getInstance().getPlayer(name)!=null) {
				ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(name);
				pp.sendMessage(Main.Prefix+MySQLRang.getchatprefix(p.getUniqueId().toString())+p.getName()+ChatColor.GRAY+" ist nun "+ChatColor.GREEN+"online");
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onlogin1(PlayerDisconnectEvent e) {
		ProxiedPlayer p = e.getPlayer();
		List<String> list = SQLFriends.friendlist(p.getUniqueId().toString());
		for(String ss:list) {
			String name = SpielerUUID.getSpielername(ss);
			if(BungeeCord.getInstance().getPlayer(name)!=null) {
				ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(name);
				pp.sendMessage(Main.Prefix+MySQLRang.getchatprefix(p.getUniqueId().toString())+p.getName()+ChatColor.GRAY+" ist nun "+ChatColor.RED+"offline");
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onpartyswitch(ServerSwitchEvent e) {
		ProxiedPlayer p = e.getPlayer();
		if(!PartyID.playerExists(p.getUniqueId().toString())) {
			return;
		}
		String id = PartyID.getID(p.getUniqueId().toString());
		if(!SQLParty.getLeader(id).equals(p.getName())) {
			return;
		}
		
		ServerInfo info = p.getServer().getInfo();
		if(!info.getName().contains("Lobby")){
			List<String> partylist = SQLParty.StringToList(SQLParty.getPartylist(id));
			for(String l : partylist){
				ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(l);
				pp.sendMessage(Main.PartyPrefix+ChatColor.YELLOW+"Die Party verbindet sich auf den Server: "+ChatColor.GRAY+info.getName());
				if(pp!=p) {
				pp.connect(info);
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onquits(PlayerDisconnectEvent e) {
		ProxiedPlayer p = e.getPlayer();
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Timestamp(System.currentTimeMillis()));
		SimpleDateFormat sdf = new SimpleDateFormat("'Datum:' dd MMMM yyyy  'Uhrzeit:'HH:mm:ss", Locale.GERMAN);
		String date = sdf.format(c.getTime())+"";
		System.out.println(date);
		Freundesysteminfo.setLastOnline(p.getUniqueId().toString(), date);
		
		if(!PartyID.playerExists(p.getUniqueId().toString())) {
			return;
		}
		String id = PartyID.getID(p.getUniqueId().toString());
		List<String> modlist = SQLParty.StringToList(SQLParty.getModlist(id));
		List<String> partylist = SQLParty.StringToList(SQLParty.getPartylist(id));
		for(String s:partylist) {
			ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(s);
			if(pp!=null) {
			pp.sendMessage(Main.PartyPrefix+MySQLRang.getchatprefix(p.getUniqueId().toString())+p.getName()+ChatColor.GRAY+" hat die Party verlassen!");
		}
		}
		
		
		if(SQLParty.getLeader(id).equals(p.getName())) {
			PartyID.removeSpieler(p.getUniqueId().toString());
			partylist.remove(p.getName());
			if(partylist.size()<=1) {
				for(String ss:partylist) {
					ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(ss);
					if(pp!=null) {
				PartyID.removeSpieler(pp.getUniqueId().toString());
				pp.sendMessage(Main.PartyPrefix+ChatColor.RED+"Die Party wurde wegen zu wenigen Mitgliedern aufgelöst!");
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
				PartyID.removeSpieler(pp.getUniqueId().toString());
				pp.sendMessage(Main.PartyPrefix+ChatColor.RED+"Die Party wurde wegen zu wenigen Mitgliedern aufgelöst!");
					}
				}
				SQLParty.removeParty(id);
				return;
			}
			SQLParty.setPartylist(id, SQLParty.ListToString(partylist));
		}
		
		
	}
	
}
