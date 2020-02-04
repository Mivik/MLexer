package com.mivik.mlexer;

public class StringBuilderDocument extends Document<StringBuilderDocument.Cursor> {
	private StringBuilder S;

	public StringBuilderDocument(StringBuilder builder) {
		this.S = builder;
	}

	@Override
	public Cursor getBeginCursor() {
		return new Cursor(0);
	}

	@Override
	public Cursor getEndCursor() {
		return new Cursor(S.length());
	}

	@Override
	public char charAt(Cursor x) {
		return S.charAt(x.ind);
	}

	@Override
	public boolean moveBack(Cursor x) {
		if (x.ind == 0) return false;
		--x.ind;
		return true;
	}

	@Override
	public boolean moveForward(Cursor x) {
		if (x.ind == S.length()) return false;
		++x.ind;
		return true;
	}

	@Override
	public int moveBack(Cursor x, int dis) {
		if (x.ind >= dis) {
			x.ind -= dis;
			return dis;
		}
		dis = x.ind;
		x.ind = 0;
		return dis;
	}

	@Override
	public int moveForward(Cursor x, int dis) {
		final int len = S.length();
		if (x.ind + dis <= len) {
			x.ind += dis;
			return dis;
		}
		dis = len - x.ind;
		x.ind = len;
		return dis;
	}

	@Override
	public StringBuilder subStringBuilder(RangeSelection<Cursor> sel) {
		return new StringBuilder(S.substring(sel.begin.ind, sel.end.ind));
	}

	@Override
	public int length() {
		return S.length();
	}

	@Override
	public Cursor Index2Cursor(int ind) {
		return new Cursor(ind);
	}

	@Override
	public int Cursor2Index(Cursor x) {
		return x.ind;
	}

	static class Cursor extends com.mivik.mlexer.Cursor {
		private int ind;

		public Cursor(int ind) {
			this.ind = ind;
		}

		@Override
		public void set(com.mivik.mlexer.Cursor cursor) {
			if (!(cursor instanceof Cursor)) return;
			Cursor t = (Cursor) cursor;
			this.ind = t.ind;
		}

		@Override
		public Cursor clone() {
			return new Cursor(ind);
		}
	}
}