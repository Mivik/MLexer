package com.mivik.mlexer;

public abstract class Cursor<T extends Cursor<T>> implements Cloneable, Comparable<Cursor<T>> {
	@Override
	public abstract T clone();
}