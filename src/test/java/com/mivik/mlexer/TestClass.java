package com.mivik.mlexer;

import java.util.Random;

public class TestClass {
	private static StringBuilder S = new StringBuilder("<!DOCTYPE html>\n" +
			"<html>\n" +
			"<head>\n" +
			"</body>\n" +
			"</html>\n");
	private static StringBuilderDocument doc = new StringBuilderDocument(S);
	private static MLexer lexer;

	public static void main(String[] args) {
		lexer = new XMLLexer();
		lexer.setDocument(doc);
		lexer.parseAll();
		printState();
	}

	private static void benchmark() {
		final int count = 10000;
		long st = System.currentTimeMillis();
		Random random = new Random();
		lexer = new JavaLexer();
		lexer.setDocument(doc);
		lexer.parseAll();
		for (int i = 0; i < count; i++) {
			int ind = random.nextInt(S.length() + 1);
			if (random.nextInt(8) == 0) insertString(ind, "\n");
			else insertString(ind, "" + ((char) (random.nextInt(95) + 32)));
		}
		st = System.currentTimeMillis() - st;
		System.out.println("插入" + count + "次耗时: " + st + "ms");
		System.out.println("平均单次插入耗时: " + ((double) st / count) + "ms");
	}

	private static void printState() {
		for (int i = 1; i <= lexer.DS[0]; i++)
			System.out.println(lexer.getTypeName(lexer.D[i]) + ":" + lexer.getTrimmedPartText(i));
		System.out.println("============");
	}

	private static void insertString(int i, String s) {
		S.insert(i, s);
		lexer.onTextReferenceUpdate();
		lexer.onInsertChars(i, s.length());
	}

	private static void deleteString(int i, int len) {
		S.delete(i - len, i);
		lexer.onTextReferenceUpdate();
		lexer.onDeleteChars(i, len);
	}
}