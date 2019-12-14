package com.mivik.mlexer;

public class SimpleStringProvider extends StringProvider {
	private char[] cs;
	private int off, len;
	private int ind;

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

	public SimpleStringProvider(char[] cs, int off) {
		this(cs, off, cs.length - off);
	}

	public SimpleStringProvider(char[] cs, int off, int len) {
		this.cs = cs;
		this.ind = this.off = off;
		this.len = len;
	}

	@Override
	public void moveCursor(int x) {
		this.ind = this.off + x;
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
	public int length() {
		return len;
	}
}