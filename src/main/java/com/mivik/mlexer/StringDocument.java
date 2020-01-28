package com.mivik.mlexer;

public class StringDocument extends Document<StringDocument.Cursor> {
	private char[] cs;
	private int off, len;

	public static char[] CharSequence2CharArray(CharSequence cs) {
		char[] ret = new char[cs.length()];
		for (int i = 0; i < cs.length(); i++) ret[i] = cs.charAt(i);
		return ret;
	}

	public StringDocument(CharSequence s) {
		this(CharSequence2CharArray(s), 0, s.length());
	}

	public StringDocument(char[] cs) {
		this(cs, 0, cs.length);
	}

	public StringDocument(char[] cs, int off, int len) {
		this.cs = cs;
		this.off = off;
		this.len = len;
	}

	public void setText(char[] cs) {
		this.cs = cs;
		this.off = 0;
		this.len = cs.length;
	}

	public void setText(char[] cs, int off, int len) {
		this.cs = cs;
		this.off = off;
		this.len = len;
	}

	@Override
	public Cursor getBeginCursor() {
		return new Cursor(0);
	}

	@Override
	public char charAt(Cursor x) {
		return cs[off + x.ind];
	}

	@Override
	public boolean moveBack(Cursor x) {
		if (x.ind == 0) return false;
		--x.ind;
		return true;
	}

	@Override
	public boolean moveForward(Cursor x) {
		if (x.ind == len) return false;
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
		return new StringBuilder(new String(cs, off + sel.begin.ind, sel.end.ind - sel.begin.ind));
	}

	@Override
	public int length() {
		return len;
	}

	@Override
	public Cursor Index2Cursor(int ind) {
		return new Cursor(ind);
	}

	@Override
	public int Cursor2Index(Cursor x) {
		return x.ind;
	}

	static class Cursor extends com.mivik.mlexer.Cursor implements Comparable<Cursor> {
		private int ind;

		public Cursor() {
			this.ind = 0;
		}

		public Cursor(int ind) {
			this.ind = ind;
		}

		@Override
		public Cursor clone() {
			return new Cursor(ind);
		}

		@Override
		public int compareTo(Cursor cursor) {
			return Integer.compare(ind, cursor.ind);
		}
	}
}