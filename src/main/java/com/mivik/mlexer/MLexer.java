package com.mivik.mlexer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class MLexer {
	public static final int EXPAND_SIZE = 128;
	public static final int TOTAL_COUNT = 24;
	public static final byte TYPE_IDENTIFIER = 0, TYPE_KEYWORD = 1, TYPE_NUMBER = 2, TYPE_COMMENT = 3, TYPE_STRING = 4, TYPE_CHAR = 5, TYPE_OPERATOR = 6, TYPE_BOOLEAN = 7, TYPE_ASSIGNMENT = 8,
			TYPE_NULL = 9, TYPE_PARENTHESIS = 10, TYPE_SQUARE_BRACKET = 11, TYPE_BRACE = 12, TYPE_SEMICOLON = 13, TYPE_COLON = 14, TYPE_PERIOD = 15, TYPE_COMMA = 16, TYPE_PURE = 17,
			TYPE_PREPROCESSOR_COMMAND = 18, TYPE_TAG_START = 19, TYPE_TAG_END = 20, TYPE_CONTENT_START = 21, TYPE_CONTENT = 22, TYPE_CDATA = 23,
			FAILED = -1, EOF = -2;

	private static String[] __TypeNames = null;

	protected Document DOC;
	protected CursorWrapper S;
	public int ST;
	public byte[] D = new byte[EXPAND_SIZE + 1];
	public int[] DS = new int[EXPAND_SIZE + 1];
	private boolean _Parsed = false;

	public MLexer() {
	}

	public final boolean isParsed() {
		return _Parsed;
	}

	public final void ensureParsed() {
		if (!_Parsed) parseAll();
	}

	public final void copyFrom(MLexer a) {
		this.S = a.S;
		this.ST = a.ST;
		this.D = a.D;
		this.DS = a.DS;
	}

	public final void readString(char type) {
		boolean z = false;
		do {
			if (S.eof()) return;
			char c = S.get();
			if (c == '\n') return;
			if (c == '\\')
				z = !z;
			else if (c == type && !z) {
				S.moveForward();
				return;
			} else if (z) z = false;
			S.moveForward();
		} while (true);
	}

	protected final void ReadSpaces() {
		while ((!S.eof()) && isWhitespace(S.get())) S.moveForward();
	}

	// 传入的是修改前光标的位置
	public final void onInsertChars(int pos, int len) {
		if (len == 0) return;
		if (S.length() - len == 0) {
			parseAll();
			return;
		}
		int part = findPart(Math.max(pos - 1, 0));
//		if (pos == DS[part]) part--;
		if (part <= 0) {
			for (int i = 1; i <= DS[0]; i++) DS[i] += len;
			return;
		}
		S.move(Math.min(DS[part], pos));
//		int en = DE[part] + len;
		int afterLen = Math.max(DS[0] - part, 0);
		byte[] afterD = new byte[afterLen];
		int[] afterDS = new int[afterLen];
		if (afterLen != 0) {
			System.arraycopy(D, part + 1, afterD, 0, afterLen);
			for (int i = 0; i < afterLen; i++)
				afterDS[i] = DS[part + i + 1] + len;
		}
		DS[0] = Math.max(part - 1, 0);
		byte type;
		int i = 0;
		while (true) {
			type = getNext();
			if (type == EOF) break;
			ensureCapacity(++DS[0] + 1);
			D[DS[0]] = type;
			DS[DS[0]] = ST;
			if (S.eof()) return;
			if (i != afterLen) if (ST == afterDS[i] && type == afterD[i]) break;
			if (i != afterLen && S.getIndex() >= afterDS[i]) {
				do {
					i++;
				} while (i != afterLen && S.getIndex() >= afterDS[i]);
				if (i != afterLen) i--;
			}
		}
		if (afterLen != 0) {
			int cpLen = afterLen - i;
			int nl = DS[0] + cpLen - 1;
			ensureCapacity(nl + 1);
			System.arraycopy(afterD, i, D, DS[0], cpLen);
			System.arraycopy(afterDS, i, DS, DS[0], cpLen);
			DS[0] = nl;
		}
	}

	// 传入的是删除前的坐标
	public final void onDeleteChars(int pos, int len) {
		if (len > pos) len = pos;
		int part2 = findPart(pos);
//		int en = DE[part2] - len;
		pos -= len;
		int part1 = findPart(Math.max(pos - 1, 0));
//		if (pos == DS[part1]) part1--;
		if (part2 <= 0) {
			for (int i = 1; i <= DS[0]; i++) DS[i] -= len;
			return;
		}
		S.move(Math.min(DS[part1], pos));
		int afterLen = Math.max(DS[0] - part2, 0);
		byte[] afterD = new byte[afterLen];
		int[] afterDS = new int[afterLen];
		if (afterLen != 0) {
			System.arraycopy(D, part2 + 1, afterD, 0, afterLen);
			for (int i = 0; i < afterLen; i++)
				afterDS[i] = DS[part2 + i + 1] - len;
		}
		DS[0] = part1 - 1;
		int i = 0;
		byte type;
		while (true) {
			type = getNext();
			if (type == EOF) break;
			ensureCapacity(++DS[0] + 1);
			D[DS[0]] = type;
			DS[DS[0]] = ST;
			if (S.eof()) return;
			if (i != afterLen) if (ST == afterDS[i] && type == afterD[i]) break;
			if (i != afterLen && S.getIndex() >= afterDS[i]) {
				do {
					i++;
				} while (i != afterLen && S.getIndex() >= afterDS[i]);
				if (i != afterLen) i--;
			}
		}
		if (afterLen != 0) {
			int cpLen = afterLen - i;
			int nl = DS[0] + cpLen - 1;
			ensureCapacity(nl + 1);
			System.arraycopy(afterD, i, D, DS[0], cpLen);
			System.arraycopy(afterDS, i, DS, DS[0], cpLen);
			DS[0] = nl;
		}
	}

	public final void setDocument(char[] cs) {
		setDocument(new StringDocument(cs));
	}

	public final <T extends Cursor> void setDocument(Document<T> s) {
		this.DOC = s;
		this.S = new CursorWrapper<>(s, s.getBeginCursor());
		onTextReferenceUpdate();
	}

	public final Document getDocument() {
		return this.DOC;
	}

	public final Cursor getDocumentAccessor() {
		return this.S;
	}

	public final void onTextReferenceUpdate() {
		_Parsed = false;
	}

	public final int findPart(int pos) {
		if (pos == 0) return 1;
		int l = 1, r = DS[0];
		int mid;
		while (l <= r) {
			mid = (l + r) >> 1;
			if (DS[mid] <= pos)
				l = mid + 1;
			else
				r = mid - 1;
		}
		return r;
	}

	public final char[] getChars() {
		return S.toString().toCharArray();
	}

	public final String getPartText(int st, int en) {
		return S.substring(st, en);
	}

	public final char[] getChars(int st, int en) {
		char[] ret = new char[en - st];
		S.getChars(st, en - st, ret, 0);
		return ret;
	}

	public final void clearCache() {
		S.move(0);
		this.DS[0] = 0;
	}

	private void ensureCapacity(int tar) {
		if (tar <= D.length) return;
		tar = newCapacity(D.length, tar);
		D = Arrays.copyOf(D, tar);
		DS = Arrays.copyOf(DS, tar);
	}

	public void parseAll() {
		if (_Parsed) return;
		if (S == null) return;
		S.move(0);
		this.DS[0] = 0;
		byte type;
		while ((type = getNext()) != EOF) {
			ensureCapacity(++DS[0] + 1);
			D[DS[0]] = type;
			DS[DS[0]] = ST;
		}
		ensureCapacity(DS[0] + 1);
		DS[DS[0] + 1] = S.length();
		_Parsed = true;
	}

	public String getTypeName(byte type) {
		if (__TypeNames == null) {
			__TypeNames = new String[Byte.MAX_VALUE - Byte.MIN_VALUE + 1];
			try {
				for (Field one : MLexer.class.getFields())
					if (one.getType() == byte.class && Modifier.isStatic(one.getModifiers()))
						__TypeNames[(int) one.getByte(null) - Byte.MIN_VALUE] = one.getName();
			} catch (Throwable t) {
				return null;
			}
		}
		return __TypeNames[(int) type - Byte.MIN_VALUE];
	}

	public int length() {
		return S.length();
	}

	protected final boolean equals(int st, int en, String s) {
		if (en - st != s.length()) return false;
		int ori = S.getIndex();
		S.move(st);
		for (int i = st; i < en; i++, S.moveForward())
			if (S.get() != s.charAt(i - st)) {
				S.move(ori);
				return false;
			}
		S.move(ori);
		return true;
	}

	public final String getLastString() {
		return S.substring(ST, S.getIndex());
	}

	public int getPartEnd(int part) {
		return part >= DS[0] ? S.length() : DS[part + 1];
	}

	public int getPartLength(int part) {
		return getPartEnd(part) - DS[part];
	}

	public String getPartText(int part) {
		return S.substring(DS[part], DS[part] + getPartLength(part));
	}

	public String getTrimmedPartText(int part) {
		return getPartText(part).trim();
	}

	public final boolean isStartOfLine() {
		int ori = S.getIndex();
		if (ori == 0) return true;
		S.moveBack();
		while (S.getIndex() >= 0) {
			if (S.get() == '\n') {
				S.move(ori);
				return true;
			}
			if (!isWhitespace(S.get())) {
				S.move(ori);
				return false;
			}
			S.moveBack();
		}
		S.move(ori);
		return true;
	}

	public final String getStateString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 1; i <= DS[0]; i++)
			builder.append(getTypeName(D[i])).append(':').append(DS[i]).append('\n');
		if (builder.length() != 0) builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

	public final String[] queryKeywords(char[] cs, int st, int en) {
		if (!(this instanceof CommonLexer)) return new String[0];
		return ((CommonLexer) this).getKeywordTrie().queryWords(cs, st, en);
	}

	public static int newCapacity(int cur, int minCapacity) {
		final int newCapacity = cur + (cur >> 1);
		if (newCapacity - minCapacity <= 0) {
			if (minCapacity < 0) throw new OutOfMemoryError();
			else return minCapacity;
		} else return newCapacity - 2147483639 <= 0 ? newCapacity : hugeCapacity(minCapacity);
	}

	private static int hugeCapacity(int minCapacity) {
		if (minCapacity < 0) throw new OutOfMemoryError();
		else return minCapacity > 2147483639 ? 2147483647 : 2147483639;
	}

	protected abstract byte getNext();

	protected abstract boolean isWhitespace(char c);

	public static class Trie {
		private final short[][] C;
		private final boolean[] L;

		public Trie(int len) {
			C = new short[len][26];
			L = new boolean[len];
		}

		public static Trie BuildTrie(String... ks) {
			int len = 1;
			int i;
			for (i = 0; i < ks.length; i++) len += ks[i].length();
			Trie t = new Trie(len);
			short tot = 0;
			int cur;
			byte c;
			for (String one : ks) {
				for (cur = i = 0; i < one.length(); i++) {
					c = (byte) (one.charAt(i) - 'a');
					if (t.C[cur][c] == 0)
						t.C[cur][c] = ++tot;
					cur = t.C[cur][c];
				}
				t.L[cur] = true;
			}
			return t;
		}

		public <T extends Cursor> boolean hasWord(CursorWrapper<T> ori, int st, int en) {
			int c;
			int cur = 0;
			CursorWrapper<T> s = ori.clone();
			s.move(st);
			for (int i = st; i < en; i++, s.moveForward()) {
				char w = s.get();
				if (w < 'a' || w > 'z') return false;
				c = w - 'a';
				if (c < 0 || c >= 26) return false;
				if ((cur = C[cur][c]) == 0) return false;
			}
			return L[cur];
		}

		public String[] queryWords(char[] cs, int st, int en) {
			final String[] EMPTY = new String[0];

			byte c;
			int cur = 0;
			for (int i = st; i < en; i++) {
				if (cs[i] < 'a' || cs[i] > 'z') return EMPTY;
				c = (byte) (cs[i] - 'a');
				if (c < 0 || c >= 26) return EMPTY;
				if ((cur = C[cur][c]) == 0) return EMPTY;
			}
			StringBuilder full = new StringBuilder();
			full.append(cs, st, en - st);
			ArrayList<String> ret = new ArrayList<>();
			listWords(full, cur, ret);
			return ret.toArray(EMPTY);
		}

		private void listWords(StringBuilder full, int node, ArrayList<String> str) {
			if (L[node]) str.add(full.toString());
			for (char c = 0; c < 26; c++) {
				if (C[node][c] == 0) continue;
				full.append((char) (c + 'a'));
				listWords(full, C[node][c], str);
				full.deleteCharAt(full.length() - 1);
			}
		}
	}
}