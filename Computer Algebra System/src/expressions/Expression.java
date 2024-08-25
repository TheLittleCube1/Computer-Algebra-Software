package expressions;

import java.util.ArrayList;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import technical.Operators;
import technical.Toolbox;

public class Expression {
	
	public boolean isConstant = false;
	public boolean termsSortable = true;
	public double numericalValue = Number.NULL_VALUE;
	public int label;
	
	public int operator;
	public List<Expression> expressions = new ArrayList<Expression>();
	
	public Expression(int operator, Expression...expressions) {
		this.operator = operator;
		this.expressions = Toolbox.arrayToList(expressions);
		isConstant = computeIsConstant();
		if (isConstant) {
			numericalValue = evaluate(0);
		}
	}
	
	public Expression(int operator, List<Expression> expressions) {
		this.operator = operator;
		this.expressions = expressions;
		isConstant = computeIsConstant();
		if (isConstant) {
			numericalValue = evaluate(0);
		}
	}
	
	public void append(Expression expr) {
		expressions.add(expr);
		computeIsConstant();
	}
	
	public void append(int index, Expression expr) {
		expressions.add(index, expr);
		computeIsConstant();
	}
	
	public void appendAll(List<Expression> exprList) {
		expressions.addAll(exprList);
		computeIsConstant();
	}
	
	public void remove(int index) {
		expressions.remove(index);
		computeIsConstant();
	}
	
	public boolean computeIsConstant() {
		if (this instanceof Number) {
			isConstant = true;
			return true;
		} else if (this instanceof Variable) {
			isConstant = false;
			return false;
		}
		for (Expression e : expressions) {
			boolean subconstant = e.computeIsConstant();
			if (!subconstant) {
				isConstant = false;
				return false;
			}
		}
		isConstant = true;
		return true;
	}
	
	public double evaluate(double x) {
		if (operator == Operators.ADDITION) {
			double sum = 0;
			for (Expression e : expressions) sum += e.evaluate(x);
			return sum;
		} else if (operator == Operators.MULTIPLICATION) {
			double product = 1;
			for (Expression e : expressions) product *= e.evaluate(x);
			return product;
		} else if (operator == Operators.DIVISION) {
			if (expressions.size() != 2) {
				System.out.println("Invalid division (" + expressions.size() + " expressions, expected 2)");
				return Number.INVALID_VALUE;
			}
			double numerator = expressions.get(0).evaluate(x);
			double denominator = expressions.get(1).evaluate(x);
			if (denominator == 0) {
				System.out.println("Invalid division (Dividing by 0)");
				return Number.INVALID_VALUE;
			}
			return numerator / denominator;
		} else if (operator == Operators.NONE) {
			if (expressions.size() != 1) {
				System.out.println("Invalid term (" + expressions.size() + " expressions, expected 1)");
				return Number.INVALID_VALUE;
			}
			return expressions.get(0).evaluate(x);
		} else if (operator == Operators.EXPONENT) {
			if (expressions.size() != 2) {
				System.out.println("Invalid exponent (" + expressions.size() + " expressions, expected 2)");
				return Number.INVALID_VALUE;
			}
			double base = expressions.get(0).evaluate(x);
			double exponent = expressions.get(1).evaluate(x);
			if (base == 0 && exponent == 0) {
				return 1;
			}
			return Math.pow(base, exponent);
		} else if (operator == Operators.DIVISION) {
			if (expressions.size() != 2) {
				System.out.println("Invalid quotient (" + expressions.size() + " expressions, expected 2)");
				return Number.INVALID_VALUE;
			}
			double numerator = expressions.get(0).evaluate(x);
			double denominator = expressions.get(1).evaluate(x);
			if (denominator == 0) {
				return Number.UNDEFINED;
			}
			return numerator / denominator;
		}
		return Number.INVALID_VALUE;
	}
	
	public Expression copy() {
		if (this instanceof Number) {
			return new Number(((Number) this).constant);
		} else if (this instanceof Variable) {
			return Variable.X;
		} else if (this instanceof Sum) {
			return new Sum(Toolbox.copyList(expressions));
		} else if (this instanceof Product) {
			return new Product(Toolbox.copyList(expressions));
		} else if (this instanceof Exponent) {
			return new Exponent(Toolbox.copyList(expressions));
		} else if (this instanceof Quotient) {
			return new Quotient(Toolbox.copyList(expressions));
		}
		return null;
	}
	
