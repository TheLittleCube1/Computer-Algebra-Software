package simplification;

import java.util.Comparator;

import algebra.Polynomial;
import expressions.Exponent;
import expressions.Expression;
import expressions.Product;
import expressions.Sum;
import technical.Operators;

class SortTermsByIncreasingWeight implements Comparator<Expression> {

	@Override
	public int compare(Expression term1, Expression term2) {
		return term1.weight() - term2.weight();
	}
	
}

class SortTermsByIncreasingDegree implements Comparator<Expression> {

	@Override
	public int compare(Expression term1, Expression term2) {
		int difference = Polynomial.degree(term1) - Polynomial.degree(term2);
		if (difference == 0) {
			return term1.weight() - term2.weight();
		} else {
			return difference;
		}
	}
	
}

class SortTermsByDecreasingDegree implements Comparator<Expression> {

	@Override
	public int compare(Expression term1, Expression term2) {
		int difference = Polynomial.degree(term1) - Polynomial.degree(term2);
		if (difference == 0) {
			return term1.weight() - term2.weight();
		} else {
			return difference;
		}
	}
	
}

public class TermSorter {
	
	public static Expression sortTerms(Expression expression) {
		if (expression.operator == Operators.NONE) return expression.copy();
		
		Expression copy = expression.copy();
		for (int i = 0; i < copy.expressions.size(); i++) {
			Expression sub = copy.expressions.get(i);
			copy.expressions.set(i, sortTerms(sub));
		}
		
		if (copy.termsSortable) return copy;
		else if (!Polynomial.isPolynomial(copy)) {
			copy.expressions.sort(new SortTermsByIncreasingWeight());
			return copy;
		} else if (Polynomial.isExpandedForm(copy) && copy instanceof Sum) {
			copy.expressions.sort(new SortTermsByDecreasingDegree());
		} else if (copy instanceof Product) {
			copy.expressions.sort(new SortTermsByIncreasingDegree());
		}
		
		return copy;
	}
	
}
