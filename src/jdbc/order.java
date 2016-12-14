

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Scanner;


public class order {
	
   	static Scanner sc = new Scanner(System.in);
	static final String jdbcURL="jdbc:oracle:thin:@fourierXXXXX";
    static final String User_name="XXXXX";
    static final String password="XXXXX";
    /**
     * If there is already this product in the shopping cart,
     * add the new quantity to the old quantity.
     * If no, only insert.
     * 
     * @param customer id
     */
    public static void addToCart(int id){
 
		boolean controler1 = true;
		boolean controler2 = true;
    	do{
	        System.out.println("Please enter product name: ");
	        String pName = sc.next();
	        System.out.println("Please enter product type: ");
	        String pType = sc.next();
	        System.out.println("Please enter quantity: ");
	        int quantity = sc.nextInt();
	        	
    	    try {
			    Class.forName("oracle.jdbc.driver.OracleDriver");
		    } catch (ClassNotFoundException e) {
			    System.out.println("Exception:"+ e);
		    }
		    try {
			    Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
			    String sql=("select count(*) from shoppingcart where product_name=? and product_category=? and customer_id=?");
	            PreparedStatement pStmt = conn.prepareStatement(sql);
	            pStmt.setString(1, pName);
	            pStmt.setString(2, pType);
	            pStmt.setInt(3, id);
	            ResultSet rset=pStmt.executeQuery();
	            int count=0;
		        if(rset.next()){
			        count = rset.getInt(1);
		        }
		        if (count == 0){
		        	sql="insert into shoppingcart values(?,?,?,?)";
	    	        pStmt = conn.prepareStatement(sql);
	    	        pStmt.setInt(1, id);
	    	    	pStmt.setString(2, pName);
	    	    	pStmt.setString(3, pType);
	    	    	pStmt.setInt(4, quantity);
	        	    pStmt.executeUpdate();
	        	    System.out.println("New product added!" + "\n");
		        }
		        else{
		        	sql="update shoppingcart set quantity=quantity+? where product_name=? and product_category=? and customer_id=?";
	    	        pStmt = conn.prepareStatement(sql);
	    	        pStmt.setInt(1, quantity);
	    	    	pStmt.setString(2, pName);
	    	    	pStmt.setString(3, pType);
	    	    	pStmt.setInt(4, id);
	        	    pStmt.executeUpdate(); 
	        	    System.out.println("Product quantity updated!" + "\n");
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
    	        System.out.println("Add more products?");
    	        System.out.println("Enter Y to continue / Enter N to exist");  	    
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
    
    public static boolean cartCheck(int id){
    	
    	boolean flag = false;
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
		try {
    	    Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
    	    /**
    	     * check if the customer had a credit card in the account
    	     */
    	    String sql=("select count(*) from creditcard where customer_id=?");
    	    PreparedStatement pStmt = conn.prepareStatement(sql);
    		pStmt.setInt(1, id);
    		ResultSet rset=pStmt.executeQuery();
    		int count=0;
    	    while(rset.next()){
    	        count=rset.getInt(1);
    	    }
    	    if (count==0){
    	    	System.out.println("You don't have any credit card in your account. Please turn back and add a payment method first!"+"\n");
    	    	flag = true;
    	    	return flag;
    	    }
    	    /**
    	     * check if the customer had a shipping address
    	     */
    	    sql=("select count(*) from delivery_address where customer_id=?");
    	    pStmt = conn.prepareStatement(sql);
    		pStmt.setInt(1, id);
    		rset=pStmt.executeQuery();
    	    while(rset.next()){
    	        count=rset.getInt(1);
    	    }
    	    if (count==0){
    	    	System.out.println("You don't have any delivery address in your account. Please turn back and add a shipping address first!"+"\n");
    	    	flag = true;
    	    	return flag;
    	    }
    	    /**
    	     * check if the shopping cart is empty
    	     */
    	    sql=("select count(*) from shoppingcart where customer_id=?");
    	    pStmt = conn.prepareStatement(sql);
    	    pStmt.setInt(1, id);
	        rset=pStmt.executeQuery();
    	    while(rset.next()){
    	        count = rset.getInt(1);
    	    }    
    	    if (count==0){
    	    	System.out.println("You don't have any products in your shopping cart. Please turn back and add what your favourite!"+"\n");
    	    	flag = true;
    	    	return flag;
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
		return flag;
    }
    /**
     * Show products and their quantity and prices in the shopping cart
     * prices are related to the state of customer's shipping address
     * @param customer id
     * @return if the cart is empty return true, else return false
     */
    public static boolean getCart(int id){
    	
    	boolean emptyCart = false;
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
		try {
    	    Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
    	    String sql=("select count(*) from shoppingcart where customer_id=?");
    	    PreparedStatement pStmt = conn.prepareStatement(sql);
    	    pStmt.setInt(1, id);
    	    ResultSet rset=pStmt.executeQuery();	  
    	    int temp = 1;
    	    while(rset.next()){
    	        temp = rset.getInt(1);
    	    }    
    	    if (temp != 0){
    	    	System.out.println("Products in your cart (name category quantity price):");

    		    sql=("select state from delivery_address natural join address where customer_id=? and primary='Yes'");
    		    pStmt = conn.prepareStatement(sql);
    		    pStmt.setInt(1, id);
    		    rset=pStmt.executeQuery();
    		    String state = null;
    		    while(rset.next()){
        	        state = rset.getString(1);
        	    }
    	    	/**
    	    	 * show all the products and quantity and prices
    	    	 */
    	    	sql=("select product_name, product_category, quantity, price from shoppingcart natural join price where state=? and customer_id=?"); 	    	
    	    	pStmt = conn.prepareStatement(sql);
    	    	pStmt.setString(1, state); 
    	    	pStmt.setInt(2, id);
    	    	rset=pStmt.executeQuery();
        	    while(rset.next()){
        		    System.out.println(rset.getString("product_name")+" "+rset.getString("product_category")+" "
        	                          +rset.getInt("quantity")+" $"+rset.getInt("price"));
                }
        	    /**
        	     * show subtotal
        	     */
        	    sql=("select quantity*price from price natural join shoppingcart where state =? and customer_id=?");
    		    pStmt = conn.prepareStatement(sql);
    		    pStmt.setString(1, state);
    		    pStmt.setInt(2, id);
    		    rset=pStmt.executeQuery();
    		    int total=0;
    		    while(rset.next()){
        	        total = rset.getInt(1)+total;
        	    }
        	    System.out.println("\n" + "Subtotal: $" + total);
    	    }
 
    	    else{
    	    	emptyCart = true;
    	    	System.out.println("Your cart is empty, but it is not necessary!");
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
		return emptyCart;
    }
    /**
     * If there is already this product in the shopping cart,
     * change the old quantity to the new quantity.
     * If no, only insert.
     * 
     * @param customer id
     */
    public static void updateQuant(int id){
 
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
		try {
			System.out.println("Please enter product name: ");
	        String pName = sc.next();
	        System.out.println("Please enter product type: ");
	        String pType = sc.next();
	        System.out.println("Please enter new quantity: ");
	        int quantity = sc.nextInt();
	        
	        Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
	        String sql=("select count(*) from shoppingcart where product_name=? and product_category=? and customer_id=?");
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, pName);
            pStmt.setString(2, pType);
            pStmt.setInt(3, id);
            ResultSet rset=pStmt.executeQuery();
            int count=0;
	        if(rset.next()){
		        count = rset.getInt(1);
	        }
	        if (count == 0){
	        	sql="insert into shoppingcart values(?,?,?,?)";
    	        pStmt = conn.prepareStatement(sql);
    	        pStmt.setInt(1, id);
    	    	pStmt.setString(2, pName);
    	    	pStmt.setString(3, pType);
    	    	pStmt.setInt(4, quantity);
        	    pStmt.executeUpdate();
        	    System.out.println("New product added!" + "\n");
	        }
	        else{
	        	sql="update shoppingcart set quantity=? where product_name=? and product_category=? and customer_id=?";
    	        pStmt = conn.prepareStatement(sql);
    	        pStmt.setInt(1, quantity);
    	    	pStmt.setString(2, pName);
    	    	pStmt.setString(3, pType);
    	    	pStmt.setInt(4, id);
        	    pStmt.executeUpdate(); 
        	    System.out.println("Product quantity updated!" + "\n");
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
    }
    
    public static void deleteFromCart(int id){

    	System.out.println("Please enter product name: ");
        String pName = sc.next();
        System.out.println("Please enter product type: ");
        String pType = sc.next();
        
        try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
		try {
			Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
			/**
			 * check if the product has already in the shopping cart
			 */
			String sql=("select count(*) from shoppingcart where product_name=? and product_category=? and customer_id=?");
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, pName);
            pStmt.setString(2, pType);
            pStmt.setInt(3, id);
            ResultSet rset=pStmt.executeQuery();
            int count=0;
	        if(rset.next()){
		        count = rset.getInt(1);
	        }
	        if (count != 0){
    	    	sql="delete from shoppingcart where product_name=? and product_category=? and customer_id=?";
    	    	pStmt = conn.prepareStatement(sql);
                pStmt.setString(1, pName);
                pStmt.setString(2, pType);
                pStmt.setInt(3, id);
                pStmt.executeUpdate();
        	    System.out.println("Cart successfully updated!");
    	    }
    	    else{
    	    	System.out.println("Your don't have such product in your cart!" + "\n");
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
    }
    /**
     * before placing an order, check if the quantity in the warehouse is enough
     * only check warehouses which are in the same state of the customer's shipping address.
     * WARNING: in a state, there might be more than one warehouse
     * 
     * @param customer id
     * @return
     */
    public static boolean placeCheck(int id){
 
    	Boolean needChange = false;
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
		    String st=null;
		    while(rset.next()){
    	        st = rset.getString(1);
    	    }
		    
		    sql=("select sum(available_amount), product_name, product_category, quantity from address natural join warehouse natural join stock natural join shoppingcart where state =? and customer_id=? group by product_name, product_category, quantity");
		    pStmt = conn.prepareStatement(sql);
		    pStmt.setString(1, st);
		    pStmt.setInt(2, id);
		    rset=pStmt.executeQuery();
		    int flag=0;
		    while(rset.next()){
    	        flag = rset.getInt(4)-rset.getInt(1);
    	        if (flag > 0){
		    	    needChange = true;
		    	    System.out.println("This product's quantity has over the available amount (product name, product category, quantity, available amount): "+"\n"+rset.getString("product_name")+" "+rset.getString("product_category")+" "+rset.getInt("quantity")+" "+rset.getInt("available_amount"));	        
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
		return needChange;
	}
    
    public static void place(int id){
    	
        try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
    	try {
			Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
            /**
             * check the total price 
             */
			String sql=("Select state from delivery_address natural join address where customer_id=? and primary='Yes'");
		    PreparedStatement pStmt = conn.prepareStatement(sql);
		    pStmt.setInt(1, id);
		    ResultSet rset=pStmt.executeQuery();
		    String st=null;
		    while(rset.next()){
    	        st = rset.getString(1);
    	    }

		    sql=("select quantity*price from price natural join shoppingcart where state =? and customer_id=?");
		    pStmt = conn.prepareStatement(sql);
		    pStmt.setString(1, st);
		    pStmt.setInt(2, id);
		    rset=pStmt.executeQuery();
		    int total=0;
		    while(rset.next()){
    	        total = rset.getInt(1)+total;
    	    }
		    System.out.println("Your total amount due: $" + total);

		    boolean controler=true;
		    long creditCard = 0;
 			do{	
	    	    System.out.println("Please select a payment method from your credit cards: ");
			/**
			 * from the existing cards, customer should select one
			 */
    	        sql=("select card_number from creditcard where customer_id=?");
    	        pStmt = conn.prepareStatement(sql);
    		    pStmt.setInt(1, id);
    	    	rset=pStmt.executeQuery();
    	        while(rset.next()){
    	            System.out.println(rset.getLong("card_number"));
    	        }
    	    
    	        System.out.println("Please enter the chosen card number: ");
                creditCard = sc.nextLong();
                sql=("select count(*) from creditcard where card_number=?");
    	        pStmt = conn.prepareStatement(sql);
    		    pStmt.setLong(1, creditCard);
    		    rset=pStmt.executeQuery();
    	        while(rset.next()){
    	    	    if (rset.getInt(1)==0)
    	                System.out.println("Wrong input! Please reenter!"+"\n");
    	    	    else
    	    		    controler=false;
    	        }
 			}while(controler);
 			/**
 			 * show the primary shipping address information
 			 */
 			System.out.println("Your items will be deliveried to the address below: ");
	        sql=("select street_number, street_name, apt_number, zipcode from delivery_address where customer_id=? and primary='Yes'");
	        pStmt = conn.prepareStatement(sql);
		    pStmt.setInt(1, id);
	    	rset=pStmt.executeQuery();
	        while(rset.next()){
	            System.out.println(rset.getInt(1)+" "+rset.getString(2)+" "+rset.getInt(3)+" "+rset.getInt(4));
	        }
 			/**
 			 * the order number increases in ascending order
 			 * the new order get an order number automatically
 			 */
            int orderNm=0;
            Statement stat = conn.createStatement();
    	    rset=stat.executeQuery("select max(order_number) from orders");
    	    while(rset.next()){
    	        orderNm = rset.getInt(1)+1;
    	    }

    	    Timestamp time = new Timestamp(System.currentTimeMillis()); 
    	    /**
    	     * from here, all the actions must all be completed together. concurrency control is needed
    	     */
    	    /**
    	     * insert new order into table 'orders' 
    	     */
    	    sql=("Insert into orders values(?,?,?,?,?, 'received')");
	        pStmt = conn.prepareStatement(sql);
    		pStmt.setInt(1, orderNm);
    		pStmt.setInt(2, id);
    		pStmt.setLong(3, creditCard);
    		pStmt.setInt(4, total);
    		pStmt.setTimestamp(5, time);
    		pStmt.executeUpdate();  
   		
			sql=("select count(*) from shoppingcart where customer_id=?");
			pStmt = conn.prepareStatement(sql);
    	    pStmt.setInt(1, id);
	        rset=pStmt.executeQuery();
    	    int j=0;
    	    while(rset.next()){
    	    	j=rset.getInt(1);
            }

			sql=("select product_name, product_category, quantity from shoppingcart where customer_id=?");
			pStmt = conn.prepareStatement(sql);
    	    pStmt.setInt(1, id);
	        rset=pStmt.executeQuery();
			int[] quantity= new int[j]; 
    	    String[] pName=new String[j], pType=new String[j];
    	    int i=0;
    	    while(rset.next()){
    	    	pName[i] = rset.getString(1);
    	    	pType[i] = rset.getString(2);
    	    	quantity[i] = rset.getInt(3);
    	    	i++;
            }
  	    
		    sql=("select count(*) from warehouse natural join address where state = ?");
			pStmt = conn.prepareStatement(sql);
    	    pStmt.setString(1, st);
	        rset=pStmt.executeQuery();
    	    int k=0;
    	    while(rset.next()){
    	    	k=rset.getInt(1);
            }

    	    sql=("select warehouse_id from warehouse natural join address where state = ? order by warehouse_id");
    	    pStmt = conn.prepareStatement(sql);
		    pStmt.setString(1, st);
		    rset=pStmt.executeQuery();
		    int[] warehouseId=new int[k];
		    i=0;
		    while(rset.next()){
		    	warehouseId[i] = rset.getInt(1);
		    	i++;
    	    }
		    /**
		     * insert order details into table 'order_product'
		     * 
		     * deduct quantity of the products from warehouse in ascending order
		     * e.g. In customer shipping address state, there are warehouse 2, warehouse 3 and warehouse 5. Deduction from 2 first and then 3 finally 5.
		     */
    		for(i=0;i<j;i++){
    	        sql=("Insert into order_details values(?,?,?,?)");
    	        pStmt = conn.prepareStatement(sql);
    	    	pStmt.setInt(1, orderNm);
    	    	pStmt.setString(2, pName[i]);
    		    pStmt.setString(3, pType[i]);
    		    pStmt.setInt(4, quantity[i]);
    		    pStmt.executeUpdate();
   		    
    		    int[] aMount=new int[k];
    		    for(int h=0;h<k;h++){
    		        sql=("select available_amount from address natural join warehouse natural join stock natural where state =? and product_name=? and product_category=? and warehouse_id=?");
    		        pStmt = conn.prepareStatement(sql);
    		        pStmt.setString(1, st);
    		        pStmt.setString(2, pName[i]);
    		        pStmt.setString(3, pType[i]);
    		        pStmt.setInt(4, warehouseId[h]);
    		        rset=pStmt.executeQuery();
    		        while(rset.next()){
    		    	    aMount[h] = rset.getInt(1);
    		        }
    			    if (aMount[h]>=quantity[i]){
    			        sql=("update stock set available_amount = available_amount -? where product_name=? and product_category=? and warehouse_id=?");
    	    	        pStmt = conn.prepareStatement(sql);
    	    		    pStmt.setInt(1, quantity[i]);
    	    		    pStmt.setString(2, pName[i]);
    	    		    pStmt.setString(3, pType[i]);
    	    		    pStmt.setInt(4, warehouseId[h]);
    	    		    pStmt.executeUpdate();
    	    		    break;
    			    }
    			    else {
    			       	sql=("update stock set available_amount = 0 where product_name=? and product_category=? and warehouse_id=?");
    	    	        pStmt = conn.prepareStatement(sql);
    	    		    pStmt.setString(1, pName[i]);
    	    		    pStmt.setString(2, pType[i]);
    	    		    pStmt.setInt(3, warehouseId[h]);
    	    		    pStmt.executeUpdate();
    	    		    quantity[i]=quantity[i]-aMount[h];
    			    }
    			}
    		}
    		/**
    		 * remove all products in the shopping cart
    		 */
    	    sql=("delete from shoppingcart where customer_id=?");
    	    pStmt = conn.prepareStatement(sql);
    		pStmt.setInt(1, id);
    		pStmt.executeUpdate();
    		System.out.println("\n"+"Order placed!"+"\n");
    		/**
    		 * update customer store balance
    		 */    	    
    		sql=("update customer set balance = balance +? where customer_id=?");
    	    pStmt = conn.prepareStatement(sql);
    	    pStmt.setInt(1, total);
    	    pStmt.setInt(2, id);
    		pStmt.executeUpdate();
	        /**
	         * show customer new store balance	       
	         */
    		sql=("select balance from customer where customer_id=?");
    		pStmt = conn.prepareStatement(sql);
    	    pStmt.setInt(1, id);
    		rset=pStmt.executeQuery();
    	    while(rset.next()){
    	    	System.out.println("Your new store balance is: $"+rset.getInt(1)+"\n");
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
    }
    /**
     * order history. both total and details
     * @param customer id
     */
    public static void history(int id){
    	System.out.println("Orders history:"+"\n");

    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Exception:"+ e);
		}
    	try {
			Connection conn = DriverManager.getConnection(jdbcURL, User_name, password);
			String sql=("select * from orders where customer_id=?");
			PreparedStatement pStmt = conn.prepareStatement(sql);
    	    pStmt.setInt(1, id);
	        ResultSet rset=pStmt.executeQuery();
    	    while(rset.next()){
    	    	System.out.println("order number: "+rset.getInt("order_number")+" credit card: "+rset.getLong("card_number")+" subtotal: "+rset.getInt("subtotal")+" placed time: "+rset.getTimestamp("time")+" status: "+rset.getString("status"));
            }
    	    System.out.println("\n");
    	    do{
    	    System.out.println("Please input an order number to check details of the order (or input 0 to quit):");
    	    int order= sc.nextInt();
    	    if (order==0)
    	    	break;
    	    
    	    System.out.println("Orders details:"+"\n");
    	    sql=("select * from order_details where order_number=?");
			pStmt = conn.prepareStatement(sql);
    	    pStmt.setInt(1, order);
	        rset=pStmt.executeQuery();
    	    while(rset.next()){
    	    	System.out.println("order number: "+rset.getInt("order_number")+" product name: "+rset.getString("product_name")+" product category: "+rset.getString("product_category")+" quantity: "+rset.getInt("quantity"));
            }
    	    System.out.println("\n");
    	    }while(true);
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
