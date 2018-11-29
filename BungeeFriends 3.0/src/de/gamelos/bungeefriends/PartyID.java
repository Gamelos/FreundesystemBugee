package de.gamelos.bungeefriends;

import java.sql.ResultSet;
import java.sql.SQLException;


@SuppressWarnings("static-access")
public class PartyID {
public static boolean playerExists(String uuid){
		
		
		try {
			ResultSet rs = Main.mysql.querry("SELECT * FROM Partyid WHERE UUID = '"+ uuid + "'");
			
			if(rs.next()){
				return rs.getString("UUID") != null;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

public static boolean idExists(String id){
	
	
	try {
		ResultSet rs = Main.mysql.querry("SELECT * FROM Partyid WHERE ID = '"+ id + "'");
		
		if(rs.next()){
			return rs.getString("ID") != null;
		}
		return false;
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return false;
}
	
public static void createPlayer(String uuid, String id){
	if(!(playerExists(uuid))){
		Main.mysql.update("INSERT INTO Partyid(UUID, ID) VALUES ('" +uuid+ "', '"+id+"');");
	}
}
	
	//get-----------------------------------------------------------------------------------------------------------------------------------
	public static String getID(String uuid){
		String i = "";
		if(playerExists(uuid)){
			try {
				ResultSet rs = Main.mysql.querry("SELECT * FROM Partyid WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("ID")) == null));
				
				i = rs.getString("ID");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
		}
		return i;
	}
	
	
	
	//set-----------------------------------------------------------------------------------------------------------------------------------
	
public static void setID(String uuid, String id){
		
		if(playerExists(uuid)){
			Main.mysql.update("UPDATE Partyid SET ID= '" + id+ "' WHERE UUID= '" + uuid+ "';");
		}else{
			createPlayer(uuid, id);
			setID(uuid, id);
		}
	}

public static void removeSpieler(String UUID){
	Main.mysql.update("DELETE FROM Partyid WHERE UUID = '"+UUID+"'");
}
}
