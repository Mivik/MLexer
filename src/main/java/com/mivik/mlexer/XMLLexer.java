package com.mivik.mlexer;

public class XMLLexer extends CommonLexer {
	@Override
	protected byte getNext() {
		if (P == L) return EOF;
		ST = P;
		if (S[P] == ' ' || S[P] == '\t') ReadSpaces();
		if (P == L) return EOF;
		int q = P;
		d:
		if (D[DS[0]] == TYPE_CONTENT_START) {
			while (P != L && S[P] != '<') P++;
			if (P == L) return TYPE_CONTENT;
			if (P == q) break d;
			return TYPE_CONTENT;
		}

		if (isIdentifierStart(S[P])) {
			ReadIdentifier();
			return TYPE_IDENTIFIER;
		}
		switch (S[P++]) {
			case '/':
				if (P == L) return TYPE_OPERATOR;
				if (S[P] == '>') {
					++P;
					return TYPE_TAG_END;
				}
				return TYPE_OPERATOR;
			case '>':
				return TYPE_CONTENT_START;
			case '?':
				if (P == L) return TYPE_TAG_END;
				if (S[P] == '>') P++;
				return TYPE_TAG_END;
			case '<': {
				if (P == L) return TYPE_TAG_START;
				if (isIdentifierStart(S[P])) ReadIdentifier();
				else switch (S[P++]) {
					case '?':
						if (P == L) return TYPE_TAG_START;
						if (isIdentifierStart(S[P])) ReadIdentifier();
						return TYPE_TAG_START;
					case '!':
						if (P == L) return TYPE_TAG_START;
						if (S[P++] == '-') {
							if (P == L) return TYPE_TAG_START;
							if (S[P++] == '-') {
								if (P == L) return TYPE_TAG_START;
								while (P + 2 < L && (!(S[P] == '-' && S[P + 1] == '-' && S[P + 2] == '>'))) P++;
								P = Math.min(P + 3, L);
								return TYPE_COMMENT;
							}
						}
						while (P != L && S[P] != '>') P++;
						if (P != L) P++;
						return TYPE_CDATA;
					case '/':
						if (P == L) return TYPE_TAG_END;
						do P++;
						while (P != L && S[P] != '>');
						if (P != L) P++;
						return TYPE_TAG_END;
				}
				return TYPE_TAG_START;
			}
			case '=':
			case '"':
				return processSymbol(S[--P]);
		}
		return TYPE_PURE;
	}

	@Override
	protected byte getWordType(int st, int en) {
		return 0;
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
