

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class customer {
	static Scanner sc = new Scanner(System.in);
	static final String jdbcURL="jdbc:oracle:thin:@fourierXXXXX";
    static final String User_name="XXXXX";
    static final String password="XXXXX";
    /**
     * get customer store balance when customer ID is known
     * @param customer id
     * @return customer balance
     */
    public static int getCustBl(int id){
    	int custBl = 0;
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
		try {
    	    Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
    	    String sql=("select balance from customer where customer_id=?");
    	    PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            ResultSet rset=pStmt.executeQuery();
    	    while(rset.next()){
    	        custBl = rset.getInt("balance");
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
    	return custBl; 
    }
    /**
     * get customer first name when customer ID is known
     * used in the greeting sentences
     * @param customer id
     * @return customer first name
     */
    public static String getCustNm(int id){
    	String custNm = null;
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
		try {
    	    Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
    	    String sql=("select firstname from Customer where customer_id=?");
    	    PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            ResultSet rset=pStmt.executeQuery();
    	    while(rset.next()){
    	        custNm = rset.getString("firstname");
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
    	return custNm;    			
    }
    /**
     * get customer password when customer ID is known
     * used to check if customer inputed a right password
     * 
     * @param customer id
     * @return customer password
     */
    public static String getCustPw(int id){
    	String custPw = null; 
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
  		try {
			Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
			String sql=("select password from Customer where customer_id=?");
    	    PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            ResultSet rset=pStmt.executeQuery();
    	    while(rset.next()){
    	        custPw = rset.getString("password");
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
    	return custPw;    			
    }
    /**
     * check if this customer id in the databases
     * @param customer id
     * @return if id exist return true, else false
     */
    public static boolean getAvailable(int id){
    	boolean custIdAva = true;
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
		try {
    	    Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
    	    String sql=("select count(*) from customer where customer_id=?");
    	    PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            ResultSet rset=pStmt.executeQuery();
    	    int temp = 0;
    	    while(rset.next()){
    	        temp = rset.getInt(1);
    	    }
    	    if (temp == 0) 
    	    	custIdAva = false;   	    
    	    conn.close();
    	    try {
			    Thread.sleep(1000);
		    } catch (InterruptedException e) {
		    	System.out.println("Exception:"+ e);
		    }
		} catch (SQLException e) {
			System.out.println("Exception:"+ e);
		}
    	return custIdAva; 
    }
    /**
     * visitor can register
     * customer will be granted a customer ID automatically
     * the password will be asked to be inputed twice to double check
     * 
     */
    public static void register(){
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
		try {
    	    Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
            Statement stat = conn.createStatement();
            ResultSet rset=stat.executeQuery("select max(customer_id) from customer");
    	    int id=0;
    	    while(rset.next()){
    	        id = rset.getInt(1)+1;
    	    }
    	    System.out.println("Please input your first name:");
    	    String first= sc.next();
    	    System.out.println("Please input your middle name:");
    	    String middle= sc.next();
    	    System.out.println("Please input your last name:");
    	    String last= sc.next();
    	    boolean flag=true;
    	    String mypassword=null;
    	    do{
    	        System.out.println("Please input a password:");
    	        mypassword= sc.next();
    	        System.out.println("Please input password again:");
    	        String check= sc.next();
    	        if (!mypassword.equals(check)){
    	        	System.out.println("Wrong input! Please input your password again!"+"\n");
    	        }
    	        else
    	            flag=false;
    	    }while(flag);
    	    String sql=("insert into customer values(?,?,?,?,0,?)");
    	    PreparedStatement pStmt = conn.prepareStatement(sql);
    	    pStmt.setInt(1, id);
    	    pStmt.setString(2, first);
    	    pStmt.setString(3, middle);
    	    pStmt.setString(4, last);
    	    pStmt.setString(5, mypassword);
    	    pStmt.executeUpdate();
    	    System.out.println("Congratulations! Your account has been created!");  
    	    System.out.println("Your customer ID is "+id+". Please use your ID and password to login!"+"\n");
    	    conn.close();
    	    try {
			    Thread.sleep(1000);
		    } catch (InterruptedException e) {
		    	System.out.println("Exception:"+ e);
		    }
		} catch (SQLException e) {
			System.out.println("Exception:"+ e);
		}
    	return; 
    }
    /**
     * customers can change password when they remember the old one
     * @param customer id
     */
    public static void password(int id){
	    String newPd=null;
	    while(true){
	        System.out.println("Please enter your new password:");
	        newPd= sc.next();
	        System.out.println("Please enter your new password again:");
	        String check= sc.next();
	        if (!newPd.equals(check)){
	        	System.out.println("Wrong input! Please enter your password again!"+"\n");
	        }
	        else
	            break;
	    }
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
		try {
    	    Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
    	    String sql=("update customer set password=? where customer_id=?");
    	    PreparedStatement pStmt = conn.prepareStatement(sql);
    	    pStmt.setString(1, newPd);
            pStmt.setInt(2, id);
            pStmt.executeUpdate();
            System.out.println("Your password updated! Nexttime please use new password to login!"+"\n");
            conn.close();
    	    try {
			    Thread.sleep(1000);
		    } catch (InterruptedException e) {
		    	System.out.println("Exception:"+ e);
		    }
		} catch (SQLException e) {
			System.out.println("Exception:"+ e);
		}
    	return; 
    }
}
