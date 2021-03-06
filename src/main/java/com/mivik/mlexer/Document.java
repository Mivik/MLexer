package com.mivik.mlexer;

public abstract class Document<T extends Cursor> {
	public abstract T getBeginCursor();

	public abstract T getEndCursor();

	public abstract char charAt(T x);

	public final char charAt(int ind) {
		return charAt(Index2Cursor(ind));
	}

	public abstract boolean moveBack(T x);

	public abstract boolean moveForward(T x);

	public abstract int moveBack(T x, int dis);

	public abstract int moveForward(T x, int dis);

	public abstract StringBuilder subStringBuilder(RangeSelection<T> sel);

	public abstract int length();

	public abstract T Index2Cursor(int ind);

	public abstract int Cursor2Index(T x);

	public final char[] subChars(RangeSelection<T> sel) {
		return subChars(sel, null, 0);
	}

	public final char[] subChars(RangeSelection<T> sel, char[] dst) {
		return subChars(sel, dst, 0);
	}

	public char[] subChars(RangeSelection<T> sel, char[] dst, int off) {
		StringBuilder ret = subStringBuilder(sel);
		if (dst == null) dst = new char[ret.length()];
		ret.getChars(0, ret.length(), dst, off);
		return dst;
	}

	public final String substring(RangeSelection<T> sel) {
		return new String(subChars(sel));
	}

	public final RangeSelection<T> fromBegin(T st, int len) {
		RangeSelection<T> sel = new RangeSelection<>(st, st);
		moveForward(sel.end, len);
		return sel;
	}

	public final RangeSelection<T> fromEnd(T en, int len) {
		RangeSelection<T> sel = new RangeSelection<>(en, en);
		moveBack(sel.begin, len);
		return sel;
	}

	public boolean eof(T x) {
		return Cursor2Index(x) == length();
	}

	public final RangeSelection<T> getFullRangeSelection() {
		return new RangeSelection<>(getBeginCursor(), getEndCursor());
	}

	@Override
	public String toString() {
		return substring(getFullRangeSelection());
	}

	public char[] toCharArray() {
		return subChars(getFullRangeSelection());
	}
}