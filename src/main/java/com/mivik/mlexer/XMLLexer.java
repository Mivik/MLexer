package com.mivik.mlexer;

public class XMLLexer extends CommonLexer {
	@Override
	protected byte getNext() {
		if (S.eof()) return EOF;
		ST = S.getIndex();
		ReadSpaces();
		if (S.eof()) return EOF;
		int q = S.getIndex();
		d:
		if (D[DS[0]] == TYPE_CONTENT_START) {
			/*while (true) {
				char c = S.getAndMoveForward();
				if (S.eof()) return TYPE_CONTENT;
				if (c == '<') {
					c = S.get();
					if (c == '/') {
						S.moveBack();
						break;
					}
				}
			}*/
			while ((!S.eof()) && S.get() != '<') S.moveForward();
			if (S.eof()) return TYPE_CONTENT;
			if (S.getIndex() == q) break d;
			return TYPE_CONTENT;
		}

		if (isIdentifierStart(S.get())) {
			ReadIdentifier();
			return TYPE_IDENTIFIER;
		}
		char c = S.getAndMoveForward();
		char cur = S.eof() ? 0 : S.get();
		switch (c) {
			case '/':
				if (S.eof()) return TYPE_OPERATOR;
				if (cur == '>') {
					S.moveForward();
					return TYPE_TAG_END;
				}
				return TYPE_OPERATOR;
			case '>':
				return TYPE_CONTENT_START;
			case '?':
				if (S.eof()) return TYPE_TAG_END;
				if (cur == '>') S.moveForward();
				return TYPE_TAG_END;
			case '<': {
				if (S.eof()) return TYPE_TAG_START;
				if (isIdentifierStart(cur)) ReadIdentifier();
				else {
					switch (S.getAndMoveForward()) {
						case '?':
							if (S.eof()) return TYPE_TAG_START;
							if (isIdentifierStart(S.get())) ReadIdentifier();
							return TYPE_TAG_START;
						case '!':
							if (S.eof()) return TYPE_TAG_START;
							if (S.getAndMoveForward() == '-') {
								if (S.eof()) return TYPE_TAG_START;
								if (S.getAndMoveForward() == '-') {
									if (S.eof()) return TYPE_TAG_START;
									while (!S.eof()) {
										if (S.getAndMoveForward() == '-') {
											if (S.eof()) break;
											if (S.getAndMoveForward() == '-') {
												if (S.eof()) break;
												if (S.getAndMoveForward() == '>') break;
												else {
													S.moveBack();
													S.moveBack();
												}
											} else S.moveBack();
										}
									}
									return TYPE_COMMENT;
								}
							}
							while ((!S.eof()) && S.get() != '>') S.moveForward();
							if (!S.eof()) S.moveForward();
							return TYPE_CDATA;
						case '/':
							if (S.eof()) return TYPE_TAG_END;
							do S.moveForward();
							while ((!S.eof()) && S.get() != '>');
							if (!S.eof()) S.moveForward();
							return TYPE_TAG_END;
					}
				}
				return TYPE_TAG_START;
			}
			/*case '=':
			case '"':
				return processSymbol(c);*/
			default:
				return processSymbol(c);
		}
//		return TYPE_PURE;
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
