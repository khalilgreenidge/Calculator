package com.bham.pij.assignments.calculator;

import java.util.ArrayList;

public class Calculator {

	private float currentValue;
	private float memoryValue;
	private static ArrayList<String> history; 
	
	public Calculator() {
		currentValue = 0;
		memoryValue = 0;
		history = new ArrayList<String>(1000);
	}
	
	public float evaluate (String expression) {
		
		String[] infix = expression.split(" ");
		boolean parentheses = false;
		float operand1 = 0, operand2 = 0, oper1 = 0, oper2 = 0;
		boolean useMemory = false;
		String operator = "";
		ArrayList <String> postfix = new ArrayList<String>(expression.length());
				
		if(infix.length < 2) {
			System.out.println("Invalid input. You only entered 1 number");
			currentValue = Float.MIN_VALUE;
			return currentValue;
		}
		
		//Search expression for errors with space
		for(int i=0;i<expression.length();i++) {

			//System.out.println("Current char: " + expression.charAt(i) + " i = " + i);
			
			//check for spaces between brackets and adjacent numbers
			if(i > 0){
				if( (expression.charAt(i-1) == '(' && expression.charAt(i) == ' ') || (expression.charAt(i-1) == ')' && expression.charAt(i) != ' ')  ) {
					System.out.println("Invalid input. " + "Brackets should not be separated from their adjacent number by space. ");
					currentValue = Float.MIN_VALUE;
					return currentValue;
				}

				//check for space after the operator
				if(  (expression.charAt(i-1) == '*' || expression.charAt(i-1) == '/' || expression.charAt(i-1) == '+' || expression.charAt(i-1) == '-') && expression.charAt(i) != ' ' ){                                           
					System.out.println("Invalid input. " + " There should be a space after the operator.");
					currentValue = Float.MIN_VALUE;	
					return currentValue;
				}
				
				//check for a space before the operator
				if(  (expression.charAt(i) == '*' || expression.charAt(i) == '/' || expression.charAt(i) == '+' || expression.charAt(i) == '-') && expression.charAt(i-1) != ' ') {

					//operator between the brackets should contain a space
					System.out.println("Invalid input. " + " There should be a space before the operator.");
					currentValue = Float.MIN_VALUE;
					return currentValue;
					
				}
			
				
				
			}
			
			if(expression.charAt(i) == '(' || expression.charAt(i) == ')' ) {
				parentheses = true;
			}
			
			if(Character.isLetter(expression.charAt(i))) {
				System.out.println("Invalid input. " + "No letters allows");
				currentValue = Float.MIN_VALUE;
				return currentValue;
			}
			
			
			if(i > 1) {
				if(expression.charAt(i) == '0' && expression.charAt(i-2) == '/' ) {
					System.out.println("Invalid input. " + "Divide by zero error");
					currentValue = Float.MIN_VALUE;
					return currentValue;
				}
			}
			
		}
		
		
		//VALID
		//check if expression contains parentheses
		if(!parentheses) {
			
			if( (expression.charAt(0) == '*' || expression.charAt(0) == '/' || expression.charAt(0) == '+' || expression.charAt(0) == '-') && infix.length == 2){
				operand1 = getMemoryValue();
				useMemory = true;
				operator = String.valueOf(expression.charAt(0));
				operand2 = Float.parseFloat(String.valueOf(infix[1].charAt(0)));
				currentValue = arithmetic(operand1, operator, operand2);
			}
			else {
				postfix = rPN(infix);
				int len = postfix.size();	
				//System.out.println("The RPN of \"" + expression + "\" is \"" + postfix + "\"");			
				currentValue = evaluateRPN(postfix);
			}
			
		}
		else if(infix.length == 7) {
			//make sure the format is 7 elements  -  (2 * 10) - (3.85 + 12)
			//System.out.println("Parentheses! ");
			
			expression = expression.replaceAll("[()]", "");			
			infix = expression.split(" ");
			
			//printArray(infix);
			//split in half
			String op1 = "", operStr2 = "";
			
			//operand1
			
			//System.out.println("Oper1: " + infix[0]);
			oper1 = Float.parseFloat(String.valueOf(infix[0]));
			//System.out.println("Oper1: " + oper1);
			
			op1 = infix[1];
			
			//System.out.println("Oper2: " + infix[2]);
			oper2 = Float.parseFloat(String.valueOf(infix[2]));
			//System.out.println("Oper2: " + oper2);
			
			operand1 = arithmetic(oper1, op1, oper2);
			//System.out.println("Operand 1: " + operand1);
			
			//main operator
			operator = infix[3];
			//System.out.println("Operator: " + operator);

			//operand 2
			oper1 = Float.parseFloat(String.valueOf(infix[4]));
			//System.out.println("Oper1-2: " + oper1);
			op1 = infix[5];
			oper2 = Float.parseFloat(String.valueOf(infix[6]));
			//System.out.println("Oper2-2: " + oper2);
			
			operand2 = arithmetic(oper1, op1, oper2);
			//System.out.println("Operand 2: " + operand2);
			
			
			currentValue = arithmetic(operand1, operator, operand2);
			
		}
	
		else if(infix.length != 7){
			System.out.println("Invalid format. Only two pairs of parentheses allowed.");
			currentValue = 0;
		}
				
		
		history.add(String.valueOf(currentValue));

		return currentValue;		
	}
	
	public static float arithmetic(float operand1, String operator, float operand2) {
		
		float ans = 0;
		
		switch(operator) {

			case "*":
				ans = operand1 * operand2;
				break;
	
			case "/":
				ans = operand1 / operand2;
				break;
	
			case "+":
				ans = operand1 + operand2;
				break;
	
			case "-":
				ans = operand1 - operand2;
				break;
	
		}
		
		return ans;
		
	}
	
