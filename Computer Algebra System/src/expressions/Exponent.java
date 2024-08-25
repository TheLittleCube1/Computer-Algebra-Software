package expressions;

import java.util.List;

import technical.Operators;

public class Exponent extends Expression {
	
	public Exponent(Expression... expressions) {
		super(Operators.EXPONENT, expressions);
		termsSortable = false;
	}
	
	public Exponent(List<Expression> expressions) {
		super(Operators.EXPONENT, expressions);
		termsSortable = false;
	}

	@Override
	public double evaluate(double x) {
		return Math.pow(expressions.get(0).evaluate(x), expressions.get(1).evaluate(x));
	}
	
}
