package main;

import expressions.Expression;
import expressions.Number;
import expressions.Product;
import expressions.Sum;
import expressions.Variable;
import simplification.Simplify;

public class Main {
	
	public static final boolean SHOW_WORK = true;
	
	public static void main(String[] args) {
		
		Expression sum1 = new Sum(new Number(2), new Number(7), Variable.X);
		Expression sum2 = new Sum(Variable.X, new Product(new Number(2), Variable.X), new Number(3));
		Expression sum3 = new Sum(new Sum(Variable.X, new Number(2)), new Product(new Number(5), Variable.X), new Number(1));
		
		Expression input = new Sum(new Product(sum1, sum2, sum3), new Product(new Number(2), Variable.X));
		System.out.println("Input: " + input + "\n");
		
		Expression output = Simplify.simplifyExpression(input, SHOW_WORK);
		System.out.println("Output: " + output);
		
//		Expression input = new Quotient(new Sum(Variable.X, new Number(7)), new Product(new Number(3), new Sum(Variable.X, new Number(7))));
//		System.out.println("Input: " + input + "\n");
//		
//		Expression output = Laws.cancelTermsInQuotient(input);
//		System.out.println("Output: " + output + "\n");
		
	}
	
}
