package com.mivik.mlexer;

import java.util.Random;

public class TestClass {
	private static char[] S = ("{'json':'good','qwe':123}").toCharArray();
	private static MLexer lexer;

	public static void main(String[] args) {
		benchmark();
	}

	private static void benchmark() {
		final int count = 1000;
		long st = System.currentTimeMillis();
		Random random = new Random();
		lexer = new JSONLexer();
		lexer.setText(S);
		for (int i = 0; i < count; i++) {
			int ind = random.nextInt(S.length + 1);
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
		char[] cs = s.toCharArray();
		char[] ns = new char[cs.length + S.length];
		System.arraycopy(S, 0, ns, 0, i);
		System.arraycopy(cs, 0, ns, i, cs.length);
		System.arraycopy(S, i, ns, i + cs.length, S.length - i);
		S = ns;
		System.gc();
		lexer.onTextReferenceUpdate();
		lexer.onInsertChars(i, s.length());
	}

	private static void deleteString(int i, int len) {
		if (len > i) len = i;
		char[] ns = new char[S.length - len];
		System.arraycopy(S, 0, ns, 0, i - len);
		System.arraycopy(S, i, ns, i - len, S.length - i);
		S = ns;
		System.gc();
		lexer.onTextReferenceUpdate();
		lexer.onDeleteChars(i, len);
	}
}
