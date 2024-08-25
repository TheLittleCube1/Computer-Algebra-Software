package simplification;

import algebra.Laws;
import expressions.Expression;
import expressions.Number;
import expressions.Product;
import expressions.Sum;
import technical.Flags;
import technical.Operators;
import technical.Toolbox;

public class LikeTerms {
	
	public static boolean likeTerms(Expression left, Expression right) {
		
		Expression noConstantsLeft = variablePart(left), noConstantsRight = variablePart(right);
		
		noConstantsLeft.sortByHash();
		noConstantsRight.sortByHash();
		
		String strLeft = noConstantsLeft.toString();
		String strRight = noConstantsRight.toString();
		
		return strLeft.equals(strRight);
	}
	
	public static Expression coefficient(Expression expression) {
		
		if (expression.isConstant) {
			return expression;
		}
		
		if (expression.operator != Operators.MULTIPLICATION) {
			return new Number(1);
		}
		
		Expression coefficient = new Product();
		for (Expression e : expression.expressions) {
			if (e.isConstant) {
				coefficient.append(e);
			}
		}
		
		if (coefficient.expressions.size() == 1) return coefficient.expressions.get(0);
		else return coefficient;
		
	}
	
	public static Expression variablePart(Expression expression) {
		
		Expression copy = expression.copy(), official;
		if (copy.operator != Operators.MULTIPLICATION) {
			official = new Product(copy);
		} else {
			official = copy;
		}
		
		for (int i = 0; i < official.expressions.size(); i++) {
			Expression sub = official.expressions.get(i);
			if (sub.isConstant) {
				official.expressions.remove(i);
				i--;
			}
		}
		
		if (official.expressions.size() == 1) return official.expressions.get(0);
		else return official;
		
	}
	
	public static Expression combineLikeTerms(Expression expression, boolean showWork) {
		if (expression.operator == Operators.NONE) return expression.copy();
		
		Expression copy = expression.copy();
		copy = shallowCombineLikeTerms(copy, false);
		copy = Laws.multiplyAllExponents(copy, false);
		for (int i = 0; i < copy.expressions.size(); i++) {
			Expression sub = copy.expressions.get(i);
			copy.expressions.set(i, combineLikeTerms(sub, false));
		}
		Expression combined = shallowCombineLikeTerms(copy, false);
		Expression finalExpression = TermSorter.sortTerms(Laws.multiplyAllExponents(combined, false));
		if (showWork) {
			Toolbox.showLineOfWork("Combine like terms", expression, finalExpression);
		}
		return finalExpression;
	}
	
	public static Expression shallowCombineLikeTerms(Expression expression, boolean showWork) {
		if (!(expression instanceof Sum)) {
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
					if (likeTerms(term1, term2)) {
						copy.expressions.set(i, combineTwoLikeTerms(term1, term2, false));
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
	
	public static Expression combineManyLikeTerms(Expression...terms) {
		Expression combined = terms[0];
		for (int i = 1; i < terms.length; i++) {
			combined = combineTwoLikeTerms(combined, terms[i], false);
		}
		return combined;
	}
	
	public static Expression combineTwoLikeTerms(Expression term1, Expression term2, boolean showWork) {
		
		Expression copy1 = term1.copy(), copy2 = term2.copy();
		
		Expression coefficient1 = coefficient(copy1);
		Expression coefficient2 = coefficient(copy2);
		Expression variablePart1 = variablePart(copy1);
		
		if (variablePart1.expressions.size() == 0) {
			return new Sum(coefficient1, coefficient2);
		}
		
		Expression combinedTerm;
		if (coefficient1 instanceof Sum) {
			coefficient1.append(coefficient2);
			combinedTerm = new Product(coefficient1, variablePart1);
		} else if (coefficient2 instanceof Sum) {
			coefficient2.append(coefficient1);
			combinedTerm = new Product(coefficient2, variablePart1);
		} else {
			combinedTerm = new Product(new Sum(coefficient1, coefficient2), variablePart1);
		}
		
		return combinedTerm;
		
	}

}
