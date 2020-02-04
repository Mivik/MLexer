package com.mivik.mlexer;

public abstract class Cursor implements Cloneable {
	@Override
	public abstract Cursor clone();

	public abstract void set(Cursor cursor);
}