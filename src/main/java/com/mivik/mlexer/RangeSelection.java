package com.mivik.mlexer;

public class RangeSelection<T extends Cursor> {
	public T begin, end;

	public RangeSelection(T cursor) {
		this.begin = (T) cursor.clone();
		this.end = (T) cursor.clone();
	}

	public RangeSelection(Document<T> doc, int st, int en) {
		this.begin = doc.Index2Cursor(st);
		this.end = doc.Index2Cursor(en);
	}

	public RangeSelection(T begin, T end) {
		this.begin = (T) begin.clone();
		this.end = (T) end.clone();
	}
}