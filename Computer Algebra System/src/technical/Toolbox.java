package technical;

import java.util.ArrayList;
import java.util.List;

import expressions.Expression;

public class Toolbox {
	
	public static void showLineOfWork(String description, Expression input, Expression output) {
		if (output.toString().equals(input.toString())) return;
		else {
			System.out.println(description + " from " + input);
			System.out.println("Result: " + output);
			System.out.println();
		}
	}
	
	public static <T> List<T> arrayToList(T[] array) {
		List<T> list = new ArrayList<T>();
		for (T t : array) {
			list.add(t);
		}
		return list;
	}
	
	public static long hashLong(long x) {
		x ^= x >> 33;
		x *= 0xff51afd7ed558ccdL;
		x ^= x >> 33;
		x *= 0xc4ceb9fe1a85ec53L;
		x ^= x >> 33;
		x *= 0x8ff7c412ed4ca9b0L;
		x ^= x >> 33;
		return x;
	}
	
	public static long hashDouble(double x) {
		if (x == 0) return 987343453464562934L;
		Double d = (Double) x;
		int hashdouble = d.hashCode();
		long hash = ((Integer) hashdouble).hashCode();
		hash = hashLong(hash);
		return hash;
	}
	
	public static long hashAddition(double x1, double x2) {
		long hash1 = hashLong(hashDouble(x1)), hash2 = hashLong(hashDouble(x2));
		hash1 ^= hash2 >> 33;
		hash2 *= 0xff51afd7ed558ccdL;
		hash2 ^= hash1 >> 33;
		hash1 *= 0xc4ceb9fe1a85ec53L;
		hash2 ^= hash1 >> 33;
		hash1 *= 0x8ff7c412ed4ca9b0L;
		hash1 ^= hash2 >> 33;
		return hash1 * hash2;
	}
	
	public static long hashMultiplication(double x1, double x2) {
		long hash1 = hashLong(hashDouble(x1)), hash2 = hashLong(hashDouble(x2));
		hash1 ^= hash2 >> 33;
		hash2 *= 0x8e9c94bab719a29dL;
		hash2 ^= hash1 >> 33;
		hash1 *= 0x9c5edc2a9b1c76ffL;
		hash2 ^= hash1 >> 33;
		hash1 *= 0x1e5ad9bc734a266eL;
		hash1 ^= hash2 >> 33;
		return hash1 * hash2;
	}
	
	public static long hashExponent(double x1, double x2) {
		long hash1 = hashLong(hashDouble(x1)), hash2 = hashLong(hashDouble(x2));
		hash1 ^= hash2 >> 33;
		hash2 *= 0x6A2F35D4B9A8C7E1L;
		hash2 ^= hash1 >> 33;
		hash1 *= 0xF1E5A98D73C6B254L;
		hash2 ^= hash1 >> 33;
		hash1 *= 0x8BC2D1F74E0A6938L;
		hash1 ^= hash2 >> 33;
		return hash1 * hash2;
	}
	
	public static List<Expression> copyList(List<Expression> list) {
		List<Expression> copy = new ArrayList<Expression>();
		for (Expression t : list) copy.add(t.copy());
		return copy;
	}
	
	public static <T> List<T> shallowCopyList(List<T> list) {
		List<T> copy = new ArrayList<T>();
		for (T t : list) copy.add(t);
		return copy;
	}
	
}
