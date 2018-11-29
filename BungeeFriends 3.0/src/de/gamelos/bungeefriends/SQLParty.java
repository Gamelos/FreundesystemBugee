package de.gamelos.bungeefriends;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("static-access")
public class SQLParty {
public static boolean idExists(String id){
		try {
			ResultSet rs = Main.mysql.querry("SELECT * FROM Party WHERE ID = '"+ id + "'");
			
			if(rs.next()){
				return rs.getString("ID") != null;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
public static void createParty(String id, String LEADER, String PARTYLIST, String PRIVATE, String TYPE){
	if(!(idExists(id))){
		Main.mysql.update("INSERT INTO Party(ID, PARTYLIST, LEADER, MODLIST, PRIVATE, TYPE) VALUES ('" +id+ "', '"+PARTYLIST+"', '"+LEADER+"', '', '"+PRIVATE+"', '"+TYPE+"');");
	}
}
	
	//get-----------------------------------------------------------------------------------------------------------------------------------
	public static String getPartylist(String id){
		String i = "";
		if(idExists(id)){
			try {
				ResultSet rs = Main.mysql.querry("SELECT * FROM Party WHERE ID = '"+ id + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("PARTYLIST")) == null));
				
				i = rs.getString("PARTYLIST");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
		}
		return i;
	}
	
	public static String getLeader(String id){
		String i = "";
		if(idExists(id)){
			try {
				ResultSet rs = Main.mysql.querry("SELECT * FROM Party WHERE ID = '"+ id + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("LEADER")) == null));
				
				i = rs.getString("LEADER");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
		}
		return i;
	}
	
	public static String getModlist(String id){
		String i = "";
		if(idExists(id)){
			try {
				ResultSet rs = Main.mysql.querry("SELECT * FROM Party WHERE ID = '"+ id + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("MODLIST")) == null));
				
				i = rs.getString("MODLIST");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
		}
		return i;
	}
	
	public static boolean getPrivate(String id){
		String i = "";
		if(idExists(id)){
			try {
				ResultSet rs = Main.mysql.querry("SELECT * FROM Party WHERE ID = '"+ id + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("PRIVATE")) == null));
				
				i = rs.getString("PRIVATE");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
		}
		if(i.equals("true")) {
			return true;
		}else {
			return false;
		}
	}
	
	public static String getType(String id){
		String i = "";
		if(idExists(id)){
			try {
				ResultSet rs = Main.mysql.querry("SELECT * FROM Party WHERE ID = '"+ id + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("TYPE")) == null));
				
				i = rs.getString("TYPE");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
		}
		return i;
	}

	
//	public static List<String> Anfragenliste(String uuid){
//		List<String> list = new ArrayList<>();
//		String s = getAnfragen(uuid);
//		String[] a = s.split(",");
//		for(String ss : a) {
//			if(ss.length()>0) {
//			list.add(ss);
//			}
//		}
//		return list;
//	}
	
	//set-----------------------------------------------------------------------------------------------------------------------------------
	
public static void setPartylist(String id, String partylist){
		
		if(idExists(id)){
			Main.mysql.update("UPDATE Party SET PARTYLIST= '" + partylist+ "' WHERE ID= '" + id+ "';");
		}else{
		}
	}

public static void setLeader(String id, String leader){
	
	if(idExists(id)){
		Main.mysql.update("UPDATE Party SET LEADER= '" + leader+ "' WHERE ID= '" + id+ "';");
	}else{
	}
}

public static void setModlist(String id, String modlist){
	
	if(idExists(id)){
		Main.mysql.update("UPDATE Party SET MODLIST= '" + modlist+ "' WHERE ID= '" + id+ "';");
	}else{
	}
}

public static void setPrivate(String id, Boolean b){
	String s = "true";
	if(b) {
		s = "true";
	}else {
		s = "false";
	}
	
	if(idExists(id)){
		Main.mysql.update("UPDATE Party SET PRIVATE= '" + s+ "' WHERE ID= '" + id+ "';");
	}else{
	}
}

public static void setType(String id, String type){
	
	if(idExists(id)){
		Main.mysql.update("UPDATE Party SET TYPE= '" + type+ "' WHERE ID= '" + id+ "';");
	}else{
	}
}

//public static void setfriendlist(String uuid, List<String> list) {
//	String s = "";
//	for(String ss:list) {
//		s = s+ss+",";
//	}
//	setFreundesliste(uuid, s);
//}

//===========================================================================================================

public static List<String> StringToList(String s){
	List<String> list = new ArrayList<>();
	String[] a = s.split(",");
	for(String ss : a) {
		if(ss.length()>0) {
		list.add(ss);
		}
	}
	return list;
}

public static String ListToString(List<String> list) {
	String s = "";
	for(String ss:list) {
		s = s+ss+",";
	}
	return s;
}

public static void removeParty(String id){
	Main.mysql.update("DELETE FROM Party WHERE ID = '"+id+"'");
}

}

