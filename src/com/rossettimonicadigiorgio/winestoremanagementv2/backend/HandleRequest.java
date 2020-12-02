package com.rossettimonicadigiorgio.winestoremanagementv2.backend;

import com.rossettimonicadigiorgio.winestoremanagementv2.backend.controllers.AdministratorController;
import com.rossettimonicadigiorgio.winestoremanagementv2.backend.controllers.EmployeeController;
import com.rossettimonicadigiorgio.winestoremanagementv2.backend.controllers.NotificationController;
import com.rossettimonicadigiorgio.winestoremanagementv2.backend.controllers.OrderController;
import com.rossettimonicadigiorgio.winestoremanagementv2.backend.controllers.UserController;
import com.rossettimonicadigiorgio.winestoremanagementv2.backend.controllers.WineController;
import com.rossettimonicadigiorgio.winestoremanagementv2.classes.Employee;
import com.rossettimonicadigiorgio.winestoremanagementv2.classes.Notification;
import com.rossettimonicadigiorgio.winestoremanagementv2.classes.Order;
import com.rossettimonicadigiorgio.winestoremanagementv2.classes.Request;
import com.rossettimonicadigiorgio.winestoremanagementv2.classes.Response;
import com.rossettimonicadigiorgio.winestoremanagementv2.classes.User;

public class HandleRequest {
	static Response run (Request rq) {
		Response rs = null;
			
		switch(rq.getValue()) {
			case "administratorLogin":
				Object administratorLogin = (Object) AdministratorController.login((String)rq.getParams().get(0), (String)rq.getParams().get(1));
				rs = new Response(administratorLogin);
				break;
				
			case "employeeLogin":
				Object employeeLogin = (Object) EmployeeController.login((String)rq.getParams().get(0), (String)rq.getParams().get(1));
				rs = new Response(employeeLogin);
				break;
				
			case "userLogin":
				Object userLogin = (Object) UserController.login((String)rq.getParams().get(0), (String)rq.getParams().get(1));
				rs = new Response(userLogin);
				break;
				
			case "employeeRegister":
				Object employeeRegister = (Object) EmployeeController.register((Employee)rq.getParams().get(0));
				rs = new Response(employeeRegister);
				break;
				
			case "userRegister":
				Object userRegister = (Object) UserController.register((User)rq.getParams().get(0));
				rs = new Response(userRegister);
				break;
				
			case "filterWines":
				Object filterWines = (Object) WineController.getFilteredWines((String)rq.getParams().get(0));
				rs = new Response(filterWines);
				break;
				
			case "listWines":
				Object listWines = (Object) WineController.getAllWines();
				rs = new Response(listWines);
				break;
				
			case "listOrders":
				Object listOrders = (Object) OrderController.getAllOrders();
				rs = new Response(listOrders);
				break;
				
			case "listOrdersForUser":
				Object listOrdersForUser = (Object) OrderController.getLastOrderForUser((int)rq.getParams().get(0));
				rs = new Response(listOrdersForUser);
				break;
				
			case "insertOrder":
				Object insertOrder = (Object) OrderController.insertOrder(((Order)rq.getParams().get(0)));
				rs = new Response(insertOrder);
				break;

			case "listNotification":
				Object listNotification = (Object) NotificationController.getNotificationByUser(((int)rq.getParams().get(0)));
				rs = new Response(listNotification);
				break;
				
			case "insertNotification":
				Object insertNotification = (Object) NotificationController.insertNotification(((Notification)rq.getParams().get(0)));
				rs = new Response(insertNotification);
				break;
		}
		
		return rs;
	}
}