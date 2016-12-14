

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class deliveryaddress {
	
	static Scanner sc = new Scanner(System.in);
	static final String jdbcURL="jdbc:oracle:thin:@fourierXXXXX";
    static final String User_name="XXXXX";
    static final String password="XXXXX";
    
    public static void add(int id){

	    System.out.println("Please enter street number: ");
	    int streetNu = sc.nextInt();
	    System.out.println("Please enter street name: ");
	    String streetNm = sc.next();
	    System.out.println("Please enter APT number: ");
	    int aptNu = sc.nextInt();
	    System.out.println("Please enter zipcode: ");
	    int zipCode = sc.nextInt();
    	
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
		try {
			Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
			/**
			 * check if the delivery address has already been in the databases
			 */
			String sql=("select count(*) from delivery_address where street_number=? and street_name=? and apt_number=? and zipcode=? and customer_id=?");
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, streetNu);
            pStmt.setString(2, streetNm);
	    	pStmt.setInt(3, aptNu);
	    	pStmt.setInt(4, zipCode);
	    	pStmt.setInt(5, id);
            ResultSet rset=pStmt.executeQuery();
            int count=0;
	        if(rset.next()){
		        count = rset.getInt(1);
	        }
	        if (count != 0){
	        	System.out.println("This address has already been in your account. Please turn back and choose to edit it!" + "\n");
	        	return;
	        }
	        
		    System.out.println("Please enter city: ");
		    String city = sc.next();
		    System.out.println("Please enter state (two-letter abbreviated code): ");
		    String state = sc.next();
			/**
			 * check if this new address has already in the address table
			 * if not, insert this shipping address into table 'address' first
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
	        /**
	         * change old primary shipping address to normal
	         * the new added one will be the new primary address
	         */
	        sql=("update delivery_address set primary='No' where primary='Yes' and customer_id=?");
            pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            pStmt.executeUpdate();
	        
    	    sql="Insert into delivery_address values(?,?,?,?,?,'Yes')";
    	    pStmt = conn.prepareStatement(sql);
    	    pStmt.setInt(1, id);
    	    pStmt.setInt(2, streetNu);
	    	pStmt.setString(3, streetNm);
	    	pStmt.setInt(4, aptNu);
	    	pStmt.setInt(5, zipCode);
    	    pStmt.executeUpdate();
    	
    	    System.out.println("New shipping address added!"+"\n");
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

    	System.out.println("---Please enter the current delivery address information---" + "\n");
	    System.out.println("Street number: ");
	    int oStreetNu = sc.nextInt();
	    System.out.println("Street name: ");
	    String oStreetNm = sc.next();
	    System.out.println("APT number: ");
	    int oAptNu = sc.nextInt();
	    System.out.println("Zipcode: ");
	    int oZipCode = sc.nextInt();
	    
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
		try {
			Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
			/**
			 * check if the delivery address has already been in the databases
			 */
			String sql=("select count(*) from delivery_address where customer_id=? and street_number=? and street_name=? and apt_number=? and zipcode=?");
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            pStmt.setInt(2, oStreetNu);
	    	pStmt.setString(3, oStreetNm);
	    	pStmt.setInt(4, oAptNu);
	    	pStmt.setInt(5, oZipCode);
            ResultSet rset=pStmt.executeQuery();
            int count=0;
	        if(rset.next()){
		        count = rset.getInt(1);
	        }
	        if (count == 0){
	    	    System.out.println("\n" + "This address has not been added in your account. Please turn back and choose to add it!" + "\n");
	    	    return;
	        }
			/**
			 * keep the primary information of the address under updating
			 * if customer want to change the primary information, they must do it in the method modifyPrimary
			 */
	        sql=("select primary from delivery_address where customer_id=? and street_number=? and street_name=? and apt_number=? and zipcode=?");
	        pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            pStmt.setInt(2, oStreetNu);
	    	pStmt.setString(3, oStreetNm);
	    	pStmt.setInt(4, oAptNu);
	    	pStmt.setInt(5, oZipCode);
            rset=pStmt.executeQuery();
            String isPrimary="No";
	        if(rset.next()){
		        isPrimary = rset.getString(1);
	        }
	        
			sql="delete from delivery_address where customer_id=? and street_number=? and street_name=? and apt_number=? and zipcode=?";
		    pStmt = conn.prepareStatement(sql);
		    pStmt.setInt(1, id);
		    pStmt.setInt(2, oStreetNu);
	    	pStmt.setString(3, oStreetNm);
	    	pStmt.setInt(4, oAptNu);
	    	pStmt.setInt(5, oZipCode);
    	    pStmt.executeUpdate();
    	    
        	System.out.println("\n" + "---Please enter the new delivery address information---"+"\n");
    	    System.out.println("Street number: ");
    	    int nStreetNu = sc.nextInt();
    	    System.out.println("Street name: ");
    	    String nStreetNm = sc.next();
    	    System.out.println("APT number: ");
    	    int nAptNu = sc.nextInt();
    	    System.out.println("Zipcode: ");
    	    int nZipCode = sc.nextInt();
    	    System.out.println("City: ");
    	    String nCity = sc.next();
    	    System.out.println("State (two-letter abbreviated code): ");
    	    String nState = sc.next();
    	    /**
			 * check if this new address has already in the address table
			 * if not, insert this shipping address into table 'address' first
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
    	
	        sql="Insert into delivery_address values(?,?,?,?,?,?)";
		    pStmt = conn.prepareStatement(sql);
		    pStmt.setInt(1, id);
		    pStmt.setInt(2, nStreetNu);
	    	pStmt.setString(3, nStreetNm);
	    	pStmt.setInt(4, nAptNu);
	    	pStmt.setInt(5, nZipCode);
	    	pStmt.setString(6, isPrimary);
    	    pStmt.executeUpdate();
	        
    	    System.out.println("Shipping address updated!"+"\n");
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
    
    public static void modifyPrimary(int id){

    	System.out.println("---Please enter the delivery address which you want to set to primary---" + "\n");
	    System.out.println("Street number: ");
	    int oStreetNu = sc.nextInt();
	    System.out.println("Street name: ");
	    String oStreetNm = sc.next();
	    System.out.println("APT number: ");
	    int oAptNu = sc.nextInt();
	    System.out.println("Zipcode: ");
	    int oZipCode = sc.nextInt();
	    
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
		try {
			Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
			/**
			 * check if the delivery address has already been in the databases
			 */
			String sql=("select count(*) from delivery_address where customer_id=? and street_number=? and street_name=? and apt_number=? and zipcode=?");
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            pStmt.setInt(2, oStreetNu);
	    	pStmt.setString(3, oStreetNm);
	    	pStmt.setInt(4, oAptNu);
	    	pStmt.setInt(5, oZipCode);
            ResultSet rset=pStmt.executeQuery();
            int count=0;
	        if(rset.next()){
		        count = rset.getInt(1);
	        }
	        if (count == 0){
	    	    System.out.println("\n" + "This address has not been added in your account. Please turn back and choose to add it!" + "\n");
	    	    return;
	        }
	        
	        sql=("update delivery_address set primary='No' where primary='Yes' and customer_id=?");
            pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            pStmt.executeUpdate();
            
	        sql=("update delivery_address set primary='Yes' where customer_id=? and street_number=? and street_name=? and apt_number=? and zipcode=?");
	        pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            pStmt.setInt(2, oStreetNu);
	    	pStmt.setString(3, oStreetNm);
	    	pStmt.setInt(4, oAptNu);
	    	pStmt.setInt(5, oZipCode);
            pStmt.executeUpdate();
	        
            System.out.println("Primary setting has already updated!"+"\n");
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
    
    public static void delete(int id){

    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}

		try {
			Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
			/**
			 * check if the address is the only shipping address
			 * if yes, the shipping address could not be removed, as customers must have at least one shipping address
			 */
			String sql=("select count(*) from delivery_address where customer_id=? "); 
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, id);
	        ResultSet rset=pStmt.executeQuery();
			int temp = 0;
    	    while(rset.next()){
    	    temp = rset.getInt(1);
    	    }
    	    if (temp == 1){
    	    	System.out.println("\n" + "You only have one shipping address in your account now. Please turn back and choose to edit it!" + "\n");
    		    return;
    	    }
  	    
        	System.out.println("---Please enter the shipping address you want to delete---" + "\n");
    	    System.out.println("Street number: ");
    	    int streetNu = sc.nextInt();
    	    System.out.println("Street name: ");
    	    String streetNm = sc.next();
    	    System.out.println("APT number: ");
    	    int aptNu = sc.nextInt();
    	    System.out.println("Zipcode: ");
    	    int zipCode = sc.nextInt();
    	    /**
			 * check if the delivery address has already been in the databases
			 */
    	    sql=("select count(*) from delivery_address where customer_id=? and street_number=? and street_name=? and apt_number=? and zipcode=?");
            pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            pStmt.setInt(2, streetNu);
	    	pStmt.setString(3, streetNm);
	    	pStmt.setInt(4, aptNu);
	    	pStmt.setInt(5, zipCode);
            rset=pStmt.executeQuery();
            int count=0;
	        if(rset.next()){
		        count = rset.getInt(1);
	        }
	        if (count == 0){
	    	    System.out.println("\n" + "This address has not been added in your account!" + "\n");
	    	    return;
	        }
    	    /**
    	     * check if the address is primary
    	     * if yes, the address should first be changed to be not primary. then to be deleted
    	     */
	        sql=("select primary from delivery_address where customer_id=? and street_number=? and street_name=? and apt_number=? and zipcode=?");
            pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            pStmt.setInt(2, streetNu);
	    	pStmt.setString(3, streetNm);
	    	pStmt.setInt(4, aptNu);
	    	pStmt.setInt(5, zipCode);
            rset=pStmt.executeQuery();
            String isPrimary="No";
	        if(rset.next()){
		        isPrimary = rset.getString(1);
	        }

	        if (isPrimary.equals("Yes")){
	    	    System.out.println("\n" + "This address is your primary address and can not be deleted!");
	    	    System.out.println("Please turn back and edit primary setting!" + "\n");
	    	    return;
	        }
	        
    	    sql=("delete from delivery_address where customer_id=? and street_number=? and street_name=? and apt_number=? and zipcode=?");
    	    pStmt = conn.prepareStatement(sql);
    	    pStmt.setInt(1, id);
    	    pStmt.setInt(2, streetNu);
    	    pStmt.setString(3, streetNm);
    	    pStmt.setInt(4, aptNu);
    	    pStmt.setInt(5, zipCode);
	        pStmt.executeUpdate();
    	
	        System.out.println("Shipping address deleted!"+"\n");
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
