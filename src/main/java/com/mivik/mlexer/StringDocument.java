package com.mivik.mlexer;

public class StringDocument implements Document {
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
		public boolean moveForward() {
			if (ind == len - 1) return false;
			++ind;
			return true;
		}

		@Override
		public boolean moveBack() {
			if (ind == 0) return false;
			--ind;
			return true;
		}

		@Override
		public int moveForward(int x) {
			if (ind + x >= len) {
				ind = len - 1;
				return len - ind - 1;
			}
			ind += x;
			return x;
		}

		@Override
		public int moveBack(int x) {
			if (x > ind) {
				x = ind;
				ind = 0;
				return x;
			}
			ind -= x;
			return x;
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