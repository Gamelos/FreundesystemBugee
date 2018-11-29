package de.gamelos.bungeefriends;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.md_5.bungee.api.ChatColor;



public class MySQLRang {
	
	//TODO Rangnamen:
	//   - Admin
	//   - Mod
	//   - Sup
	//   - Builder
	//   - Youtuber
	//   - Eiszeit
	//   - Premium

public static boolean playerExists(String uuid){
		
		
		try {
			@SuppressWarnings("static-access")
			ResultSet rs = Main.mysql.querry("SELECT * FROM Raenge WHERE UUID = '"+ uuid + "'");
			if(rs.next()){
				return rs.getString("UUID") != null;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void createPlayer(String uuid){
		if(!(playerExists(uuid))){
			Main.mysql.update("INSERT INTO Raenge(UUID, RANGNAME, PREFIX, TIME) VALUES ('" +uuid+ "', '0', '0', '0');");
		}
	}
	
	//get-----------------------------------------------------------------------------------------------------------------------------------
	public static String getRangname(String uuid){
		String i = null;
		if(playerExists(uuid)){
			try {
				@SuppressWarnings("static-access")
				ResultSet rs = Main.mysql.querry("SELECT * FROM Raenge WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("RANGNAME")) == null));
				
				i = rs.getString("RANGNAME");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i;
	}
	
	
	
	public static String getPrefix(String uuid){
		String i = null;
		if(playerExists(uuid)){
			try {
				@SuppressWarnings("static-access")
				ResultSet rs = Main.mysql.querry("SELECT * FROM Raenge WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("PREFIX")) == null));
				
				i = rs.getString("PREFIX");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i;
	}
	
	public static String getTime(String uuid){
		String i = null;
		if(playerExists(uuid)){
			try {
				@SuppressWarnings("static-access")
				ResultSet rs = Main.mysql.querry("SELECT * FROM Raenge WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("TIME")) == null));
				
				i = rs.getString("TIME");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i;
	}
	
	//set-----------------------------------------------------------------------------------------------------------------------------------
	
	public static void setRangname(String uuid, String Rangname){
		
		if(playerExists(uuid)){
			Main.mysql.update("UPDATE Raenge SET RANGNAME= '" + Rangname+ "' WHERE UUID= '" + uuid+ "';");
		}else{
			createPlayer(uuid);
			setRangname(uuid, Rangname);
		}
		
	}
	
	public static void setPrefix(String uuid, String Prefix){
		
		if(playerExists(uuid)){
			Main.mysql.update("UPDATE Raenge SET PREFIX= '" + Prefix+ "' WHERE UUID= '" + uuid+ "';");
		}else{
			createPlayer(uuid);
			setPrefix(uuid, Prefix);
		}
	}
	
	public static void setTime(String uuid, String Time){
		
		if(playerExists(uuid)){
			Main.mysql.update("UPDATE Raenge SET TIME= '" + Time+ "' WHERE UUID= '" + uuid+ "';");
		}else{
			createPlayer(uuid);
			setTime(uuid, Time);
		}
	}
	
public static void delPlayer(String uuid){
		
		if(playerExists(uuid)){
			Main.mysql.update("DELETE FROM Raenge WHERE UUID = '"+uuid+"';");
		}else{
			createPlayer(uuid);
			delPlayer(uuid);
		}
	}
	
	public static String getchatprefix(String uuid){
		String s = "";
		if(MySQLRang.getRangname(uuid)!=null){
			if(MySQLRang.getRangname(uuid).equals("Admin")){
				s = ChatColor.DARK_RED+"";
			}else if(MySQLRang.getRangname(uuid).equals("Mod")){
				s = ChatColor.RED+"";
			}else if(MySQLRang.getRangname(uuid).equals("Sup")){
				s = ChatColor.BLUE+"";
			}else if(MySQLRang.getRangname(uuid).equals("Builder")){
				s = ChatColor.DARK_AQUA+"";
			}else if(MySQLRang.getRangname(uuid).equals("Youtuber")){
				s = ChatColor.DARK_PURPLE+"";
			}else if(MySQLRang.getRangname(uuid).equals("Eiszeit")){
				s = ChatColor.AQUA+"";
			}else if(MySQLRang.getRangname(uuid).equals("Premium")){
				s = ChatColor.GOLD+"";
			}else if(MySQLRang.getRangname(uuid).equals("Prem+")){
				s = ChatColor.YELLOW+"";
			}else if(MySQLRang.getRangname(uuid).equals("Dev")){
				s = ChatColor.AQUA+"";
			}else if(MySQLRang.getRangname(uuid).equals("Contant")){
				s = ChatColor.RED+"";
			}else{
				s = ChatColor.GREEN+"";	
			}
		}else{
			s = ChatColor.GREEN+"§a";
		}
		return s;
	}
	
}