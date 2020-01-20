package com.mivik.mlexer;

public class SimpleStringProvider implements Document {
	private char[] cs;
	private int off, len;

	public static char[] CharSequence2CharArray(CharSequence cs) {
		char[] ret = new char[cs.length()];
		for (int i = 0; i < cs.length(); i++) ret[i] = cs.charAt(i);
		return ret;
	}

	public SimpleStringProvider(CharSequence s) {
		this(CharSequence2CharArray(s), 0, s.length());
	}

	public SimpleStringProvider(char[] cs) {
		this(cs, 0, cs.length);
	}

	public SimpleStringProvider(char[] cs, int off, int len) {
		this.cs = cs;
		this.off = off;
		this.len = len;
	}

	public void setText(char[] cs) {
		setText(cs, 0, cs.length);
	}

	public void setText(char[] cs, int off, int len) {
		this.cs = cs;
		this.off = off;
		this.len = len;
	}

	@Override
	public DocumentAccessor getAccessor() {
		return new Accessor();
	}

	public class Accessor extends DocumentAccessor {
		private int ind;

		private Accessor() {
			this.ind = off;
		}

		private Accessor(Accessor origin) {
			this.ind = origin.ind;
		}

		@Override
		public void moveCursor(int x) {
			this.ind = off + x;
		}

		@Override
		public char get() {
			return cs[ind];
		}

		@Override
		public void moveLeft() {
			--ind;
		}

		@Override
		public void moveRight() {
			++ind;
		}

		@Override
		public int getCursor() {
			return ind - off;
		}

		@Override
		public String substring(int st, int en) {
			return new String(cs, off + st, en - st);
		}

		@Override
		public void getChars(int st, int len, char[] dst, int off) {
			System.arraycopy(cs, st, dst, off, len);
		}

		@Override
		public int length() {
			return len;
		}

		@Override
		public DocumentAccessor clone() {
			return new Accessor(this);
		}
	}

	@Override
	public String toString() {
		return new String(cs, off, len);
	}
}