package de.gamelos.bungeefriends;

import java.sql.ResultSet;
import java.sql.SQLException;


@SuppressWarnings("static-access")
public class Freundesysteminfo {
public static boolean playerExists(String uuid){
		
		
		try {
			ResultSet rs = Main.mysql.querry("SELECT * FROM Freundesysteminfo WHERE UUID = '"+ uuid + "'");
			
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
		Main.mysql.update("INSERT INTO Freundesysteminfo(UUID, Seite, LastOnline) VALUES ('" +uuid+ "', '1', 'null');");
	}
}
	
	//get-----------------------------------------------------------------------------------------------------------------------------------
	public static String getSeite(String uuid){
		String i = "";
		if(playerExists(uuid)){
			try {
				ResultSet rs = Main.mysql.querry("SELECT * FROM Freundesysteminfo WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("Seite")) == null));
				
				i = rs.getString("Seite");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
		}
		return i;
	}
	
	public static String getLastOnline(String uuid){
		String i = "";
		if(playerExists(uuid)){
			try {
				ResultSet rs = Main.mysql.querry("SELECT * FROM Freundesysteminfo WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("LastOnline")) == null));
				
				i = rs.getString("LastOnline");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
		}
		return i;
	}
	
	
	
	//set-----------------------------------------------------------------------------------------------------------------------------------
	
public static void setSeite(String uuid, String seite){
		
		if(playerExists(uuid)){
			Main.mysql.update("UPDATE Freundesysteminfo SET Seite= '" + seite+ "' WHERE UUID= '" + uuid+ "';");
		}else{
			createPlayer(uuid);
			setSeite(uuid, seite);
		}
	}

public static void setLastOnline(String uuid, String data){
	
	if(playerExists(uuid)){
		Main.mysql.update("UPDATE Freundesysteminfo SET LastOnline= '" + data+ "' WHERE UUID= '" + uuid+ "';");
	}else{
		createPlayer(uuid);
		setSeite(uuid, data);
	}
}

}
