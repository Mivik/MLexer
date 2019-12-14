package com.mivik.mlexer;

public class CLexer extends CommonLexer {
	private static Trie KEYWORD_TRIE = null;

	@Override
	public Trie getKeywordTrie() {
		if (KEYWORD_TRIE == null)
			KEYWORD_TRIE = Trie.BuildTrie(
					"auto", "break", "case", "char", "const", "continue", "default", "do", "double", "else", "enum", "extern", "float", "for", "goto", "if", "int", "long", "restrict",
					"register", "return", "short", "signed", "sizeof", "static", "struct", "switch", "typedef", "union", "unsigned", "void", "volatile", "while", "inline"
			);
		return KEYWORD_TRIE;
	}

	public CLexer() {
	}

	@Override
	protected byte getWordType(int st, int en) {
		if (isKeyword(S, st, en)) return TYPE_KEYWORD;
		// No bool in C!!!!
		return TYPE_IDENTIFIER;
	}

	@Override
	protected boolean isWhitespace(char c) {
		return (c == ' ' || c == '\n' || c == '\t' || c == '\r' || c == '\f' || c == '\uFFFF');
	}

	@Override
	protected boolean isIdentifierStart(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_';
	}

	@Override
	protected boolean isIdentifierPart(char c) {
		return isIdentifierStart(c);
	}

	@Override
	public byte specialJudge() {
		char c=S.get();
		if (c == '#' && isStartOfLine()) {
			do {
				S.moveRight();
			} while ((!S.eof()) && S.get() != '\n');
			return TYPE_PREPROCESSOR_COMMAND;
		}
		return super.specialJudge();
	}
}