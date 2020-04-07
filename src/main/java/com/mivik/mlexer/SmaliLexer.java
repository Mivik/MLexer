package com.mivik.mlexer;

public class SmaliLexer extends CommonLexer {

	@Override
	protected byte getWordType(int st, int en) {
		final CursorWrapper<?> cursor = S.clone();
		cursor.move(st);
		// dirty but works
		if (cursor.get() == 'v' || cursor.get() == 'p') return (en - st) > 4 ? TYPE_KEYWORD : TYPE_IDENTIFIER;
		else return TYPE_KEYWORD;
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
		return c=='-'||Character.isJavaIdentifierPart(c);
	}

	@Override
	public byte processSymbol(char c) {
		switch (c) {
			case '.': {
				if (S.eof()) return TYPE_OPERATOR;
				if (S.get()=='.') S.moveForward();
				return TYPE_OPERATOR;
			}
			case '-': {
				if (S.eof()) return TYPE_OPERATOR;
				if (S.get()=='>') S.moveForward();
				return TYPE_OPERATOR;
			}
			case '=':
				return TYPE_ASSIGNMENT;
			case ':':
				return TYPE_COLON;
			case ',':
				return TYPE_COMMA;
			case '{':
			case '}':
				return TYPE_BRACE;
			case '(':
			case ')':
				return TYPE_PARENTHESIS;
			case '@':
				return TYPE_OPERATOR;
		}
		return TYPE_PURE;
	}

	@Override
	protected boolean isWhitespace(char c) {
		return (c == ' ' || c == '\n' || c == '\t' || c == '\r' || c == '\f' || c == '\uFFFF');
	}
}