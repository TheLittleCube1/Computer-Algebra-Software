package expressions;

import java.util.List;

import simplification.LikeTerms;
import technical.Operators;
import technical.Toolbox;

public class Product extends Expression {
	
	public Product(Expression... expressions) {
		super(Operators.MULTIPLICATION, expressions);
	}
	
	public Product(List<Expression> expressions) {
		super(Operators.MULTIPLICATION, expressions);
	}

	@Override
	public double evaluate(double x) {
		double product = 1;
		for (Expression e : expressions) {
			product *= e.evaluate(x);
		}
		return product;
	}
	
	public static Expression expandedProduct(Expression expression, boolean showWork) {
		if (expression.expressions.size() == 0) return new Number(1);
		Expression finalExpression = expression.expressions.get(0);
		for (int i = 1; i < expression.expressions.size(); i++) {
			Expression nextTerm = expression.expressions.get(i), runningProduct = finalExpression.copy();
			if (!(nextTerm instanceof Sum)) {
				nextTerm = new Sum(expression.expressions.get(i));
			}
			if (!(runningProduct instanceof Sum)) {
				runningProduct = new Sum(runningProduct);
			}
			finalExpression = expandedProductOfTwoSums(runningProduct, nextTerm, false);
		}
		if (showWork) {
			Toolbox.showLineOfWork("Expand product", expression, finalExpression);
		}
		return finalExpression;
	}
	
	public static Expression expandedProductOfTwoSums(Expression sum1, Expression sum2, boolean showWork) {
		Expression expanded = new Sum();
		for (Expression a : sum1.expressions) {
			for (Expression b : sum2.expressions) {
				Expression term = new Product(a, b);
				expanded.append(term);
			}
		}
		if (showWork) {
			Toolbox.showLineOfWork("Expand product of two sums", new Product(sum1, sum2), expanded);
		}
		return LikeTerms.combineLikeTerms(expanded, showWork);
	}
	
}
