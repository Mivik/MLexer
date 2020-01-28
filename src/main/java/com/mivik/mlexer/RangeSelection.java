package com.mivik.mlexer;

public class RangeSelection<T extends Cursor> implements Cloneable {
	public T begin, end;

	public RangeSelection(T cursor) {
		this.begin = (T) cursor.clone();
		this.end = (T) cursor.clone();
	}

	public RangeSelection(T begin, T end) {
		this.begin = (T) begin.clone();
		this.end = (T) end.clone();
	}

	public RangeSelection(Document<T> doc, int cursor) {
		this(doc.Index2Cursor(cursor));
	}

	public RangeSelection(Document<T> doc, int st, int en) {
		this(doc.Index2Cursor(st), doc.Index2Cursor(en));
	}

	public RangeSelection(RangeSelection<T> ori) {
		this.begin = (T) ori.begin.clone();
		this.end = (T) ori.end.clone();
	}

	@Override
	public RangeSelection<T> clone() {
		return new RangeSelection<>(this);
	}
}