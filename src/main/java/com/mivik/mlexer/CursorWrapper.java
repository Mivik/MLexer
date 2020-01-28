package com.mivik.mlexer;

public class CursorWrapper<T extends Cursor> extends Cursor {
	private Document<T> doc;
	private T cursor;
	private int ind;

	public CursorWrapper(Document<T> doc, T cursor) {
		this.doc = doc;
		this.cursor = cursor;
		this.ind = doc.Cursor2Index(cursor);
	}

	public boolean moveBack() {
		boolean ret = doc.moveBack(cursor);
		if (ret) --ind;
		return ret;
	}

	public boolean moveForward() {
		boolean ret = doc.moveForward(cursor);
		if (ret) ++ind;
		return ret;
	}

	public int moveBack(int dis) {
		int ret = doc.moveBack(cursor, dis);
		ind -= ret;
		return ret;
	}

	public int moveForward(int dis) {
		int ret = doc.moveForward(cursor, dis);
		ind += ret;
		return ret;
	}

	public char getAndMoveForward() {
		return doc.getAndMoveForward(cursor);
	}

	public char get() {
		return doc.charAt(cursor);
	}

	public int getCursor() {
		return ind;
	}

	public boolean eof() {
		return ind == doc.length();
	}

	public int length() {
		return doc.length();
	}

	public void moveCursor(int ind) {
		cursor = doc.Index2Cursor(ind);
		this.ind = doc.Cursor2Index(cursor);
	}

	public void getChars(int st, int en, char[] dst, int off) {
		doc.subChars(new RangeSelection<>(doc, st, en), dst, off);
	}

	public String substring(int st, int en) {
		return doc.substring(new RangeSelection<>(doc, st, en));
	}

	@Override
	public CursorWrapper<T> clone() {
		return new CursorWrapper<>(doc, cursor);
	}
}