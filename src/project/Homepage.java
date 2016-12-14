

import java.util.Scanner;

import jdbc.creditcard;
import jdbc.customer;
import jdbc.deliveryaddress;
import jdbc.order;
import jdbc.product;
import jdbc.staff;
/**
 * This is the CS425 project 'online grocery store application'
 * This is the main class. 2016-12-11 
 * 
 *
 */
public class Homepage {
	
	public static int CUSID;
	public static int STAFFID;	
	Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		System.out.println("Welcome!"+"\n");
		new Homepage().run();
	}
	/**
	 * From here, customers and staffs can log in through different entrances. 
	 * Visitors also can browse the products and have chance to register to be a customer.
	 * 
	 */
	private void run(){
		boolean controler = true;
		do{
		    System.out.println("Customer login please enter A");
		    System.out.println("Staff login please enter B");		    
		    System.out.println("Visitor please enter C");
		    System.out.println("Or enter Q to exsit...");
		    String login = sc.next();
		    switch(login){
		    case "A":
			    customerLogin();
			    break;
		    case "B":
			    staffLogin();
			    break;
		    case "C":
			    visitorMenu();
			    break;
		    case "Q":
			    System.out.println("Goodbye!");
			    controler = false;
			    break;
		    default:
			    System.out.println("Please enter capital letter A, B, C or Q" + "\n");
		    }		
	    }while(controler);
	}
	/**
	 * Customer login function. Call methods from customer class.
	 * First, check if the inputed customer ID is a valid ID
	 * Second, check if password match
	 * Third, greeting and 6 different action list
	 * 
	 */
	private void customerLogin(){
		System.out.println("Please enter your customerID:");
		CUSID = sc.nextInt();		
		if(!customer.getAvailable(CUSID)){
		    System.out.println("No such customerID. Please register!" + "\n");
		}
		else{
		    boolean controler = true;
		    do{
		        System.out.println("Please enter your password:");
		        String code= sc.next();
		        if (code.equals("Q")){
		        	return;
		        }
		        if (!code.equals(customer.getCustPw(CUSID))){
			        System.out.println("Wrong password! Please reenter! (or enter Q to exist)" + "\n");
		        }
		        else 
		    	    controler = false;
	    	}while(controler);
		    System.out.println("\n" + "Dear " + customer.getCustNm(CUSID) + ", welcome back!");
		    System.out.println("\n" + "What can I do for you today?" + "\n");
			do{
			    System.out.println("My Shopping Cart -> Enter A");
			    System.out.println("My Wallet -> Enter B");
			    System.out.println("My Shipping Address -> Enter C");
			    System.out.println("My Order History -> Enter D");
			    System.out.println("My Password -> Enter E");
			    System.out.println("Products Information -> Enter F");
			    System.out.println("Log out -> Enter Q");
			    String choice = sc.next();
			    switch(choice){
			    case "A":
			    	shoppingCart();
				    break;
			    case "B":
				    creditCard();
				    break;
			    case "C":
			    	shippingAddress();
			    	break;
			    case "D":
			    	orderHistory();
			    	break;
			    case "E":
			    	password();
			    	break;
			    case "F":
			    	productInfor();
			    	break;
			    case "Q":
			    	controler = true;
			    	break;
			    default:
			    	System.out.println("Please enter capital letter A, B, C, D, E, F or Q" + "\n");
			    }
			}while(!controler);
		}
		return;
	}
	/**
	 * Customer can check their shopping cart, add products, update quantity, delete products and check out.
	 * To show customer friendly, it is not very strict in choices A and B
	 * If a customer selected A but add a product which has already in the cart, the application will add more quantity to the old one. 
	 * e.g. John has already has 4 apples in the cart. He selected add new products and add 5 apples. He now has 9 apples in the cart.
	 * If a customer selected B. The quantity will be substituted. And he can also add new products through choice A.
	 * Customers will get notes after each action. 
	 * 
	 * shopping cart remain the same regardless customers login and logout.
	 * shopping cart shows quantity and price of the products, also shows subtotal.
	 * Before order placed, the application will check if the amount in the cart was over the amount in the warehouse
	 * after order placed, 
	 * the shopping cart will be set empty again; 
	 * the order information will be record (received order);
	 * order details be recorded too in the order_product table;
	 * the stock will reduced;
	 * (deduct from warehouses in ascending order e.g. illinois has three warehouses, which ids are 3, 5, 8. The warehouse 3 will first be deducted, then 5, finally 8.)
	 * the customer balance will increase
	 * 
	 */
    private void shoppingCart(){
	    boolean controler = true;
	    do{	
	    	System.out.println("--------------------------------------------");
	    	order.getCart(CUSID);
	    	System.out.println("--------------------------------------------");
	    	System.out.println("Add new products into cart -> enter A");
	        System.out.println("Update product quantity in cart -> enter B");
	        System.out.println("Delete products from cart -> enter C");
	        System.out.println("I AM READY TO PLACE ORDER! -> enter D");
	        System.out.println("Back to my account -> enter Q");
	        String cart=sc.next();
	        switch(cart){
	        case "A":
	        	order.addToCart(CUSID);
	        	break;
	        case "B":
	        	order.updateQuant(CUSID);
	        	break;
	        case "C":
	        	order.deleteFromCart(CUSID);
	        	break;
	        case "D":
	        	if (!order.cartCheck(CUSID) && !order.placeCheck(CUSID))
	        		order.place(CUSID);
	        	break;
	        case "Q":
	        	controler = false;
	        	break;
	        default:
	        	System.out.println("Please capital letter A, B, C, D or Q" + "\n");
	        }	        
 	    }while(controler);
	    return;
	}
	/**
	 * Customers add, edit and delete their credit cards.
	 * They can also check their balances here;
	 * After order placed, the total of the order will be added to the balance automatically.
	 * Customer can select one of the existing credit card in payment.
	 * 
	 */
    private void creditCard(){
    	boolean controler = true;
		do{
    	    System.out.println("Add a new card -> enter A");
    	    System.out.println("Edit a current card -> enter B");
    	    System.out.println("Delete a card -> enter C");
    	    System.out.println("Check my store balance -> enter D");
    	    System.out.println("Back to my account -> enter Q");
    	    String card = sc.next();
	        switch(card){
	        case "A":
	    	    creditcard.add(CUSID);
		        break;
	        case "B":
		        creditcard.modify(CUSID);
		        break;
	        case "C":
	        	creditcard.delete();
	    	    break;
	        case "D":
	        	System.out.println("Your store balance is $" + customer.getCustBl(CUSID)+ "\n");
	    	    break;
		    case "Q":
		    	controler = false;
		    	break;
	        default:
	    	    System.out.println("Please enter capital letter A, B, C, D or Q" + "\n");
	    }
	}while(controler);
	return;
	}
	/**
	 * Add, edit and delete delivery address.
	 * Customers can have one to many delivery address. They can set one of them as primary.
	 * When customer place an order, the primary shipping address will be chosen.
	 * When customer add a new address, the new one will be set as primary automatically. 
	 * Customers also have primary setting update option from the menu. 
	 *  
	 */
    private void shippingAddress(){
    	boolean controler = true;
		do{
			System.out.println("Add a new shipping address -> enter A");
    	    System.out.println("Edit a shipping address -> enter B");
    	    System.out.println("Update primary setting -> enter C");
    	    System.out.println("Delete a shipping address -> enter D");
    	    System.out.println("Back to my account -> enter Q");
    	    String address = sc.next();
	        switch(address){
	        case "A":
	    	    deliveryaddress.add(CUSID);
		        break;
	        case "B":
	        	deliveryaddress.modify(CUSID);
		        break;
	        case "C":
	        	deliveryaddress.modifyPrimary(CUSID);
	    	    break;
	        case "D":
	        	deliveryaddress.delete(CUSID);
	    	    break;
		    case "Q":
		    	controler = false;
		    	break;
	        default:
	    	    System.out.println("Please enter capital letter A, B, C, D or Q" + "\n");
	        }
	    }while(controler);
	    return;
	}
    /**
     * check order history. both total and detail.
     */
    private void orderHistory(){
    	order.history(CUSID);	
	}
    /**
     * customer can edit their passwords as long as they know their old ones.
     */
    private void password(){
    	boolean controler = true;
    	do{
    	    System.out.println("Please enter your old password:");
    	    String oPassword=customer.getCustPw(CUSID);
    	    String iPassword=sc.next();
    	    if (!iPassword.equals(oPassword)){
    	    	System.out.println("Wrong password! Please enter your old password again:");
    	    }
    	    else{
    	    	customer.password(CUSID);
    	    	controler = false;
    	    }   	    	
    	}while(controler);
	}
    /**
     * Customer can check their local products available and browse products.
     * Local means products warehouse has same state with customer delivery address
     * Product browse will be shown type by type. Same category products are grouped together.
     * 
     */
    public void productInfor(){
    	boolean controler = true;
		do{
    	    System.out.println("Search for products available -> enter A");
    	    System.out.println("Browse all products grouped by type -> enter B");
    	    System.out.println("Back to my account -> enter Q");    	    
    	    String productIn = sc.next();
	        switch(productIn){
	        case "A":
	    	    product.search(CUSID);
		        break;
	        case "B":
	        	product.browse();
		        break;
		    case "Q":
		    	controler = false;
		    	break;
	        default:
	    	    System.out.println("Please enter capital letter A, B or Q" + "\n");
	        }
	    }while(controler);	
	    return;
	}
    /**
     * staff and manager. 
     * staff can add, edit, delete products information, and set prices.
     * manage can manage storage and do what staff do.
     * login process mostly like customers'
     * When adding into a warehouse, the capacity of the warehouse will be checked and the over store action will be refused.
     * 
     */
    private void staffLogin(){
		System.out.println("Please enter your staffID:");
		STAFFID= sc.nextInt();
		
		if(!staff.getAvailable(STAFFID)){
		    System.out.println("No such staffID. Please reenter!" + "\n");
		}  
		else{
		    boolean controler = true;
		    do{
		        System.out.println("Please enter your password:");
		        String code= sc.next();
		        if (code.equals("Q")){
		        	return;
		        }
		        if (!code.equals(staff.getStaffPw(STAFFID))){
			        System.out.println("Wrong password! Please reenter! (or enter Q to exist)" + "\n");
		        }
		        else 
		        	controler = false;
		    }while(controler);
		    System.out.println("\n" + "Dear " + staff.getStaffTitle(STAFFID) + " " + staff.getStaffNm(STAFFID) + ", welcome back!");
	        String title = staff.getStaffTitle(STAFFID);
		    if(title.equals("staff")){
			    System.out.println("\n" + "---Product information organization---" + "\n");
			    productOrga();
		    }
		    else{
			    do{
				    System.out.println("\n" + "Product information organization -> enter A");
				    System.out.println("Product storage organization -> enter B");
				    System.out.println("Log out -> enter Q");
	    	        String work = sc.next();
		            switch(work){
		            case "A":
		            	productOrga();
			            break;
		            case "B":
		            	product.addInWare();;
			            break;
			        case "Q":
			        	controler = true;
			        	break;
		            default:
		    	        System.out.println("Please enter capital letter A, B or Q" + "\n");
		            }
		        }while(!controler);
		    }		
		}
		return;
	}
    /**
     * add, edit,delete products
     * set prices
     */
    public void productOrga(){
    	boolean controler = true;
		do{
    	    System.out.println("Add a new product -> enter A");
    	    System.out.println("Update product information -> enter B");
    	    System.out.println("Delete a product -> enter C");
    	    System.out.println("Set product price -> enter D");
    	    System.out.println("Exist -> enter Q");   	    
    	    String productOr = sc.next();
	        switch(productOr){
	        case "A":
	    	    product.add();
		        break;
	        case "B":
	        	product.modify();
		        break;
	        case "C":
	        	product.delete();
		        break;
	        case "D":
	        	product.setPrice();
		        break;
		    case "Q":
		    	controler = false;
		    	break;
	        default:
	    	    System.out.println("Please enter capital letter A, B, C, D or Q" + "\n");
	        }
	    }while(controler);	
	    return;
    }
    /**
     * Visitors can browse products and register
     */
    private void visitorMenu(){
    	boolean controler = true;
		do{
    	    System.out.println("Browse all products grouped by type -> enter A");
	        System.out.println("Register to place order -> enter B");
	        System.out.println("Exist -> enter Q"); 
	        String visitor = sc.next();
	        switch(visitor){
	        case "A":
            	product.browse();
            	break;
	        case "B":
            	customer.register();
            	controler = false;
            	break;
	        case "Q":
	        	controler = false;
            	break;
            default:
            	System.out.println("Please capital letter A, B or Q" + "\n");
	        }
        }while(controler);	
    return;
	}

}
