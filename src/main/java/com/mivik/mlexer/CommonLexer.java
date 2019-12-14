package com.mivik.mlexer;

public abstract class CommonLexer extends MLexer {
	public CommonLexer() {
	}

	protected byte getNext() {
		if (P == L) return EOF;
		ST = P;
		if (isWhitespace(S[P])) ReadSpaces();
		if (P == L) return EOF;
		if (isIdentifierStart(S[P])) {
			int st = P;
			ReadIdentifier();
			return getWordType(st, P);
		}
		byte type = specialJudge();
		if (type != FAILED) return type;
		return processSymbol(S[P++]);
	}

	protected void ReadIdentifier() {
		do {
			++P;
		} while (P != L && isIdentifierPart(S[P]));
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
		if (Character.isDigit(S[P]) || S[P] == '.' || S[P] == '-' || S[P] == '+') {
			boolean hex = false;
			do {
				if (++P == L) break;
				if (S[P] == 'x' && S[P - 1] == '0' && P - 1 == ST) hex = true;
			} while (Character.isDigit(S[P]) || S[P] == '.' || (S[P] == 'e' && P != ST && S[P - 1] != '.') || (hex && Character.isLetter(S[P])) || ((S[P] == '-' || S[P] == '+') && S[P - 1] == 'e'));
			if (P != L && P == ST + 1) {
				switch (S[ST]) {
					case '.':
						return TYPE_PERIOD;
					case '+':
					case '-':
						return TYPE_OPERATOR;
				}
			}
			if (P != L)
				switch (S[P]) {
					case 'l':
					case 'L':
					case 'd':
					case 'D':
					case 'f':
					case 'F':
						++P;
				}
			return TYPE_NUMBER;
		}
		return FAILED;
	}

	public final boolean isKeyword(char[] cs, int st, int en) {
		return getKeywordTrie().hasWord(cs, st, en);
	}

	public byte processSymbol(char c) {
		switch (c) {
			// 有 ## 或者 #= 用法的运算符
			case '|':
			case '&':
			case '+':
			case '-': {
				if (P == L) return TYPE_OPERATOR;
				if (S[P] == S[P - 1] || S[P] == '=') {
					++P;
					return TYPE_OPERATOR;
				}
				return TYPE_OPERATOR;
			}
			// 有 # 或者 #= 用法的运算符
			case '!': {
				if (P == L) return TYPE_OPERATOR;
				if (S[P] == '=')
					++P;
				return TYPE_OPERATOR;
			}
			// 有 # 或者 #= 或者 ## 或者 ##= 用法的运算符
			case '>':
			case '<': {
				if (P == L) return TYPE_OPERATOR;
				if (S[P] == c) {
					if (++P == L) return TYPE_OPERATOR;
					if (S[P] == '=') {
						++P;
						return TYPE_OPERATOR;
					}
				} else if (S[P] == '=') ++P;
				return TYPE_OPERATOR;
			}
			// 只有 # 用法的运算符
			case '~':
			case '?':
				return TYPE_OPERATOR;
			// 只有 #= 用法的运算符
			case '^':
			case '%':
			case '*': {
				if (P == L) return TYPE_OPERATOR;
				if (S[P] == '=') {
					++P;
					return TYPE_OPERATOR;
				}
				return TYPE_OPERATOR;
			}
			case '/': {
				if (P == L) return TYPE_OPERATOR;
				switch (S[P]) {
					case '*': {
						boolean star = false;
						do {
							if (++P == L) return TYPE_COMMENT;
							if (S[P] == '*') star = true;
							else {
								if (S[P] == '/' && star) {
									++P;
									return TYPE_COMMENT;
								}
								star = false;
							}
						} while (true);
					}
					case '/': {
						do {
							++P;
						} while (P != L && S[P] != '\n');
						return TYPE_COMMENT;
					}
					case '=':
						++P;
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
				if (P == L) return TYPE_ASSIGNMENT;
				if (S[P] == '=') {
					++P;
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
