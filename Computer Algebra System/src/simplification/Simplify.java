package simplification;

import algebra.Polynomial;
import expressions.Expression;

public class Simplify {
	
	public static Expression simplifyExpression(Expression expression, boolean showWork) {
		Expression expr = expression.copy();
		expr = LikeTerms.combineLikeTerms(expr, showWork);
		
		if (Polynomial.isPolynomial(expr)) expr = Polynomial.expandPolynomial(expr, showWork);
		
		return expr;
	}
	
}
