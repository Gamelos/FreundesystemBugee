package de.gamelos.bungeefriends;

import java.sql.ResultSet;
import java.sql.SQLException;


@SuppressWarnings("static-access")
public class Online {
public static boolean playerExists(String uuid){
		
		
		try {
			ResultSet rs = Main.mysql.querry("SELECT * FROM Online WHERE UUID = '"+ uuid + "'");
			
			if(rs.next()){
				return rs.getString("UUID") != null;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
public static void createPlayer(String uuid, String servername){
	if(!(playerExists(uuid))){
		Main.mysql.update("INSERT INTO Online(UUID, Servername) VALUES ('" +uuid+ "', '"+servername+"');");
	}
}
	
	//get-----------------------------------------------------------------------------------------------------------------------------------
	public static String getServer(String uuid){
		String i = "";
		if(playerExists(uuid)){
			try {
				ResultSet rs = Main.mysql.querry("SELECT * FROM Online WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("Servername")) == null));
				
				i = rs.getString("Servername");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
		}
		return i;
	}
	
	
	
	//set-----------------------------------------------------------------------------------------------------------------------------------
	
public static void setServer(String uuid, String servername){
		
		if(playerExists(uuid)){
			Main.mysql.update("UPDATE Online SET Servername= '" + servername+ "' WHERE UUID= '" + uuid+ "';");
		}else{
		}
	}

public static void removeSpieler(String UUID){
	Main.mysql.update("DELETE FROM Online WHERE UUID = '"+UUID+"'");
}
}