	public long hash() {
		if (operator == Operators.NONE && isConstant) {
			return Toolbox.hashDouble(((Number) expressions.get(0)).constant);
		} else if (operator == Operators.NONE && !isConstant) {
			return Toolbox.hashDouble(1782345.8791);
		} else if (operator == Operators.ADDITION) {
			long hash = expressions.get(0).hash();
			for (Expression e : expressions) {
				if (expressions.get(0) == e) continue;
				hash = Toolbox.hashAddition(hash, e.hash());
			}
			return hash;
		} else if (operator == Operators.MULTIPLICATION) {
			long hash = expressions.get(0).hash();
			for (Expression e : expressions) {
				if (expressions.get(0) == e) continue;
				hash = Toolbox.hashMultiplication(hash, e.hash());
			}
			return hash;
		} else if (operator == Operators.EXPONENT) {
			return Toolbox.hashExponent(expressions.get(0).hash(), expressions.get(1).hash());
		}
		return -1;
	}
	
	public int weight() {
		String str = toString();
		int weight = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != ' ') {
				weight++;
			}
		}
		return weight;
	}
	
	public static int bigIndex = 0;
	
	public void printStructure() {
		bigIndex = 0;
		printSubStructure();
		System.out.println("-------------------------------------------------------------------------------------------------------");
	}
	
	public void printSubStructure() {
		System.out.print("Expr" + label + ": ");
		String str = "";
		if (operator == Operators.ADDITION) {
			str += "Sum(";
		} else if (operator == Operators.MULTIPLICATION) {
			str += "Product(";
		} else if (operator == Operators.EXPONENT) {
			str += "Exponent(";
		} else if (operator == Operators.DIVISION) {
			str += "Quotient(";
		}
		
		Queue<Expression> queue = new LinkedList<Expression>();
		for (int i = 0; i < expressions.size(); i++) {
			Expression sub = expressions.get(i);
			if (sub.operator == Operators.NONE) str += sub;
			else {
				bigIndex++;
				str += "Expr" + bigIndex;
				queue.add(sub);
				sub.label = bigIndex;
			}
			if (i != expressions.size() - 1) str += ", ";
		}
		if (operator != Operators.NONE) str += ")";
		System.out.print(str + "\n");
		
		while (!queue.isEmpty()) {
			Expression e = queue.poll();
			e.printSubStructure();
		}
	}
	
	public String toString() {
		if (operator == Operators.NONE) {
			if (expressions.get(0) instanceof Number) {
				double c = ((Number) expressions.get(0)).constant;
				if ((long) c == c) {
					return "" + (long) c;
				}
				return "" + c;
			} else {
				return "x";
			}
		} else if (operator == Operators.ADDITION) {
			String str = "";
			for (int i = 0; i < expressions.size(); i++) {
				str += expressions.get(i);
				if (i != expressions.size() - 1) {
					str += " + ";
				}
			}
			return str + "";
		} else if (operator == Operators.MULTIPLICATION) {
			String str = "";
			for (int i = 0; i < expressions.size(); i++) {
				Expression e = expressions.get(i);
				boolean bracket = false;
				
				if (e.operator == Operators.ADDITION) {
					bracket = true;
				} else {
					if (i == 0) {
						bracket = false;
					} else if (Character.isDigit(e.toString().charAt(0))) {
						bracket = true;
					} else {
						char previousCharacter = expressions.get(i - 1).toString().charAt(expressions.get(i - 1).toString().length() - 1);
						if (Character.isDigit(previousCharacter)) {
							bracket = false;
						} else {
							bracket = true;
						}
					}
				}
				
				if (bracket) str += "(" + e + ")";
				else str += e;
			}
			return str;
		} else if (operator == Operators.EXPONENT) {
			String str = "";
			Expression base = expressions.get(0), exponent = expressions.get(1);
			if (base.operator != Operators.NONE) {
				str += "(" + base + ")";
			} else {
				str += base;
			}
			str += "^";
			if (exponent.operator != Operators.NONE) {
				str += "(" + exponent + ")";
			} else {
				str += exponent;
			}
			return str;
		} else if (operator == Operators.DIVISION) {
			String numerator = expressions.get(0).toString(), denominator = expressions.get(1).toString();
			String str = "";
			if (numerator.contains(" ")) {
				str += "(" + numerator + ")/";
			} else {
				str += numerator + "/";
			}
			if (denominator.contains(" ")) {
				str += "(" + denominator + ")";
			} else {
				str += denominator + "";
			}
			return str;
		}
		return "Invalid expression";
	}
	
	public Expression sortedByHash() {
		Expression sorted = this.copy();
		sorted.sortByHash();
		return sorted;
	}
	
	public void sortByHash() {
		if (operator == Operators.NONE) return;
		for (Expression subexpression : this.expressions) {
			subexpression.sortByHash();
		}
		if (operator == Operators.EXPONENT) return;
		expressions.sort(new CompareByHash());
	}
	
}

class CompareByHash implements Comparator<Expression> {

	@Override
	public int compare(Expression o1, Expression o2) {
		long difference = o1.hash() - o2.hash();
		if (difference > 0) return 1;
		else if (difference < 0) return -1;
		else return 0;
	}
	
}
