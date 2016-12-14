

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class product {
	static Scanner sc = new Scanner(System.in);
	static final String jdbcURL="jdbc:oracle:thin:@fourierXXXXX";
    static final String User_name="XXXXX";
    static final String password="XXXXX";
    /**
     * customer can check the product available amount in their shipping address's state
     * @param customer id
     */
    public static void search(int id){
    	
		while(true){
    	    System.out.println("Please enter the product name  (or enter Q to exit)");
    	    String productNm = sc.next();
            if (productNm.equals("Q")) break;
            System.out.println("Please enter the product category  (or enter Q to exit)");
    	    String productCt = sc.next();
    	    if (productCt.equals("Q")) break;
    	    try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
			} catch (ClassNotFoundException e) {
				System.out.println("Exception:"+ e);
			}
			try {
				Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
			    String sql=("Select state from delivery_address natural join address where customer_id=? and primary='Yes'");
			    PreparedStatement pStmt = conn.prepareStatement(sql);
			    pStmt.setInt(1, id);
			    ResultSet rset=pStmt.executeQuery();
			    String state=null;
			    while(rset.next()){
	    	        state = rset.getString(1);
	    	    }

			    sql=("select available_amount from address natural join warehouse natural join stock where state =? and product_name=? and product_category=?");
			    pStmt = conn.prepareStatement(sql);
			  	pStmt.setString(1, state);
			    pStmt.setString(2, productNm);
			  	pStmt.setString(3, productCt);
			    rset=pStmt.executeQuery();
			    int amount = 0;
			  	while(rset.next()){
			        amount = rset.getInt(1) + amount;
			      	    }
			  	System.out.println("Available amount: " + amount +"\n");
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
    /**
     * customer and visitor can browse all the products grouped by type
     */
    public static void browse(){

	    System.out.println("Our products grouped by its type: " + "\n");
    	try {
	        Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
		try {
			Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
			Statement stat = conn.createStatement();
        	ResultSet rset=stat.executeQuery("select product_name, product_category from product order by product_category"); 
        	while(rset.next()){
        		System.out.println(rset.getString("product_name")+" "+rset.getString("product_category"));
                }
        	System.out.println("\n");
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
    
    public static void add(){

    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}

		try {
		    System.out.println("Please enter product name: ");
		    String name = sc.next();
		    System.out.println("Please enter product category: ");
		    String category = sc.next();
		    /**
		     * check if the product has already in the database
		     */
			Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
			String sql=("select count(*) from product where product_name=? and product_category=?");
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, name);
            pStmt.setString(2, category);
            ResultSet rset=pStmt.executeQuery();
            int count=0;
	        if(rset.next()){
		        count = rset.getInt(1);
	        }
	        if (count != 0){
	        	System.out.println("This product has already been in the databases. Please turn back and choose to edit it!" + "\n");
	        	return;
	        }
	        
	        System.out.println("Please enter product size (in cubic feet): ");
		    int size = sc.nextInt();
		    
    	    sql="Insert into product values(?,?,?)";
    	    pStmt = conn.prepareStatement(sql);
    	    pStmt.setString(1, name);
	    	pStmt.setString(2, category);
	    	pStmt.setInt(3, size);
    	    pStmt.executeUpdate();
 
    	    System.out.println("New product added!" + "\n");

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
    
    public static void modify(){

	    try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
		try {
			System.out.println("Please enter the old product name: ");
		    String name = sc.next();
		    System.out.println("Please enter the old product category: ");
		    String category = sc.next();    

			Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
		    /**
		     * check if the product has already in the database
		     */
			String sql=("select count(*) from product where product_name=? and product_category=?");
	        PreparedStatement pStmt = conn.prepareStatement(sql);
	        pStmt.setString(1, name);
            pStmt.setString(2, category);
	        ResultSet rset=pStmt.executeQuery();
	        int count=0;
    	    if(rset.next()){
    		    count = rset.getInt(1);
    	    }
    	    if (count == 0){
    	        System.out.println("This product is not in the databases. Please turn back and choose to add it!" + "\n");
	        	return;
    	    }
 
		    System.out.println("Please enter the new product size (in cubic feet): ");
		    int size = sc.nextInt();
   	    
    	    sql="update product set sizes=? where product_name=? and product_category=?";
    	    pStmt = conn.prepareStatement(sql);
	    	pStmt.setInt(1, size);
    	    pStmt.setString(2, name);
	    	pStmt.setString(3, category);
    	    pStmt.executeUpdate();
    	    
    	    System.out.println("Product information updated!" + "\n");
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
    
	public static void delete() {

	    System.out.println("Please enter product name: ");
	    String name = sc.next();
	    System.out.println("Please enter product category: ");
	    String category = sc.next();
	    try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
		try {
			Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
		    /**
		     * check if the product has already in the database
		     */
			String sql=("select count(*) from product where product_name=? and product_category=?");
	        PreparedStatement pStmt = conn.prepareStatement(sql);
	        pStmt.setString(1, name);
            pStmt.setString(2, category);
	        ResultSet rset=pStmt.executeQuery();
	        int count=0;
    	    if(rset.next()){
    		    count = rset.getInt(1);
    	    }
    	    if (count == 0){
    	        System.out.println("This product is not in the databases!" + "\n");
	        	return;
    	    }
    	    /**
    	     * before deleting from table 'product', products in the table 'stock' and table 'price' should first be deleted
    	     * if applicable, also deleting from other child table of 'product'
    	     */
		    sql="delete from stock where product_name =? and product_category =?";
		    pStmt = conn.prepareStatement(sql);
		    pStmt.setString(1, name);
	    	pStmt.setString(2, category);
    	    pStmt.executeUpdate();
    	    
		    sql="delete from price where product_name =? and product_category =?";
		    pStmt = conn.prepareStatement(sql);
		    pStmt.setString(1, name);
	    	pStmt.setString(2, category);
    	    pStmt.executeUpdate();
    	    
		    sql="delete from product where product_name =? and product_category =?";
		    pStmt = conn.prepareStatement(sql);
		    pStmt.setString(1, name);
	    	pStmt.setString(2, category);
    	    pStmt.executeUpdate();
    
    	    System.out.println("Product deleted!"+"\n");
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
	
	public static void setPrice(){
	
		boolean controler1 = true;
		boolean controler2 = true;	    
	    try {
		    Class.forName("oracle.jdbc.driver.OracleDriver");
	    } catch (ClassNotFoundException e) {
		    System.out.println("Exception:"+ e);
	    }
        try {
        	System.out.println("Please enter product name:");
    	    String name = sc.next();
    	    System.out.println("Please enter product category:");
    	    String category = sc.next();
    	    
	        Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
		    /**
		     * check if the product has already in the database
		     */
	        String sql=("select count(*) from product where product_name=? and product_category=?");
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, name);
            pStmt.setString(2, category);
            ResultSet rset=pStmt.executeQuery();
            int count=0;
	        if(rset.next()){
		        count = rset.getInt(1);
	        }
	        if (count == 0){
	            System.out.println("This product is not in the databases. Please turn back and choose to add it!" + "\n");
            	return;
	        }
	    
	        do{
	            System.out.println("Please enter the related state:");
	            String state = sc.next();
	            System.out.println("Please enter price:");
	            int price = sc.nextInt();
			    /**
			     * check if the product has already have record in the related state
			     * if no, insert this price information
			     * else change the price 
			     */
			    sql=("select count(*) from price where product_name=? and product_category=? and state=?");
    	        pStmt = conn.prepareStatement(sql);
    	        pStmt.setString(1, name);
	    	    pStmt.setString(2, category);
	    	    pStmt.setString(3, state);
    	        rset=pStmt.executeQuery();
    	        if(rset.next()){
    	    	    count = rset.getInt(1);
    	        }
    	        if (count == 0){
    	        	sql=("insert into price values(?,?,?,?)");
        	        pStmt = conn.prepareStatement(sql);
        	        pStmt.setString(1, name);
    	    	    pStmt.setString(2, category);
    	    	    pStmt.setString(3, state);
    	        	pStmt.setInt(4, price);
        	        pStmt.executeUpdate();
    	        }
    	        else{
    	    	    sql=("update price set price=? where product_name=? and product_category=? and state=?");
        	        pStmt = conn.prepareStatement(sql);
        	        pStmt.setInt(1, price);
    	        	pStmt.setString(2, name);
    	        	pStmt.setString(3, category);
    	        	pStmt.setString(4, state);
        	        pStmt.executeUpdate();
    	        }
    	        do{
    	    	    System.out.println("Set more prices for this product? Enter Y to continue / Enter N to exist");
    	    	    String flag = sc.next();
    	    	    switch(flag){
    	            case "Y":
    	            	controler1 = false;
    		            break;
    	            case "N":
    		        	controler2 = false;
    		        	break;
    	            default:
    	    	        System.out.println("Please enter capital letter Y or N" + "\n");
    	            }
    	    	}while(controler1 && controler2);
   	    	}while(controler2); 
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
	public static void addInWare(){

		boolean controler1 = true;
		boolean controler2 = true;
	    System.out.println("Please enter product name:");
	    String name = sc.next();
	    System.out.println("Please enter product category:");
	    String category = sc.next();
	    do{
	        System.out.println("Please enter related warehouseID:");
	        int warehouseId = sc.nextInt();
	        System.out.println("Please enter amount: ");
	        int newAmount = sc.nextInt();
	        try {
			    Class.forName("oracle.jdbc.driver.OracleDriver");
		    } catch (ClassNotFoundException e) {
			    System.out.println("Exception:"+ e);
		    }
	        try {
			    Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
			    /**
			     * check if the product has already in the database
			     */
			    String sql=("select count(*) from product where product_name=? and product_category=?");
	            PreparedStatement pStmt = conn.prepareStatement(sql);
	            pStmt.setString(1, name);
	            pStmt.setString(2, category);
	            ResultSet rset=pStmt.executeQuery();
	            int count=0;
		        if(rset.next()){
			        count = rset.getInt(1);
		        }
		        if (count == 0){
		            System.out.println("This product is not in the databases. Please turn back and choose to add it!" + "\n");
	            	return;
		        }
		        /**
			     * check if the product has already in the related warehouse
			     */
			    sql=("select count(*) from stock where product_name=? and product_category=? and warehouse_id=?");
    	        pStmt = conn.prepareStatement(sql);
    	        pStmt.setString(1, name);
	    	    pStmt.setString(2, category);
	    	    pStmt.setInt(3, warehouseId);
    	        rset=pStmt.executeQuery();
    	        count=0;
    	        if(rset.next()){
    	        	count = rset.getInt(1);  	    	    
    	        }
    	        /**
    	         * check if the amount of the product to be added will make the total amount in the warehouse over its capacity
    	         */
    	        sql=("select storage_capacity from warehouse where warehouse_id=?");
    	        pStmt = conn.prepareStatement(sql);
	    	    pStmt.setInt(1, warehouseId);
    	        rset=pStmt.executeQuery();
    	        int totalSize=0;
    	        if(rset.next()){
    	        	totalSize = rset.getInt(1);
    	        }

    	        sql=("select sizes from product where product_name=? and product_category=? ");
    	        pStmt = conn.prepareStatement(sql);
    	        pStmt.setString(1, name);
	    	    pStmt.setString(2, category);
    	        rset=pStmt.executeQuery();
    	        int productSize=0;
    	        if(rset.next()){    	        	
    	        	productSize = rset.getInt(1);
    	        }

    	        sql=("select available_amount*sizes from stock natural join product where warehouse_id=?");
    	        pStmt = conn.prepareStatement(sql);
	    	    pStmt.setInt(1, warehouseId);
    	        rset=pStmt.executeQuery();
    	        int currentSize=0;
    	        while(rset.next()){     	
    	        	currentSize = rset.getInt(1)+currentSize;
    	        }
    	        
    	        int expectSize = newAmount * productSize + currentSize;
    	        if (expectSize > totalSize){
    	        	System.out.println("Adding action will lead to exceeding of the size of the warehouse.");
    	        	System.out.println("Please reenter!");
    	        	System.out.println("The current available space in this warehouse is: "+ (totalSize-currentSize));
    	        	System.out.println("The product size is: "+ productSize +"\n");
    	        }
    	        else{
    	            if (count == 0){
    	        	    sql=("insert into stock values(?,?,?,?)");
        	            pStmt = conn.prepareStatement(sql);
        	            pStmt.setString(1, name);
    	    	        pStmt.setString(2, category);
    	    	        pStmt.setInt(3, warehouseId);
    	    	        pStmt.setInt(4, newAmount);
        	            pStmt.executeUpdate();
    	            }
    	            else{
    	    	        sql=("update stock set available_amount=available_amount +? where product_name=? and product_category=? and warehouse_id=?");
        	            pStmt = conn.prepareStatement(sql);
        	            pStmt.setInt(1, newAmount);
    	        	    pStmt.setString(2, name);
    	        	    pStmt.setString(3, category);
    	        	    pStmt.setInt(4, warehouseId);
        	            pStmt.executeUpdate();
    	            }
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
    	    do{
    	        System.out.println("Set more storage for this product? Enter Y to continue / Enter N to exist");
    	        String flag = sc.next();
    	        switch(flag){
                case "Y":
                	controler1 = false;
	                break;
                case "N":
	            	controler2 = false;
	            	break;
                default:
    	            System.out.println("Please enter capital letter Y or N" + "\n");
               }
    	    }while(controler1 && controler2);
    	}while(controler2); 
	}
}
