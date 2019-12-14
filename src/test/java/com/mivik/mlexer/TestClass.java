package com.mivik.mlexer;

public class TestClass {
	private static char[] S = ("{'json':'good','qwe':123}").toCharArray();
	private static MLexer lexer;

	public static void main(String[] args) {
		lexer = new JSONLexer();
		lexer.setText(S);
		printState();
	}

	private static void printState() {
		int t;
		for (int i = 1; i <= lexer.DS[0]; i++) {
			if (i == lexer.DS[0]) t = lexer.length();
			else t = lexer.DS[i + 1];
			System.out.println(lexer.getTypeName(lexer.D[i]) + ":" + lexer.getTrimmedPartText(i));
		}
		System.out.println("============");
	}

	private static void insertString(int i, String s) {
		char[] cs = s.toCharArray();
		char[] ns = new char[cs.length + S.length];
		System.arraycopy(S, 0, ns, 0, i);
		System.arraycopy(cs, 0, ns, i, cs.length);
		System.arraycopy(S, i, ns, i + cs.length, S.length - i);
		S = ns;
		cs = null;
		ns = null;
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
		ns = null;
		System.gc();
		lexer.onTextReferenceUpdate();
		lexer.onDeleteChars(i, len);
	}
}
