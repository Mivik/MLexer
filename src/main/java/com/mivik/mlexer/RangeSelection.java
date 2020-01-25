package com.mivik.mlexer;

public class RangeSelection<T extends Cursor<T>> implements Comparable<RangeSelection<T>> {
	public T begin, end;

	public RangeSelection(T cursor) {
		this.begin = cursor.clone();
		this.end = cursor.clone();
	}

	public RangeSelection(Document<T> doc, int st, int en) {
		this.begin = doc.Index2Cursor(st);
		this.end = doc.Index2Cursor(en);
	}

	public RangeSelection(T begin, T end) {
		this.begin = begin.clone();
		this.end = end.clone();
	}

	@Override
	public int compareTo(RangeSelection<T> t) {
		int ret = begin.compareTo(t.begin);
		if (ret != 0) return ret;
		ret = end.compareTo(t.end);
		return ret;
	}
}