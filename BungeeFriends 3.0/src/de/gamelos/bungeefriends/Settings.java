package de.gamelos.bungeefriends;

import java.sql.ResultSet;
import java.sql.SQLException;


@SuppressWarnings("static-access")
public class Settings {
public static boolean playerExists(String uuid){
		
		
		try {
			ResultSet rs = Main.mysql.querry("SELECT * FROM Settings WHERE UUID = '"+ uuid + "'");
			
			if(rs.next()){
				return rs.getString("jump") != null;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void createPlayer(String uuid){
		if(!(playerExists(uuid))){
			Main.mysql.update("INSERT INTO Settings(UUID, jump, msg, party, friend, status) VALUES ('" +uuid+ "', 'true', 'true', 'true', 'true', 'null');");
		}
	}
	
	//get-----------------------------------------------------------------------------------------------------------------------------------
	public static boolean getjump(String uuid){
		String i = "";
		if(playerExists(uuid)){
			try {
				ResultSet rs = Main.mysql.querry("SELECT * FROM Settings WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("jump")) == null));
				
				i = rs.getString("jump");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
		}
		if(i.equals("true")){
			return true;
		}
		return false;
	}
	public static boolean getmsg(String uuid){
		String i = "";
		if(playerExists(uuid)){
			try {
				ResultSet rs = Main.mysql.querry("SELECT * FROM Settings WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("msg")) == null));
				
				i = rs.getString("msg");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
		}
		if(i.equals("true")){
			return true;
		}
		return false;
	}
	public static boolean getparty(String uuid){
		String i = "";
		if(playerExists(uuid)){
			try {
				ResultSet rs = Main.mysql.querry("SELECT * FROM Settings WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("party")) == null));
				
				i = rs.getString("party");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
		}
		if(i.equals("true")){
			return true;
		}
		return false;
	}
	public static boolean getfriend(String uuid){
		String i = "";
		if(playerExists(uuid)){
			try {
				ResultSet rs = Main.mysql.querry("SELECT * FROM Settings WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("friend")) == null));
				
				i = rs.getString("friend");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
		}
		if(i.equals("true")){
			return true;
		}
		return false;
	}
	
	public static String getstatus(String uuid){
		String i = "";
		if(playerExists(uuid)){
			try {
				ResultSet rs = Main.mysql.querry("SELECT * FROM Settings WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("status")) == null));
				
				i = rs.getString("status");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
		}
		return i;
	}
	//set-----------------------------------------------------------------------------------------------------------------------------------
public static void setjump(String uuid, String status){
		if(playerExists(uuid)){
			Main.mysql.update("UPDATE Settings SET jump= '" + status+ "' WHERE UUID= '" + uuid+ "';");
		}else{
			createPlayer(uuid);
			setjump(uuid, status);
		}
	}
public static void setmsg(String uuid, String status){
	if(playerExists(uuid)){
		Main.mysql.update("UPDATE Settings SET msg= '" + status+ "' WHERE UUID= '" + uuid+ "';");
	}else{
		createPlayer(uuid);
		setmsg(uuid, status);
	}
}
public static void setparty(String uuid, String status){
	if(playerExists(uuid)){
		Main.mysql.update("UPDATE Settings SET party= '" + status+ "' WHERE UUID= '" + uuid+ "';");
	}else{
		createPlayer(uuid);
		setparty(uuid, status);
	}
}
public static void setfriend(String uuid, String status){
	if(playerExists(uuid)){
		Main.mysql.update("UPDATE Settings SET friend= '" + status+ "' WHERE UUID= '" + uuid+ "';");
	}else{
		createPlayer(uuid);
		setfriend(uuid, status);
	}
}

public static void setstatus(String uuid, String status){
	if(playerExists(uuid)){
		Main.mysql.update("UPDATE Settings SET status= '" + status+ "' WHERE UUID= '" + uuid+ "';");
	}else{
		createPlayer(uuid);
		setstatus(uuid, status);
	}
}
}
