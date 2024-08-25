package expressions;

import java.util.List;

import technical.Operators;

public class Sum extends Expression {
	
	public Sum(Expression... expressions) {
		super(Operators.ADDITION, expressions);
	}
	
	public Sum(List<Expression> expressions) {
		super(Operators.ADDITION, expressions);
	}

	@Override
	public double evaluate(double x) {
		double sum = 0;
		for (Expression e : expressions) {
			sum += e.evaluate(x);
		}
		return sum;
	}
	
}
