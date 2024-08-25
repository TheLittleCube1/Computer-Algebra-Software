package expressions;

import java.util.List;

import technical.Operators;

public class Quotient extends Expression {
	
	public Quotient(Expression... expressions) {
		super(Operators.DIVISION, expressions);
		termsSortable = false;
	}
	
	public Quotient(List<Expression> expressions) {
		super(Operators.DIVISION, expressions);
		termsSortable = false;
	}

	@Override
	public double evaluate(double x) {
		if (expressions.size() != 2) {
			return Number.INVALID_VALUE;
		}
		double numerator = expressions.get(0).evaluate(x);
		double denominator = expressions.get(1).evaluate(x);
		if (denominator == 0) {
			return Number.UNDEFINED;
		}
		return numerator / denominator;
	}
	
}
