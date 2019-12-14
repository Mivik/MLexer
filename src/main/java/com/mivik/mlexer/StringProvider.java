package com.mivik.mlexer;

public abstract class StringProvider {
	public abstract void moveCursor(int x);

	public abstract int getCursor();

	public abstract char get();

	public abstract void moveLeft();

	public abstract void moveRight();

	public abstract String substring(int st, int en);

	public abstract int length();

	public final char getAndMoveRight() {
		char ret=get();
		moveRight();
		return ret;
	}

	public boolean eof() {
		return getCursor() == length();
	}
}