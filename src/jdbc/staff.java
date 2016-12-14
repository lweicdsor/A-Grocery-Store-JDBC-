

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class staff {
	
	static final String jdbcURL="jdbc:oracle:thin:@fourierXXXXX";
    static final String User_name="XXXXX";
    static final String password="XXXXX";
    /**
     * check if the staff id is in the databases
     * @param staff id
     * @return if id in the databases return true, else return false
     */
    public static boolean getAvailable(int id){
    	boolean staffIdAva = true;
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
		try {
    	    Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
    	    String sql=("select count(*) from staff where staff_id=?");
    	    PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            ResultSet rset=pStmt.executeQuery();
    	    int temp = 0;
    	    while(rset.next()){
    	        temp = rset.getInt(1);
    	    }
    	    if (temp == 0) 
    	    	staffIdAva = false;   	    
    	    conn.close();
    	    try {
			    Thread.sleep(1000);
		    } catch (InterruptedException e) {
		    	System.out.println("Exception:"+ e);
		    }
		} catch (SQLException e) {
			System.out.println("Exception:"+ e);
		}
    	return staffIdAva; 
    }
    /**
     * get staff first name when staff id is known
     * used for greeting sentences
     * @param staff id
     * @return staff first name
     */
    public static String getStaffNm(int id){
    	String staffNm = null;
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
		try {
    	    Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
    	    String sql=("select firstname from staff where staff_id=?");
    	    PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            ResultSet rset=pStmt.executeQuery();
    	    while(rset.next()){
    	        staffNm = rset.getString("firstname");
    	    }
    	    conn.close();
    	    try {
			    Thread.sleep(1000);
		    } catch (InterruptedException e) {
		    	System.out.println("Exception:"+ e);
		    }
		} catch (SQLException e) {
			System.out.println("Exception:"+ e);
		}
    	return staffNm;    			
    }
    /**
     * get staff password when staff id is known
     * used for check if staff inputed a right password
     * @param staff id
     * @return staff password
     */
    public static String getStaffPw(int id){
    	String staffPw = null; 
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
  		try {
			Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
			String sql=("select password from staff where staff_id=?");
    	    PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            ResultSet rset=pStmt.executeQuery();
    	    while(rset.next()){
    	        staffPw = rset.getString("password");
    	    }
    	    conn.close();
    	    try {
			    Thread.sleep(1000);
		    } catch (InterruptedException e) {
		    	System.out.println("Exception:"+ e);
		    }
		} catch (SQLException e) {
			System.out.println("Exception:"+ e);
		}
    	return staffPw;    			
    }
    /**
     * get staff title when staff id is known
     * used to get this staff's authority limit and also for greeting sentences
     * @param id
     * @return
     */
    public static String getStaffTitle(int id){
    	String staffTt = null; 
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
  		try {
			Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
			String sql=("select job_title from staff where staff_id=?");
			PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            ResultSet rset=pStmt.executeQuery();
			while(rset.next()){
    	        staffTt = rset.getString("job_title");
    	    }
    	    conn.close();
    	    try {
			    Thread.sleep(1000);
		    } catch (InterruptedException e) {
		    	System.out.println("Exception:"+ e);
		    }
		} catch (SQLException e) {
			System.out.println("Exception:"+ e);
		}
    	return staffTt;    			
    }
}
