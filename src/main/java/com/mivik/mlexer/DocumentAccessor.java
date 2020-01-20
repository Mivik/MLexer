package com.mivik.mlexer;

public abstract class DocumentAccessor implements Cloneable {
	public abstract void moveCursor(int x);

	public abstract int getCursor();

	public abstract char get();

	public abstract void moveLeft();

	public abstract void moveRight();

	public abstract void getChars(int st, int len, char[] dst, int off);

	public abstract int length();

	public abstract DocumentAccessor clone();

	public String substring(int st, int en) {
		char[] he = new char[en - st];
		getChars(st, he.length, he, 0);
		return new String(he);
	}

	public final char getAndMoveRight() {
		char ret = get();
		moveRight();
		return ret;
	}

	public boolean eof() {
		return getCursor() == length();
	}
}