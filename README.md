<h1 align="center">MLexer</h1>
<p>
  <img alt="Version" src="https://img.shields.io/badge/version-1.0.0-blue.svg?cacheSeconds=2592000" />
  <a href="./LICENSE.md" target="_blank">
    <img alt="License: GNU General Public License v3.0" src="https://img.shields.io/badge/License-GNU General Public License v3.0-yellow.svg" />
  </a>
</p>

> A Powerful Programming Language Lexer Written in Java

[中文版](./README-zh_CN.md)

## Author

👤 **Mivik**

* Website: https://mivik.gitee.io/
* Github: [@Mivik](https://github.com/Mivik)

## Introduction

_MLexer_ is a lexer for several programming languages such as Java, JavaScript, C++, C and so on.

## Advantages

Such programming language lexers are used usually in code highlighting. Many lexer such as _JFlex_ and _JavaCC_ can meet this requirement, however MLexer can do more:

- Dynamic lexing based on modifications.
- Custom document type (not just String!)

What do them mean? Let me explain:

For example, if you wrote a editor and wanted to make it able to highlight code, then you have to use a lexer. However, after modifying the code, you'll have to re-lex all the code again, which can take a long time - **while in _MLexer_, you can just simply call the method `onInsertChars(int pos, int len)` to clarify that some chars have been inserted, _MLexer_ will automatically work out which parts are needed to re-lex and just re-lex needed parts.**

How about custom document type? It means the content parameter in _MLexer_ can be any classes extended from `com.mivik.mlexer.Document` !

But what's its particular application? Imagine now you have a custom document type which manages contents by separating them into lines. If you want to lex them in the normal way, you have to convert the content into String before passing the content to the lexer. However, in _MLexer_, you can make your document type extend from `com.mivik.mlexer.Document` by providing a cursor type extended from `com.mivik.mlexer.Cursor` and implementing several simple methods such as `get(Cursor cursor)`, `moveForward(Cursor cursor)`, then you can directly pass your document to _MLexer_ !

## Show your support

Give a ⭐️ if this project helped you!

## 📝 License

Copyright © 2020 [Mivik](https://github.com/Mivik).

This project is [GNU General Public License v3.0](./LICENSE.md) licensed.
