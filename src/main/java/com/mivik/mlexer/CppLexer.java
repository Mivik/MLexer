package com.mivik.mlexer;

public class CppLexer extends CLexer {
	private static Trie KEYWORD_TRIE = null;

	@Override
	public Trie getKeywordTrie() {
		if (KEYWORD_TRIE == null)
			KEYWORD_TRIE = Trie.BuildTrie(
					"asm", "do", "if", "return", "typedef", "auto", "double", "inline", "short", "typeid", "bool", "int", "signed", "typename", "break", "else", "long", "sizeof", "union", "case", "enum", "mutable", "static", "unsigned", "catch", "explicit",
					"namespace", "using", "char", "export", "new", "struct", "virtual", "class", "extern", "operator", "switch", "void", "const", "false", "private", "template", "volatile", "float", "protected", "this", "continue",
					"for", "public", "throw", "while", "default", "friend", "register", "true", "delete", "goto", "try"
			);
		return KEYWORD_TRIE;
	}

	@Override
	protected byte getWordType(int st, int en) {
		if (equals(st, en, "true") || equals(st,en, "false")) return TYPE_BOOLEAN;
		return super.getWordType(st, en);
	}

	public CppLexer() {
	}
}
