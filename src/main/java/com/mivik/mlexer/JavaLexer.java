package com.mivik.mlexer;

public class JavaLexer extends CommonLexer {
	// 这里应该使用Lazy Build，因为可能有子类继承，那时我们应该让子类来Build这个tree（只需要重写getKeywordTrie）
	private static Trie KEYWORD_TRIE = null;

	@Override
	public Trie getKeywordTrie() {
		if (KEYWORD_TRIE == null)
			KEYWORD_TRIE = Trie.BuildTrie(
					"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do",
					"double", "else", "enum", "extends", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof",
					"int", "interface", "long", "native", "new", "package", "private", "protected", "public", "return", "strictfp", "short",
					"static", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while"
			);
		return KEYWORD_TRIE;
	}

	@Override
	protected boolean isIdentifierStart(char c) {
		return Character.isJavaIdentifierStart(c);
	}

	@Override
	protected boolean isIdentifierPart(char c) {
		return Character.isJavaIdentifierPart(c);
	}

	public JavaLexer() {
	}

	@Override
	protected byte getWordType(int st, int en) {
		if (isKeyword(S, st, en)) return TYPE_KEYWORD;
		else if (equals(st, en, "true") || equals(st, en, "false")) return TYPE_BOOLEAN;
		else if (equals(st, en, "null")) return TYPE_NULL;
		return TYPE_IDENTIFIER;
	}

	@Override
	protected boolean isWhitespace(char c) {
		return (c == ' ' || c == '\n' || c == '\t' || c == '\r' || c == '\f' || c == '\uFFFF');
	}

	@Override
	public byte processSymbol(char c) {
		// 我大Java的 >>> 和 >>>=
		if (c == '>') {
			if (S.eof()) return TYPE_OPERATOR;
			char cur = S.get();
			if (cur == c) {
				S.moveRight();
				if (S.eof()) return TYPE_OPERATOR; // >>
				c = cur;
				cur = S.get();
				if (cur == '=') S.moveRight(); // >>=
				else if (cur == '>') {
					S.moveRight();
					if (S.eof()) return TYPE_OPERATOR; // >>>
					if (S.get() == '=') { // >>>=
						S.moveRight();
						return TYPE_OPERATOR;
					}
				}
			} else if (cur == '=') S.moveRight(); // >=
			return TYPE_OPERATOR;
		}
		return super.processSymbol(c);
	}
}