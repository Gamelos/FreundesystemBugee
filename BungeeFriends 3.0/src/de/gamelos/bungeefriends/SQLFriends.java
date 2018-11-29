package de.gamelos.bungeefriends;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("static-access")
public class SQLFriends {
public static boolean playerExists(String uuid){
		try {
			ResultSet rs = Main.mysql.querry("SELECT * FROM Frienddata WHERE UUID = '"+ uuid + "'");
			
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
		Main.mysql.update("INSERT INTO Frienddata(UUID, Freundesliste, Anfragen) VALUES ('" +uuid+ "', 'null', 'null');");
	}
}
	
	//get-----------------------------------------------------------------------------------------------------------------------------------
	public static String getFreundesliste(String uuid){
		String i = "";
		if(playerExists(uuid)){
			try {
				ResultSet rs = Main.mysql.querry("SELECT * FROM Frienddata WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("Freundesliste")) == null));
				
				i = rs.getString("Freundesliste");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
		}
		return i;
	}
	
	public static String getAnfragen(String uuid){
		String i = "";
		if(playerExists(uuid)){
			try {
				ResultSet rs = Main.mysql.querry("SELECT * FROM Frienddata WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("Anfragen")) == null));
				
				i = rs.getString("Anfragen");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
		}
		return i;
	}
	
	public static List<String> friendlist(String uuid){
		List<String> list = new ArrayList<>();
		String s = getFreundesliste(uuid);
		String[] a = s.split(",");
		for(String ss : a) {
			if(ss.length()>0) {
				if(!ss.equals("null")) {
			list.add(ss);
				}
			}
		}
		return list;
	}
	
	public static List<String> Anfragenliste(String uuid){
		List<String> list = new ArrayList<>();
		String s = getAnfragen(uuid);
		System.out.println(s);
		String[] a = s.split(",");
		for(String ss : a) {
			if(ss.length()>0) {
			if(!ss.equals("null")) {
			System.out.println(ss);
			list.add(ss);
			}
			}
		}
		return list;
	}
	
	//set-----------------------------------------------------------------------------------------------------------------------------------
	
public static void setFreundesliste(String uuid, String freundesliste){
		
		if(playerExists(uuid)){
			Main.mysql.update("UPDATE Frienddata SET Freundesliste= '" + freundesliste+ "' WHERE UUID= '" + uuid+ "';");
		}else{
			createPlayer(uuid);
			setFreundesliste(uuid, freundesliste);
		}
	}

public static void setAnfragen(String uuid, String anfragenliste){
	
	if(playerExists(uuid)){
		Main.mysql.update("UPDATE Frienddata SET Anfragen= '" + anfragenliste+ "' WHERE UUID= '" + uuid+ "';");
	}else{
		createPlayer(uuid);
		setAnfragen(uuid, anfragenliste);
	}
}

public static void setfriendlist(String uuid, List<String> list) {
	String s = "";
	for(String ss:list) {
		s = s+ss+",";
	}
	setFreundesliste(uuid, s);
}

public static void setanfragen(String uuid, List<String> list) {
	String s = "";
	for(String ss:list) {
		s = s+ss+",";
	}
	setAnfragen(uuid, s);
}

}
