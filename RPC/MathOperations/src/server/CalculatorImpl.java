

import java.rmi.*;
import java.rmi.server.*;

public class CalculatorImpl extends UnicastRemoteObject implements Calculator {
	
	private static final long serialVersionUID = 1;

	public CalculatorImpl() throws RemoteException {
	}
	
	public String calculate(String input) throws RemoteException {
		System.out.println("Received message from client: " + input);
		 OperationStructure data = getRequestData(input);
		 OperationStructure result = doServerComputation(data);
		 String reply = buildReplyMessage(result);
		 System.out.println("Send message to client: " + reply);
		 return(reply);
	}
	
	private OperationStructure doServerComputation(OperationStructure data) {
		
		String operator = " ";
		int res = 0;
		
		switch (data.getOperator()) {
		 case "+" : 
			 operator = "R";
			 res = data.addition();
			 break;
		 case "-" : 
			 operator = "R";
			 res = data.substraction();
			 break;
		 case "*" : 
			 operator = "R";
			 res = data.multiplication();
			 break;
		 case "/" : 
			 operator = "R";
			 res = data.division();
			 break;
		 case "!" : 
			 operator = "!";
			 res = data.exit();
			 break;
		 }
		
		OperationStructure result = new OperationStructure(0,0, operator);
		result.setResult(res);
		return result;

	}
	
	private OperationStructure getRequestData(String request) {
		String[] parts = request.split(" ");
		
		String op = parts[0];
		int a,b;
		
		if(op.equals("!")) {
			a = 0;
			b = 0;
		} else {
			a = Integer.parseInt(parts[1]);
			b = Integer.parseInt(parts[2]);
		}
		
		OperationStructure data =  new OperationStructure(a, b, op);
		return data;
	}
	
	private String buildReplyMessage(OperationStructure result) {
		if(result.getOperator().equals("!")) {
			return "!";
		} else
			return result.getOperator() + " " + result.getResult();
	}
		
}
