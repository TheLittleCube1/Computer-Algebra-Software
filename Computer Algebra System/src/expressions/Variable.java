package expressions;

import java.util.ArrayList;

import technical.Operators;

public class Variable extends Expression {
	
	public Variable() {
		super(Operators.NONE, new ArrayList<Expression>());
		append(this);
		isConstant = false;
	}

	public static final Variable X = new Variable();

	@Override
	public double evaluate(double x) {
		return x;
	}
	
}
