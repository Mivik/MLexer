package com.mivik.mlexer;

public class NullLexer extends MLexer {
	@Override
	protected byte getNext() {
		if (S.eof()) return EOF;
		S.moveCursor(S.length());
		return TYPE_PURE;
	}

	@Override
	protected boolean isWhitespace(char c) {
		return false;
	}

	public NullLexer() {
	}
}
