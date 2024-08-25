package expressions;

import java.util.ArrayList;

import technical.Operators;

public class Number extends Expression {

	public static final double NULL_VALUE = 9.1375e14, INVALID_VALUE = 7.463245e15, UNDEFINED = 8.627384123e15;
	public static final Number ZERO = new Number(0), ONE = new Number(1), NULL = new Number(NULL_VALUE);
	
	public double constant;
	
	public Number(double constant) {
		super(Operators.NONE, new ArrayList<Expression>());
		append(this);
		this.constant = constant;
		this.isConstant = true;
		this.numericalValue = constant;
	}

	@Override
	public double evaluate(double x) {
		return constant;
	}
	
}
