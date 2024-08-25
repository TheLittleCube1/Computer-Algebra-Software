package algebra;

import expressions.Exponent;
import expressions.Expression;
import expressions.Number;
import expressions.Product;
import expressions.Quotient;
import expressions.Sum;
import simplification.Combine;
import simplification.LikeTerms;
import technical.Flags;
import technical.Operators;
import technical.Toolbox;

public class Laws {
	
	// EXPONENT LAWS ============================================================================================
	
	public static boolean sameBase(Expression expr1, Expression expr2) {
		long hash1 = expr1.hash(), hash2 = expr2.hash();
		if (expr1 instanceof Exponent) hash1 = expr1.expressions.get(0).hash();
		if (expr2 instanceof Exponent) hash2 = expr2.expressions.get(0).hash();
		return hash1 == hash2;
	}
	
	public static Expression multiplyExponents(Expression expr1, Expression expr2) {
		Expression exponent1 = Number.ONE, exponent2 = Number.ONE, finalExponent;
		Expression base = expr1;
		if (expr1 instanceof Exponent) {
			base = expr1.expressions.get(0);
			exponent1 = expr1.expressions.get(1);
		}
		if (expr2 instanceof Exponent) exponent1 = expr2.expressions.get(1);
		finalExponent = LikeTerms.shallowCombineLikeTerms(new Sum(exponent1, exponent2), false);
		return new Exponent(base, finalExponent);
	}
	
	public static Expression multiplyAllExponents(Expression expression, boolean showWork) {
		if (expression.operator == Operators.NONE) return expression.copy();
		
		Expression copy = expression.copy();
		for (int i = 0; i < copy.expressions.size(); i++) {
			Expression sub = copy.expressions.get(i);
			copy.expressions.set(i, multiplyAllExponents(sub, false));
		}
		Expression finalExpression = shallowMultiplyExponents(copy, showWork);
		if (showWork) {
			Toolbox.showLineOfWork("Product to exponents", expression, finalExpression);
		}
		return finalExpression;
	}
	
	public static Expression shallowMultiplyExponents(Expression expression, boolean showWork) {
		if (!(expression instanceof Product)) {
			return expression.copy();
		}
		Expression copy = expression.copy();
		copy = Combine.combineConstants(copy, false);
		int flag = Flags.DID_NOTHING;
		while (true) {
			flag = Flags.DID_NOTHING;
			for (int i = 0; i < copy.expressions.size(); i++) {
				for (int j = i + 1; j < copy.expressions.size(); j++) {
					Expression term1 = copy.expressions.get(i);
					Expression term2 = copy.expressions.get(j);
					if (sameBase(term1, term2)) {
						copy.expressions.set(i, multiplyExponents(term1, term2));
						copy.expressions.remove(j);
						j--;
						flag = Flags.SUCCESSFUL;
					}
				}
			}
			if (flag == Flags.DID_NOTHING) {
				break;
			}
		}
		Expression finalExpression = Combine.combineConstants(copy, false);
		return finalExpression;
	}
	
	// ==========================================================================================================
	
	// QUOTIENT LAWS
	
	public static Expression cancelTermsInQuotient(Expression expr) {
		if (!(expr instanceof Quotient)) {
			return expr.copy();
		}
		Expression copy = expr.copy();
		Expression numerator = copy.expressions.get(0);
		Expression denominator = copy.expressions.get(1);
		if (!(numerator instanceof Product)) numerator = new Product(numerator);
		if (!(denominator instanceof Product)) denominator = new Product(denominator);
		for (int numeratorIndex = 0; numeratorIndex < numerator.expressions.size(); numeratorIndex++) {
			for (int denominatorIndex = 0; denominatorIndex < denominator.expressions.size(); denominatorIndex++) {
				Expression numeratorTerm = numerator.expressions.get(numeratorIndex);
				Expression denominatorTerm = denominator.expressions.get(denominatorIndex);
				if (numeratorTerm.hash() == denominatorTerm.hash()) {
					numerator.expressions.remove(numeratorIndex);
					denominator.expressions.remove(denominatorIndex);
					numeratorIndex--;
					break;
				}
			}
		}
		return Combine.pruneNodes(new Quotient(numerator, denominator));
	}
	
}
