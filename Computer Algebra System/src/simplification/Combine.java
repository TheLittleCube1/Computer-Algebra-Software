package simplification;

import expressions.Expression;
import expressions.Number;
import expressions.Product;
import expressions.Sum;
import main.Main;
import technical.Operators;

public class Combine {
	
	public static Expression combineConstants(Expression expr, boolean showWork) {
		
		int numbersCombined = 0;

		Expression expression = expr.copy();
		
		if (expression.operator != Operators.NONE) {
			for (int i = 0; i < expression.expressions.size(); i++) {
				expression.expressions.set(i, combineConstants(expression.expressions.get(i), false));
			}
		}
		
		if (expression instanceof Sum) {
			double sumOfNumbers = 0;
			Expression finalExpression = new Sum();
			for (Expression sub : expression.expressions) {
				if (sub instanceof Number) {
					numbersCombined++;
					sumOfNumbers += ((Number) sub).constant;
				} else {
					finalExpression.append(combineConstants(sub, false));
				}
			}
			if (sumOfNumbers != 0) {
				finalExpression.append(new Number(sumOfNumbers));
			}
			expression = finalExpression;
		} else if (expression instanceof Product) {
			
			Expression combinedCoefficient = new Product(), finalExpression = new Product();
			
			double productOfNumbers = 1;
			for (Expression sub : expression.expressions) {
				if (sub instanceof Number) {
					numbersCombined++;
					productOfNumbers *= ((Number) sub).constant;
				} else if (sub.isConstant) {
					combinedCoefficient.append(combineConstants(sub, false));
				} else {
					finalExpression.append(combineConstants(sub, false));
				}
			}
			if (productOfNumbers != 1) {
				combinedCoefficient.append(0, new Number(productOfNumbers));
			}
			for (Expression constant : combinedCoefficient.expressions) {
				finalExpression.append(0, constant);
			}
			expression = finalExpression;
		}
		
		if (numbersCombined >= 2) expression = combineConstants(pruneNodes(expression), showWork);
		
		Expression finalExpression = pruneNodes(expression);
		if (showWork) System.out.println("Combined constants\n" + finalExpression + "\n");
		return finalExpression;
		
	}
	
	public static Expression pruneNodes(Expression expr) {
		Expression copy = expr.copy();
		
		if (copy.operator == Operators.NONE) {
			return copy;
		}
		
		if (copy.operator == Operators.ADDITION) {
			if (copy.expressions.size() == 0) {
				return Number.ZERO;
			} else if (copy.expressions.size() == 1) {
				return copy.expressions.get(0);
			} else {
				Expression pruned = new Sum();
				for (Expression sub : copy.expressions) {
					Expression prunedSub = pruneNodes(sub);
					if (prunedSub.operator == Operators.ADDITION) {
						pruned.appendAll(prunedSub.expressions);
					} else {
						pruned.append(prunedSub);
					}
				}
				return pruned;
			}
		} else if (copy.operator == Operators.MULTIPLICATION) {
			if (copy.expressions.size() == 1) {
				return copy.expressions.get(0);
			} else if (copy.expressions.size() == 0) {
				return Number.ONE;
			} else {
				Expression pruned = new Product();
				for (Expression sub : copy.expressions) {
					Expression prunedSub = pruneNodes(sub);
					if (prunedSub.operator == Operators.MULTIPLICATION) {
						pruned.appendAll(prunedSub.expressions);
					} else {
						pruned.append(prunedSub);
					}
				}
				return pruned;
			}
		}
		
		for (int i = 0; i < copy.expressions.size(); i++) {
			copy.expressions.set(i, pruneNodes(copy.expressions.get(i)));
		}
		
		return copy;
	}
	
}