	public static int precedence(char op) {

		int pres = 0;

		switch(op) {

		case '/':
			pres = 3;
			break;

		case '*':
			pres = 3;
			break;

		case '+':
			pres = 2;
			break;

		case '-':
			pres = 2;
			break;

		}

		return pres;
	}
	
	public static ArrayList<String> rPN(String[] expression) {
		
		//String [] infix = expression.split(" ");
		ArrayList <String> postfix = new ArrayList<String>(expression.length);
		ArrayList<String> operatorStack = new ArrayList<String>(expression.length / 2);
		//System.out.println("the current infix/expression is " );
		//printArray(expression);
		//Shunting yard algorithm
		
				
		for(int i=0;i<expression.length; i++) {  //while there are tokens to be read
			
			String token = expression[i];
			//System.out.println("Token "+i+ " is " +token);
			//if token is a number
			if(isNumber(token)) {
				postfix.add(token);  //append number to post fix
			}

			else if(token.equals("/") || token.equals("*") || token.equals("+") || token.equals("-") ) {  // token is an operator or parentheses
				
				int j = 0;
				
				//get precedence of current token
				
				//while there's an operator at top of stack   AND ( precedence of operator at top >= precedence of current token 
				// 5,6,7
				// [0],[1],[2]
				//size = 3
				
				if(!operatorStack.isEmpty()) {
					int tp = precedence(token.charAt(0));
					int op = precedence(operatorStack.get(j).charAt(0));
					 
					while( (j < operatorStack.size()) && tp <= op && (!operatorStack.get(j).equals("("))  ){ 
						
						//System.out.println("index J = " + j + " , and size = " + operatorStack.size());	
						
						//System.out.println("Precedence of token: " + tp + ", Precedence of op:  " + op);												
						postfix.add(operatorStack.get(j));
						operatorStack.remove(j);
											
						
						if(j == operatorStack.size())
							break;
						else {
							op = precedence(operatorStack.get(j).charAt(0));							
						}
					}
				}
				
				
				operatorStack.add(0, token); //add new token to the top of the operator stack 
				
			}
			
			else if(token.equals("(") ){
				postfix.add(token);
			}
			
			else if(token.equals(")")){
				int k=0;
				while(!operatorStack.get(k).equals("(")) {
					postfix.add(operatorStack.get(k));
					operatorStack.remove(k);
				}
				if(operatorStack.get(k).equals("(")){
					operatorStack.remove(k);
				}
			}
			
						
		}
		
		//System.out.println("Output stack: " + postfix);
		
		//System.out.println("Operator Stack: " + operatorStack);
		
		
		//pop entire operator stack to the output stack
		postfix.addAll(operatorStack);
		//System.out.println("Entire stack added");
		operatorStack.clear();
		
		//System.out.println("Output stack: " + postfix);
		
		//System.out.println("Operator Stack: " + operatorStack);
		
		return postfix;
		
	}
	
	public static float evaluateRPN(ArrayList<String> postfix) {

		int len = postfix.size();	
		ArrayList <String> stack = new ArrayList<String>(len);
		float var0, var1, result;

		//evaluate RPN  -  462*+15-
		for(int i=0; i <postfix.size();i++) {

			switch(postfix.get(i)) {

			case "/":
				var0 = Float.parseFloat(stack.get(0));
				var1 = Float.parseFloat(stack.get(1));
				result = var1 / var0;
				stack.remove(0);
				stack.remove(0);
				stack.add(0, String.valueOf(result));
				break;

			case "*":
				var0 = Float.parseFloat(stack.get(0));
				var1 = Float.parseFloat(stack.get(1));
				result = var1 * var0;
				stack.remove(0);
				stack.remove(0);
				stack.add(0, String.valueOf(result));
				break;

			case "+":
				var0 = Float.parseFloat(stack.get(0));
				var1 = Float.parseFloat(stack.get(1));
				result = var1 + var0;
				stack.remove(0);
				stack.remove(0);
				stack.add(0, String.valueOf(result));
				break;

			case "-":
				var0 = Float.parseFloat(stack.get(0));
				var1 = Float.parseFloat(stack.get(1));
				result = var1 - var0;
				stack.remove(0);
				stack.remove(0);
				stack.add(0, String.valueOf(result));
				break;

			default:
				stack.add(0, postfix.get(i));

			}

			//System.out.println(stack);

		}

		return Float.parseFloat(stack.get(0));

	}
	
	public static void printArray(String[] array) {
		for(int sub=0; sub < array.length; sub++) {
			System.out.print(array[sub] + " ");
		}
		System.out.println();
	}
	
	public static void printArray(ArrayList<String> stack) {
		for(int sub=0; sub < stack.size(); sub++) {
			System.out.print(stack.get(sub) + ", ");
		}
		System.out.println();
		
	}

	public static boolean isNumber(String s) {
		
		try { 
			Float.parseFloat(s); 
		} catch(NumberFormatException e) { 
			return false; 
		}
		// only got here if we didn't return false
		return true;
	}
	
	public float getCurrentValue() {
		return currentValue;
	}
	
	public float getMemoryValue() {
		return memoryValue;
	}
	
	public void setMemoryValue(float memval) {
		memoryValue = memval;
	}
	
	public void clearMemory() {
		memoryValue = 0;
	}
	
	public void getHistory() {
		
		for(int i=0;i<history.size();i++) {
			System.out.print(history.get(i) + " ");
		}
	}
	
	public float getHistoryValue(int index) {
		
		return Float.parseFloat(history.get(index));
	}
	

}
