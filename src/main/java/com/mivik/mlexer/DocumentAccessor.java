package com.mivik.mlexer;

public abstract class DocumentAccessor implements Cloneable {
	public abstract void moveCursor(int x);

	public abstract int getCursor();

	public abstract char get();

	public abstract boolean moveBack();

	public abstract boolean moveForward();

	public abstract void getChars(int st, int len, char[] dst, int off);

	public abstract int length();

	public abstract DocumentAccessor clone();

	public int moveBack(int x) {
		int ret = 0;
		for (int i = 0; i < x; i++) {
			if (moveBack()) return ret;
			++ret;
		}
		return ret;
	}

	public int moveForward(int x) {
		int ret = 0;
		for (int i = 0; i < x; i++) {
			if (moveForward()) return ret;
			++ret;
		}
		return ret;
	}

	public String substring(int st, int en) {
		char[] he = new char[en - st];
		getChars(st, he.length, he, 0);
		return new String(he);
	}

	public final char getAndMoveForward() {
		char ret = get();
		moveForward();
		return ret;
	}

	public boolean eof() {
		return getCursor() == length();
	}
}