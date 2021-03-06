package com.rossettimonicadigiorgio.winestoremanagementv2.backend.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.rossettimonicadigiorgio.winestoremanagementv2.backend.MySQLConnection;
import com.rossettimonicadigiorgio.winestoremanagementv2.classes.Order;
import com.rossettimonicadigiorgio.winestoremanagementv2.classes.StatusEnum;
import com.rossettimonicadigiorgio.winestoremanagementv2.classes.User;
import com.rossettimonicadigiorgio.winestoremanagementv2.classes.Wine;

/**
 * The {@code OrderController} is a class that defines:
 * controller for the order
 * 
 * @author 296666
 *
 */
public class OrderController {
	
	/**
	 * The method getAllOrders
	 * 
	 * @return the selected orders
	 */
	public static ArrayList<Order> getAllOrders() {
		try {
			Statement stmt =  MySQLConnection.establishConnection().createStatement();
			
			String query = "SELECT * FROM orders";
			
			ResultSet rset = stmt.executeQuery(query);
			
			ArrayList<Order> result = new ArrayList<Order>();
			
			while (rset.next()) {
				int idOrder = rset.getInt("IDOrder");
				int idStatus = rset.getInt("Status");
				int idUser = rset.getInt("User");
				
				User user = UserController.getUserByID(idUser);
				StatusEnum status = StatusEnum.values()[idStatus - 1];
				
				query = "SELECT * FROM orderwines WHERE OrderNumber = " + idOrder;
				
				Statement stmtWines = MySQLConnection.establishConnection().createStatement();
				ResultSet rsetWines = stmtWines.executeQuery(query);
				
				ArrayList<Wine> wines = new ArrayList<Wine>();
				
				while(rsetWines.next()) {
					int idWine = rsetWines.getInt("Wine");
					int orderBottles = rsetWines.getInt("BottlesNumber");
					
					Wine wine = WineController.getWineByID(idWine);
					
					wine.setBottlesNumber(orderBottles);
					
					wines.add(wine);
				}
				
				Order order = new Order(idOrder, status, user, wines);	
				
				result.add(order);
			}
			
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * The method getOrdersToShip
	 * 
	 * @return the orders to ship
	 */
	public static ArrayList<Order> getOrdersToShip() {
		try {
			Statement stmt =  MySQLConnection.establishConnection().createStatement();
			
			String query = "SELECT * FROM orders WHERE Status <> " + StatusEnum.Shipped.GetID();
			
			ResultSet rset = stmt.executeQuery(query);
			
			ArrayList<Order> result = new ArrayList<Order>();
			
			while (rset.next()) {
				int idOrder = rset.getInt("IDOrder");
				int idStatus = rset.getInt("Status");
				int idUser = rset.getInt("User");
				
				User user = UserController.getUserByID(idUser);
				StatusEnum status = StatusEnum.values()[idStatus - 1];
				
				query = "SELECT * FROM orderwines WHERE OrderNumber = " + idOrder;
				
				Statement stmtWines = MySQLConnection.establishConnection().createStatement();
				ResultSet rsetWines = stmtWines.executeQuery(query);
				
				ArrayList<Wine> wines = new ArrayList<Wine>();
				
				while(rsetWines.next()) {
					int idWine = rsetWines.getInt("Wine");
					int orderBottles = rsetWines.getInt("BottlesNumber");
					
					Wine wine = WineController.getWineByID(idWine);
					
					wine.setBottlesNumber(orderBottles);
					
					wines.add(wine);
				}
				
				Order order = new Order(idOrder, status, user, wines);	
				
				result.add(order);
			}
			
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * The method getLastOrderForUser
	 * allows to show at the user their last order
	 * 
	 * @param idUser the id of the user
	 * @return the last order for the user
	 */
	public static Order getLastOrderForUser(int idUser) {
		try {
			Statement stmt =  MySQLConnection.establishConnection().createStatement();
			
			String query = "SELECT * FROM orders WHERE User = " + idUser + " ORDER BY IDOrder DESC LIMIT 1";
			
			ResultSet rset = stmt.executeQuery(query);
			
			rset.next();
			
			int idOrder = rset.getInt("IDOrder");
			int idStatus = rset.getInt("Status");
			
			User user = UserController.getUserByID(idUser);
			StatusEnum status = StatusEnum.values()[idStatus - 1];
			
			query = "SELECT * FROM orderwines WHERE OrderNumber = " + idOrder;
			
			Statement stmtWines = MySQLConnection.establishConnection().createStatement();
			ResultSet rsetWines = stmtWines.executeQuery(query);
			
			ArrayList<Wine> wines = new ArrayList<Wine>();
			
			while(rsetWines.next()) {
				int idWine = rsetWines.getInt("Wine");
				int orderBottles = rsetWines.getInt("BottlesNumber");
				
				Wine wine = WineController.getWineByID(idWine);
				
				wine.setBottlesNumber(orderBottles);
				
				wines.add(wine);
			}
			
			Order order = new Order(idOrder, status, user, wines);	
			
			return order;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * The method shipOrder
	 * allows to update the status of the order (to shipped)
	 * 
	 * @param idOrder the id of the order
	 * @return if the order has been updated
	 */
	public static boolean shipOrder(int idOrder) {
		try {
			Statement stmt =  MySQLConnection.establishConnection().createStatement();
			
			String query = "UPDATE orders SET Status = " + StatusEnum.Shipped.GetID() + " WHERE IDOrder = " + idOrder;
			
			stmt.executeUpdate(query);
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * The method insertOrder
	 * allows to insert an order
	 * 
	 * @param orderToInsert the order to insert
	 * @return the inserted order
	 */
	public static Order insertOrder(Order orderToInsert) {
		try {
			String insertOrderQuery = "INSERT INTO orders (Status, User) VALUES (?, ?)";
			PreparedStatement insertOrderStatement = MySQLConnection.establishConnection().prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS);
			
			insertOrderStatement.setInt(1, orderToInsert.getStatus().GetID());
			insertOrderStatement.setInt(2, orderToInsert.getUser().getIDPerson());
			
			insertOrderStatement.execute();
			
			int idOrder = 0;
			
	        try (ResultSet generatedKeys = insertOrderStatement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	            	idOrder = generatedKeys.getInt(1);
	            }
	        }
			
			ArrayList<Wine> orderedAvailableWines = new ArrayList<Wine>(); 
			
			for (Wine wine : orderToInsert.getWines()) {
				Wine updatedWine = WineController.getWineByID(wine.getIDWine());
				
				if(updatedWine.ProcessOrder(wine.getBottlesNumber())) {
					orderedAvailableWines.add(wine);
					
					addOrderWines(idOrder, wine);
					WineController.updateWine(updatedWine);
				}	
			}
			
			Order order = new Order(idOrder, orderToInsert.getStatus(), orderToInsert.getUser(), orderedAvailableWines);
			
			return order;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * The method addOrderWines
	 * allows to add the wines to the order list
	 * 
	 * @param idOrder the id of the order
	 * @param wine that we want to add
	 */
	private static void addOrderWines(int idOrder, Wine wine) {
		try {
			String insertOrderQuery = "INSERT INTO orderwines (Wine, OrderNumber, BottlesNumber) VALUES (?, ?, ?)";
			PreparedStatement insertOrderStatement = MySQLConnection.establishConnection().prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS);
			
			insertOrderStatement.setInt(1, wine.getIDWine());
			insertOrderStatement.setInt(2, idOrder);
			insertOrderStatement.setInt(3, wine.getBottlesNumber());
			
			insertOrderStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
