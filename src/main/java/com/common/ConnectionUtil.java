package com.common;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ConnectionUtil {
	
	private static String RESOURCE = "java:/comp/env";
	private static  String RESOURCE_NAME = "jdbc/oracle";
	
	public static DataSource getDataSource() {
		DataSource dataSource = null; 
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup(RESOURCE);
			dataSource = (DataSource) envContext.lookup(RESOURCE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataSource; 
	}
}
