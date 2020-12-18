package com.bham.pij.assignments.calculator;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Calculator cal = new Calculator();
		String exp = "";
		int hisIndex = 0;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Please enter an expression..");
		exp = sc.nextLine();
		
		do{
					
			float result = cal.evaluate(exp);
			
			System.out.println("Result is: " + result);
			
			System.out.println("Choose one of the following options..."
					+ "\nm - store the most recent calculator result"
					+ "\nmr - print to the console the stored memory value (or zero if no value has been stored"
					+ "\nc - clear memory by setting it to zero"
					+ "\nh - press h for history");
			exp = sc.nextLine();
		
			switch(exp) {
			
				case "m":
					cal.setMemoryValue(result);
					System.out.println("Saved to memory");				
					cal.getMemoryValue();
					break;
					
				case "mr":
					System.out.println(cal.getMemoryValue());
					break;
									
				case "c":
					cal.clearMemory();
					break;
					
				case "h":
					cal.getHistory();
					break;
									
			}
			
			System.out.println("Choose an index to get history. ");
			hisIndex = Integer.parseInt(sc.nextLine());
			System.out.println("History at index " + hisIndex + " = " + cal.getHistoryValue(hisIndex));
			
			System.out.println("Please enter an expression.. or press 'x' to quit");
			exp = sc.nextLine();
			
		}
		while(!exp.equals("x")); 
		
		
		
	}

}
