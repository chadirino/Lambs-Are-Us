package db;

import java.sql.*;
import windows.*;
import net.proteanit.sql.*;

public class Sql {

	//=======================================  LOGIN GUI ======================================\\
	
	// change password
    public static void setPassword(String user, String password) {
    	DbConnection.connect();
    	try {
			PreparedStatement pst = DbConnection.con.prepareStatement("Update userLogin set password = \"" + password + "\" where userName = \"" + user + "\"");
			pst.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		DbConnection.disconnect();
    }
    
    // validating user login
    public static boolean validCredentials(String user, String password) {
    	DbConnection.connect();
		int counter = 0;
		try {
			PreparedStatement pst = DbConnection.con.prepareStatement("select * from userLogin where userName = \"" + user + "\" and password = \"" + password + "\"");
			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				counter++;
			}
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		DbConnection.disconnect();
		
		if(counter == 0) {
			System.out.println("invalid credentials");
			return false;
		}
		else {
			System.out.println("valid credentials");
			return true;
		}
	}
    
    // get user type
    public static String getUserType(String user) {
    	DbConnection.connect();
		String type = "";
		try {
			PreparedStatement pst = DbConnection.con.prepareStatement("select * from userLogin where userName = \"" + user + "\"");
			ResultSet rs = pst.executeQuery();
			
			type = rs.getString("userType");
			System.out.println("the user is a(n): "+ type);
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		DbConnection.disconnect();
		
		
		return type;
	}

	public static void main(String[] args) {
		DbConnection.connect();
		DbConnection.disconnect();
	}
	
	
	//======================================= ADMIN GUI ====================================\\//
	
	//view users list
	public static void getUsers() {
		DbConnection.connect();
		try {
			PreparedStatement pst = DbConnection.con.prepareStatement("select \"employeeID\", \"userName\", \"userType\" from userLogin");
			ResultSet rs = pst.executeQuery();
			AdminGUI.tblUsers.setModel(DbUtils.resultSetToTableModel(rs));
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		DbConnection.disconnect();
	}
	
	// get employee name
    public static String getEmployeeName(int employeeID) {
    	DbConnection.connect();
    	String name = "";
		try {
			PreparedStatement pst = DbConnection.con.prepareStatement("select * from employee where employeeID = " + employeeID);
			ResultSet rs = pst.executeQuery();
			
			name = rs.getString("firstName") + " " + rs.getString("lastName");
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		DbConnection.disconnect();
		
		
		return name;
    }
    
    //validate employeeID
    public static boolean validEmployee(int employeeID) {
    	DbConnection.connect();
    	int counter = 0;
		try {
			PreparedStatement pst = DbConnection.con.prepareStatement("select * from employee where employeeID = " + employeeID);
			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				counter++;
			}
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		DbConnection.disconnect();
		
		if(counter == 0) {
			System.out.println("invalid employee");
			return false;
		}
		else {
			System.out.println("valid employee");
			return true;
		}
    }
    
    //duplicate employee
    public static boolean duplicateEmployee(Integer employeeID) {
    	DbConnection.connect();
    	int counter = 0;
		try {
			PreparedStatement pst = DbConnection.con.prepareStatement("select * from userLogin where employeeID = " + employeeID);
			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				counter++;
			}
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		DbConnection.disconnect();
		
		if(counter == 0) {
			System.out.println("no duplicate employee");
			return false;
		}
		else {
			System.out.println("duplicate employee already exists");
			return true;
		}
    }
    
    //duplicate userName
    public static boolean duplicateUserName(String userName) {
    	DbConnection.connect();
    	int counter = 0;
		try {
			PreparedStatement pst = DbConnection.con.prepareStatement("select * from userLogin where userName = '" + userName+"'");
			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				counter++;
			}
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		DbConnection.disconnect();
		
		if(counter == 0) {
			System.out.println("no duplicate userName");
			return false;
		}
		else {
			System.out.println("duplicate userName already exists");
			return true;
		}
    }
    
    //add user
    public static void addUser(int employeeID, String userName, String userType) {
    	DbConnection.connect();
    	try {
			PreparedStatement pst = DbConnection.con.prepareStatement("insert into userLogin (employeeID, userName, userType, password) values("+employeeID+", '"+userName+"', '"+userType+"', 'password')");
			pst.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		DbConnection.disconnect();
    }
	
	//======================================== INGREDIENT GUI ==================================\\
	
	//view list of inventory items
	public static void getIngredients() {
		DbConnection.connect();
		try {
			PreparedStatement pst = DbConnection.con.prepareStatement("select * from ingredient");
			ResultSet rs = pst.executeQuery();
			IngredientGUI.tblView.setModel(DbUtils.resultSetToTableModel(rs));
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		DbConnection.disconnect();
	}
	
	// sort A to Z
    public static void sortAtoZ() {
    	DbConnection.connect();
    	try {
			PreparedStatement pst = DbConnection.con.prepareStatement("Select * from ingredient order by name");
			ResultSet rs = pst.executeQuery();
			IngredientGUI.tblView.setModel(DbUtils.resultSetToTableModel(rs));
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		DbConnection.disconnect();
    }
    
    // sort by date in descending order
    public static void sortZtoA() {
    	DbConnection.connect();
    	try {
			PreparedStatement pst = DbConnection.con.prepareStatement("Select * from ingredient order by name desc");
			ResultSet rs = pst.executeQuery();
			IngredientGUI.tblView.setModel(DbUtils.resultSetToTableModel(rs));
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		DbConnection.disconnect();
    }
    
    // add ingredient
    public static void addIngredient(String name, String unitOfMeasure, int reOrderPoint) {
    	DbConnection.connect();
    	try {
			PreparedStatement pst = DbConnection.con.prepareStatement("insert into ingredient (name, unitOfMeasure, unitsOnHand, reOrderPoint) values('"+name+"', '"+unitOfMeasure+"', 0, " + reOrderPoint + ")");
			pst.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		DbConnection.disconnect();
    }
     
    // add ingredient
    public static void updateQty(String name, int unitQty) {
    	DbConnection.connect();
    	try {PreparedStatement pst1 = DbConnection.con.prepareStatement("select * from ingredient where name = '" + name + "'");
    		ResultSet rs1 = pst1.executeQuery();
    		int newQuantity = rs1.getInt("unitsOnHand") + unitQty;
    		
			PreparedStatement pst = DbConnection.con.prepareStatement("update ingredient set name = '"+name+"', unitsOnHand = "+newQuantity);
			pst.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		DbConnection.disconnect();
    }
    

	//======================================== Purchase GUI ======================================\\
	
	//view list of purchases
	public static void getPurchases() {
		DbConnection.connect();
		try {
			PreparedStatement pst = DbConnection.con.prepareStatement("select * from purchase");
			ResultSet rs = pst.executeQuery();
			PurchaseGUI.tblView.setModel(DbUtils.resultSetToTableModel(rs));
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		DbConnection.disconnect();
	}
	
	// get purchase items
	public static void getPurchaseItems(int purchaseID) {
		DbConnection.connect();
    	try {
		PreparedStatement pst = DbConnection.con.prepareStatement("Select * from purchaseItem where purchaseID = " + purchaseID);
		ResultSet rs = pst.executeQuery();
		PurchaseGUI.tblItems.setModel(DbUtils.resultSetToTableModel(rs));
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		DbConnection.disconnect();
	}
	
	// get Employee ID
	public static int getEmployeeID(String user) {
		DbConnection.connect();
		int employeeID = 0;
    	try {
		PreparedStatement pst = DbConnection.con.prepareStatement("Select * from userLogin where username = '" + user + "'");
		ResultSet rs = pst.executeQuery();
		employeeID = rs.getInt("employeeID");
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		DbConnection.disconnect();
		
		return employeeID;
	}
	
	//get Purchase ID
	public static int getPurchaseID() {
		int purchaseID = 0;
		DbConnection.connect();
		try {
		PreparedStatement pst = DbConnection.con.prepareStatement("Select * from purchase");
		ResultSet rs = pst.executeQuery();
		while (rs.next()) {
			purchaseID = rs.getInt("purchaseID");
		}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		DbConnection.disconnect();
		
		return purchaseID;
	}
	
	
	// add purchase
	public static void addPurchase(String date, int employeeID) {
		DbConnection.connect();
    	try {
			PreparedStatement pst = DbConnection.con.prepareStatement("insert into purchase (date, employeeID) values('"+date+"', " + employeeID);
			pst.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		DbConnection.disconnect();
	}
	
	//add purchase item
	public static void addPurchaseItem(int purchaseID, String name, double price, int qty) {
		DbConnection.connect();
    	try {
			PreparedStatement pst = DbConnection.con.prepareStatement("insert into purchaseItem (purchaseID, name, unitPrice, unitQty) values(" + purchaseID + ", '" + name + "', " + price + ", " + qty);
			pst.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		DbConnection.disconnect();
	}
	
	
	
	// sort by date
	    public static void setDateRange(String date1, String date2) {
	    	DbConnection.connect();
	    	try {
			PreparedStatement pst = DbConnection.con.prepareStatement("Select * from purchase where date >= \"" + date1 + "\" and date <= \"" + date2 + "\"");
			ResultSet rs = pst.executeQuery();
			PurchaseGUI.tblView.setModel(DbUtils.resultSetToTableModel(rs));
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			DbConnection.disconnect();
	    }
	
	// sort by date in ascending order
	    public static void mostRecentLast() {
	    	DbConnection.connect();
	    	try {
			PreparedStatement pst = DbConnection.con.prepareStatement("Select * from purchase order by date");
			ResultSet rs = pst.executeQuery();
			PurchaseGUI.tblView.setModel(DbUtils.resultSetToTableModel(rs));
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			DbConnection.disconnect();
	    }
	    
	 // sort by date in descending order
	    public static void mostRecentFirst() {
	    	DbConnection.connect();
	    	try {
			PreparedStatement pst = DbConnection.con.prepareStatement("Select * from purchase order by date desc");
			ResultSet rs = pst.executeQuery();
			PurchaseGUI.tblView.setModel(DbUtils.resultSetToTableModel(rs));
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			DbConnection.disconnect();
	    }
	    
	    
	// JComboBox
	    public static void getUserCB() {
	    	DbConnection.connect();
			try {
				PreparedStatement pst = DbConnection.con.prepareStatement("select * from ingredient order by name");
				ResultSet rs = pst.executeQuery();
				while (rs.next()) {
					String name = rs.getString("name");
					PurchaseGUI.cbName.addItem(name);
				}
				
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
			DbConnection.disconnect();
	    }
	    
	 // delete purchase
	    public static void deletePurchase(int purchaseID) {
			DbConnection.connect();
	    	try {
				PreparedStatement pst = DbConnection.con.prepareStatement("delete from purchase where purchaseID = " + purchaseID);
				pst.executeUpdate();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			DbConnection.disconnect();
		}
	    
	  
	    
}
