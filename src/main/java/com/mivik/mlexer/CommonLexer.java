package com.mivik.mlexer;

public abstract class CommonLexer extends MLexer {
	public CommonLexer() {
	}

	protected byte getNext() {
		if (S.eof()) return EOF;
		ST = S.getCursor();
		if (isWhitespace(S.get())) ReadSpaces();
		if (S.eof()) return EOF;
		if (isIdentifierStart(S.get())) {
			int st = S.getCursor();
			ReadIdentifier();
			return getWordType(st, S.getCursor());
		}
		byte type = specialJudge();
		if (type != FAILED) return type;
		char c = S.get();
		S.moveForward();
		return processSymbol(c);
	}

	protected void ReadIdentifier() {
		do {
			S.moveForward();
		} while ((!S.eof()) && isIdentifierPart(S.get()));
	}

	// Overrideable
	// Special judge for stuffs such as numbers, or pragma statement in C
	// Called when MLexer reads content that is neither space nor identifier
	// If this method returns TYPE_FAILED, MLexer will treat the content as symbols (processSymbol)
	// 可重写
	// 特殊判断一些类似于数字或者C语言里面的预编译指令
	// 当MLexer读到非空格非标志符时会调用此函数
	// 如果函数返回TYPE_FAILED，MLexer将会把当前内容作为符号自动处理（processSymbol）
	public byte specialJudge() {
		char c = S.get();
		if (Character.isDigit(c) || c == '.' || c == '-' || c == '+') {
			boolean hex = false;
			char cur = c;
			do {
				c = cur;
				S.moveForward();
				if (S.eof()) break;
				cur = S.get();
				if (cur == 'x' && c == '0' && S.getCursor() - 1 == ST) hex = true;
			} while (Character.isDigit(cur) || cur == '.' || (cur == 'e' && (!S.eof()) && c != '.') || (hex && Character.isLetter(cur)) || ((cur == '-' || cur == '+') && c == 'e'));
			if ((!S.eof()) && S.getCursor() == ST + 1) {
				int ori = S.getCursor();
				S.moveCursor(ST);
				char cc = S.get();
				S.moveCursor(ori);
				switch (cc) {
					case '.':
						return TYPE_PERIOD;
					case '+':
					case '-':
						return TYPE_OPERATOR;
				}
			}
			if (!S.eof())
				switch (S.get()) {
					case 'l':
					case 'L':
					case 'd':
					case 'D':
					case 'f':
					case 'F':
						S.moveForward();
				}
			return TYPE_NUMBER;
		}
		return FAILED;
	}

	public final boolean isKeyword(DocumentAccessor s, int st, int en) {
		return getKeywordTrie().hasWord(s, st, en);
	}

	public byte processSymbol(char c) {
		switch (c) {
			// 有 # 或者 ## 或者 #= 用法的运算符
			case '|':
			case '&':
			case '+':
			case '-': {
				if (S.eof()) return TYPE_OPERATOR;
				char cur = S.get();
				if (cur == c || cur == '=') {
					S.moveForward();
					return TYPE_OPERATOR;
				}
				return TYPE_OPERATOR;
			}
			// 有 # 或者 #= 用法的运算符
			case '^':
			case '%':
			case '*':
			case '!': {
				if (S.eof()) return TYPE_OPERATOR;
				if (S.get() == '=') S.moveForward();
				return TYPE_OPERATOR;
			}
			// 有 # 或者 #= 或者 ## 或者 ##= 用法的运算符
			case '>':
			case '<': {
				if (S.eof()) return TYPE_OPERATOR;
				if (S.get() == c) {
					S.moveForward();
					if (S.eof()) return TYPE_OPERATOR;
					if (S.get() == '=') {
						S.moveForward();
						return TYPE_OPERATOR;
					}
				} else if (S.get() == '=') S.moveForward();
				return TYPE_OPERATOR;
			}
			// 只有 # 用法的运算符
			case '~':
			case '?':
				return TYPE_OPERATOR;
			case '/': {
				if (S.eof()) return TYPE_OPERATOR;
				switch (S.get()) {
					case '*': {
						boolean star = false;
						do {
							S.moveForward();
							if (S.eof()) return TYPE_COMMENT;
							char cur = S.get();
							if (cur == '*') star = true;
							else {
								if (cur == '/' && star) {
									S.moveForward();
									return TYPE_COMMENT;
								}
								star = false;
							}
						} while (true);
					}
					case '/': {
						do {
							S.moveForward();
						} while ((!S.eof()) && S.get() != '\n');
						return TYPE_COMMENT;
					}
					case '=':
						S.moveForward();
						return TYPE_OPERATOR;
					default:
						return TYPE_OPERATOR;
				}
			}
			case '"': {
				readString('"');
				return TYPE_STRING;
			}
			case '\'': {
				// 尽管单引号里面只允许有一个字符，但考虑到转义（我懒得写判断了）和用户异常遍及，我还是把它当作里面可以放n个字符吧
				// 以及...最好只parse一行
				readString('\'');
				return TYPE_CHAR;
			}
			case '=': {
				if (S.eof()) return TYPE_ASSIGNMENT;
				if (S.get() == '=') {
					S.moveForward();
					return TYPE_OPERATOR;
				}
				return TYPE_ASSIGNMENT;
			}
			case '(':
			case ')':
				return TYPE_PARENTHESIS;
			case '[':
			case ']':
				return TYPE_SQUARE_BRACKET;
			case '{':
			case '}':
				return TYPE_BRACE;
			case ';':
				return TYPE_SEMICOLON;
			case ':':
				return TYPE_COLON;
			case ',':
				return TYPE_COMMA;
		}
		return TYPE_PURE;
		// 还是不要抛出错误吧——要是用户从外面粘贴过来乱码你就崩溃了是几个意思?
//		throw new RuntimeException();
	}

	protected abstract byte getWordType(int st, int en);

	public abstract Trie getKeywordTrie();

	protected abstract boolean isIdentifierStart(char c);

	protected abstract boolean isIdentifierPart(char c);
}
