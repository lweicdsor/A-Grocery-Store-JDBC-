

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class creditcard {
	
	static Scanner sc = new Scanner(System.in);
	static final String jdbcURL="jdbc:oracle:thin:@fourierXXXXX";
    static final String User_name="XXXXX";
    static final String password="XXXXX";
    
    public static void add(int id){
    	
	    System.out.println("Please enter card number: ");
	    long cardNu= sc.nextLong();
    	
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}

		try {
			Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
			/**
			 * check whether the card has already been in the databases.
			 */
			String sql=("select count(*) from creditcard where card_number=?");
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setLong(1, cardNu);
            ResultSet rset=pStmt.executeQuery();
            int count=0;
	        if(rset.next()){
		        count = rset.getInt(1);
	        }
	        if (count != 0){
	        	System.out.println("This card has already been in your account. Please turn back and choose to edit it!" + "\n");
	        	return;
	        }
			
	        System.out.println("Please enter bank name: ");
		    String bank = sc.next();
		    System.out.println("Please enter expiration date (e.g. 31-Dec-19): ");
		    String expirationDt = sc.next();
		    System.out.println("\n" + "---Please enter the billing address---"+ "\n");
		    System.out.println("Please enter street number: ");
		    int streetNu = sc.nextInt();
		    System.out.println("Please enter street name: ");
		    String streetNm = sc.next();
		    System.out.println("Please enter APT number: ");
		    int aptNu = sc.nextInt();
		    System.out.println("Please enter zipcode: ");
		    int zipCode = sc.nextInt();
		    System.out.println("Please enter city: ");
		    String city = sc.next();
		    System.out.println("Please enter state (two-letter abbreviated code): ");
		    String state = sc.next();
			/**
			 * if the parent table 'address' don't contain this card's billing address, 
			 * first insert its billing address into table 'address'.
			 * 
			 */
			sql=("select count(*) from address where street_number=? and street_name=? and apt_number=? and zipcode=?");
            pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, streetNu);
            pStmt.setString(2, streetNm);
	    	pStmt.setInt(3, aptNu);
	    	pStmt.setInt(4, zipCode);
            rset=pStmt.executeQuery();
	        if(rset.next()){
		        count = rset.getInt(1);
	        }
	        if (count == 0){
	        	sql="Insert into address values(?,?,?,?,?,?)";
	    	    pStmt = conn.prepareStatement(sql);
	    	    pStmt.setInt(1, streetNu);
		    	pStmt.setString(2, streetNm);
		    	pStmt.setInt(3, aptNu);
		    	pStmt.setInt(4, zipCode);
		    	pStmt.setString(5, city);
		    	pStmt.setString(6, state);
	    	    pStmt.executeUpdate();
	        }
    	    
    	    sql="Insert into creditcard values(?,?,?,?,?,?,?,?)";
    	    pStmt = conn.prepareStatement(sql);
    	    pStmt.setLong(1, cardNu);
	    	pStmt.setString(2, bank);
	    	pStmt.setString(3, expirationDt);
	    	pStmt.setInt(4, id);
	    	pStmt.setInt(5, streetNu);
	    	pStmt.setString(6, streetNm);
	    	pStmt.setInt(7, aptNu);
	    	pStmt.setInt(8, zipCode);
    	    pStmt.executeUpdate();
    	
    	    System.out.println("New card added!"+"\n");
    	    conn.close();
    	    
    	    try {
			    Thread.sleep(1000);
		    } catch (InterruptedException e) {
		    	System.out.println("Exception:"+ e);
		    } 
		} catch (SQLException e) {
			System.out.println("Exception:"+ e);
		}
    }
    
    public static void modify(int id){
    	
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
    	
		try {
			System.out.println("---Please enter the card number---"+"\n");
            System.out.println("Card number: ");
            long cardNu = sc.nextLong();
            Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
            /**
             * check if the card information has already in the databases
             * 
             */
	        String sql=("select count(*) from creditcard where card_number=?");
	        PreparedStatement pStmt = conn.prepareStatement(sql);
	        pStmt.setLong(1, cardNu);
	        ResultSet rset=pStmt.executeQuery();
	        int count=0;
    	    if(rset.next()){
    		    count = rset.getInt(1);
    	    }
    	    if (count == 0){
    	        System.out.println("This card has not been added in your account. Please turn back and choose to add it" + "\n");
    	    	return;
    	    }
    	    
	        System.out.println("\n" + "---Please enter the new card information---" + "\n");
	        System.out.println("Bank name: ");
	        String nBank = sc.next();
	        System.out.println("Expiration date (e.g. 31-Dec-19): ");
	        String nExpirationDt = sc.next();
	        System.out.println("\n" + "---Please enter the new billing address of this card---" + "\n");
	        System.out.println("Street number: ");
	        int nStreetNu = sc.nextInt();
	        System.out.println("Street name: ");
	        String nStreetNm = sc.next();
	        System.out.println("APT number: ");
	        int nAptNu = sc.nextInt();
	        System.out.println("Zipcode: ");
	        int nZipCode = sc.nextInt();
	        System.out.print("City: ");
	        String nCity = sc.next();
	        System.out.print("State (two-letter abbreviated code): ");
	        String nState = sc.next();
    	    /**
    	     * check whether the new billing address (if changed) has already in the table 'address'
    	     * If not, first insert this billing address into table 'address'
    	     * 
    	     */
    	    sql=("select count(*) from address where street_number=? and street_name=? and apt_number=? and zipcode=?");
            pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, nStreetNu);
            pStmt.setString(2, nStreetNm);
	    	pStmt.setInt(3, nAptNu);
	    	pStmt.setInt(4, nZipCode);
            rset=pStmt.executeQuery();
	        if(rset.next()){
		        count = rset.getInt(1);
	        }
	        if (count == 0){
	        	sql="Insert into address values(?,?,?,?,?,?)";
	    	    pStmt = conn.prepareStatement(sql);
	    	    pStmt.setInt(1, nStreetNu);
		    	pStmt.setString(2, nStreetNm);
		    	pStmt.setInt(3, nAptNu);
		    	pStmt.setInt(4, nZipCode);
		    	pStmt.setString(5, nCity);
		    	pStmt.setString(6, nState);	
	    	    pStmt.executeUpdate();
	        }
    	
	        sql="update creditcard set bank=?, expiration_date=?, street_number=?, street_name=?, apt_number=?, zipcode=? where card_number=?";
    	    pStmt = conn.prepareStatement(sql);
    	    pStmt.setString(1, nBank);
	    	pStmt.setString(2, nExpirationDt);
	    	pStmt.setInt(3, nStreetNu);
	    	pStmt.setString(4, nStreetNm);
	    	pStmt.setInt(5, nAptNu);
	    	pStmt.setInt(6, nZipCode);
	    	pStmt.setLong(7, cardNu);
    	    pStmt.executeUpdate();
    	      	
    	    System.out.println("Card information updated!"+"\n");
    	    conn.close();
    	    try {
			    Thread.sleep(1000);
		    } catch (InterruptedException e) {
			    System.out.println("Exception:"+ e);
		    } 
		} catch (SQLException e) {
			System.out.println("Exception:"+ e);
		}
    }
    
    public static void delete(){
    	
	    System.out.println("Please enter card number: ");
	    long cardNu= sc.nextLong();
    	
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
		try {
			Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
            /**
             * check if the card information has already in the databases
             * 
             */
			String sql=("select count(*) from creditcard where card_number=?");
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setLong(1, cardNu);
            ResultSet rset=pStmt.executeQuery();
            int count=0;
	        if(rset.next()){
		        count = rset.getInt(1);
	        }
	        if (count == 0){
	    	    System.out.println("This card has not been added in your account!" + "\n");
	    	    return;
	        }

    	    sql="delete from creditcard where card_number=?";
    	    pStmt = conn.prepareStatement(sql);
    	    pStmt.setLong(1, cardNu);
    	    pStmt.executeUpdate();
    	    
    	    System.out.println("Card deleted!"+"\n");
    	    conn.close();
    	    try {
			    Thread.sleep(1000);
		    } catch (InterruptedException e) {
			    System.out.println("Exception:"+ e);
		    } 
		} catch (SQLException e) {
			System.out.println("Exception:"+ e);
		}
    }
}
