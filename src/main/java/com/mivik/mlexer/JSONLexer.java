package com.mivik.mlexer;

public class JSONLexer extends JavaScriptLexer {
	@Override
	protected byte getWordType(int st, int en) {
		if (equals(st, en, "true") || equals(st, P, "false")) return TYPE_BOOLEAN;
		return TYPE_IDENTIFIER;
	}

	@Override
	public Trie getKeywordTrie() {
		return null;
	}

	@Override
	protected boolean isIdentifierStart(char c) {
		return Character.isJavaIdentifierStart(c);
	}

	@Override
	protected boolean isIdentifierPart(char c) {
		return Character.isJavaIdentifierPart(c);
	}

	@Override
	protected boolean isWhitespace(char c) {
		return (c == ' ' || c == '\n' || c == '\t' || c == '\r' || c == '\f' || c == '\uFFFF');
	}
}