package algebra;

import expressions.Exponent;
import expressions.Expression;
import expressions.Number;
import expressions.Product;
import expressions.Sum;
import expressions.Variable;
import simplification.LikeTerms;
import technical.Operators;

public class Polynomial {

	public static boolean isPolynomial(Expression expression) {

		if (expression.operator == Operators.NONE || expression.isConstant) {
			return true;
		}

		if (expression.operator == Operators.ADDITION || expression.operator == Operators.MULTIPLICATION) {
			boolean componentsPolynomial = true;
			for (Expression sub : expression.expressions) {
				componentsPolynomial &= isPolynomial(sub);
			}
			if (!componentsPolynomial) {
				return false;
			}
		}

		if (expression.operator == Operators.ADDITION) {
			return true;
		} else if (expression.operator == Operators.MULTIPLICATION) {
			return true;
		} else if (expression.operator == Operators.EXPONENT) {
			boolean baseIsPolynomial = isPolynomial(expression.expressions.get(0));
			boolean exponentIsN0;
			if (!expression.expressions.get(1).isConstant) {
				exponentIsN0 = false;
			} else {
				double exponent = expression.expressions.get(1).numericalValue;
				exponentIsN0 = (exponent == (long) exponent && exponent >= 0);
			}
			return baseIsPolynomial && exponentIsN0;
		}

		return false;

	}

	public static Expression expandPolynomial(Expression expression, boolean showWork) {

		Expression expr = expression.copy();

		expr = LikeTerms.combineLikeTerms(expr, showWork);

		if (expr.operator == Operators.NONE || expr.isConstant) {
			return expr;
		}

		if (expr.operator == Operators.ADDITION) {
			Expression expanded = new Sum();
			for (Expression sub : expr.expressions) {
				expanded.append(expandPolynomial(sub, showWork));
			}
			expanded = LikeTerms.combineLikeTerms(expanded, showWork);
			expr = expanded;
		} else if (expr.operator == Operators.MULTIPLICATION) {
			expr = Product.expandedProduct(expr, showWork);
		}

		expr = LikeTerms.combineLikeTerms(expr, showWork);

		if (!expression.toString().equals(expr.toString())) {
			expr = expandPolynomial(expr, showWork);
		}

		return expr;
	}

	public static int degree(Expression expression) {
		if (expression instanceof Number) {
			return 0;
		} else if (expression instanceof Variable) {
			return 1;
		} else if (expression instanceof Sum) {
			int maxDegree = 0;
			for (Expression sub : expression.expressions) {
				maxDegree = Math.max(degree(sub), maxDegree);
			}
			return maxDegree;
		} else if (expression instanceof Product) {
			int degree = 0;
			for (Expression sub : expression.expressions) {
				degree += degree(sub);
			}
			return degree;
		} else if (expression instanceof Exponent) {
			return (int) (degree(expression.expressions.get(0)) * expression.expressions.get(1).numericalValue);
		}
		return 0;
	}

	public static boolean isExpandedForm(Expression expression) {
		if (!(expression instanceof Sum)) {
			if (expression instanceof Variable) return true;
			else if (expression.isConstant) return true;
			else if (expression instanceof Product && productIsPolynomialTerm(expression)) return true;
			else if (expression instanceof Exponent && expression.expressions.get(0) instanceof Variable) return true;
			else return false;
		} else if (!isPolynomial(expression)) {
			return false;
		}
		for (Expression sub : expression.expressions) {
			if (!productIsPolynomialTerm(sub)) return false;
		}
		return true;
	}
	
	public static boolean productIsPolynomialTerm(Expression expression) {
		if (expression.expressions.size() != 2)
			return false;
		else {
			Expression exponentPart;
			if (expression.expressions.get(0).isConstant) {
				exponentPart = expression.expressions.get(1);
				boolean pass = false;
				if (exponentPart instanceof Variable) {
					pass = true;
				} else if (exponentPart instanceof Exponent
						&& exponentPart.expressions.get(0) instanceof Variable) {
					pass = true;
				}
				if (!pass)
					return false;
			} else if (expression.expressions.get(1).isConstant) {
				exponentPart = expression.expressions.get(0);
				boolean pass = false;
				if (exponentPart instanceof Variable) {
					pass = true;
				} else if (exponentPart instanceof Exponent
						&& exponentPart.expressions.get(0) instanceof Variable) {
					pass = true;
				}
				if (!pass)
					return false;
			} else {
				return false;
			}
		}
		return true;
	}

}
