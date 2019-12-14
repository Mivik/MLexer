package com.mivik.mlexer;

public class JavaScriptLexer extends JavaLexer {
	// 为什么这里要Lazy Build？参看我的父亲
	private static Trie KEYWORD_TRIE = null;

	@Override
	public Trie getKeywordTrie() {
		if (KEYWORD_TRIE == null)
			KEYWORD_TRIE = Trie.BuildTrie(
					"break", "case", "continue", "default", "delete", "do", "else", "for", "function", "if", "in", "let", "new", "return", "switch",
					"this", "typeof", "var", "void", "while", "with", "yield", "catch", "const", "debugger", "finally", "instanceof", "throw", "try"
			);
		return KEYWORD_TRIE;
	}

	public JavaScriptLexer() {
	}

	@Override
	public byte processSymbol(char c) {
		if (c == '\'') {
			// JS的世界里没有char
			boolean z = false;
			char cur;
			do {
				if (S.eof()) return TYPE_STRING;
				cur = S.get();
				if (cur == '\n') return TYPE_STRING;
				if (cur == '\\')
					z = !z;
				else if (cur == '\'' && !z) {
					S.moveRight();
					return TYPE_STRING;
				} else if (z) z = false;
				S.moveRight();
			} while (true);
		}
		if ((!S.eof()) && c == '=' && S.get() == '>') // =>
			return TYPE_OPERATOR;
		return super.processSymbol(c);
	}
}