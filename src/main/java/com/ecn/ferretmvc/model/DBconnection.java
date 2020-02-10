package com.ecn.ferretmvc.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class DBconnection {
	public static Connection conn = null;
	public DBconnection (Connection conn){
		DBconnection.conn = conn;
	}
	
	public static void Connection () {
	
	// Connecting to the database
	String url = "jdbc:postgresql://ser-info-03.ec-nantes.fr:5432/Ferret_data";
	//String url = "jdbc:postgresql://localhost:5432/Ferret_data";
	//String user = "postgres";
	String user = "ferret";
	String passwd = "Ferret.1";
	DBconnection.conn = null;
	try {
		Class.forName("org.postgresql.Driver");
		DBconnection.conn = DriverManager.getConnection(url, user, passwd);
		System.out.println("J'affiche conn" + DBconnection.conn);
	} catch (ClassNotFoundException e1) {
		e1.printStackTrace();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	// Verifying that connection is OK
	if (DBconnection.conn == null) {
		JOptionPane.showMessageDialog(null, "Connection to database failed", "Warning",
				JOptionPane.ERROR_MESSAGE);
	}
	}
}
