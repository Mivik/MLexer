package com.mivik.mlexer;

public class NullLexer extends BaseLexer {
	@Override
	protected byte getNext() {
		if (P == L) return EOF;
		P = L;
		return TYPE_PURE;
	}

	@Override
	protected boolean isWhitespace(char c) {
		return false;
	}

	public NullLexer() {
	}
}
