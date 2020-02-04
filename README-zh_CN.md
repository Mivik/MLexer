<h1 align="center">MLexer</h1>
<p>
  <img alt="版本" src="https://img.shields.io/badge/版本-1.0.0-blue.svg?cacheSeconds=2592000" />
  <a href="./LICENSE.md" target="_blank">
    <img alt="开源许可证: GNU General Public License v3.0" src="https://img.shields.io/badge/开源许可证-GNU General Public License v3.0-yellow.svg" />
  </a>
</p>

> 一个基于Java的编程语言词法解析器（Lexer）

[English Version](./README.md)

## 作者

👤 **Mivik**

* 主页: https://mivik.gitee.io/
* Github: [@Mivik](https://github.com/Mivik)

## 简介

_MLexer_ 是一个为Java、JavaScript、C++、C等编程语言设计的词法解析器。

## 特点

这类的编程语言词法解析器通常会用在代码高亮中。许多类似于 _JFlex_ 和 _JavaCC_ 的词法解析器都可以使用Java来满足这个需求，但 _MLexer_ 能做的事更多：

- 基于修改操作的动态词法解析
- 自定义文档（Document）类型（不只能使用String！）

这意味着什么？

举个例子，如果你写了一个编辑器并希望它能够高亮代码，那么你就需要用到词法解析器。但在修改代码后，你必须要重新解析代码，这可能会花上很长时间——**但在 _MLexer_ 中，你可以直接调用 `onInsertChars(int pos, int len)` 方法来告知 _MLexer_ 你做出了一个修改，_MLexer_ 就会自动分析出那些部分是需要重新解析的并只重新解析这些需要的部分。**

那么自定义文档类型又有什么用？它意味这 _MLexer_ 接受的内容参数可以是任意继承自`com.mivik.mlexer.Document`的类！

但它的实际用处是什么呢？假设你有一个自定义的文档类型，通过把文字按行分割来管理文档。如果你想对这个文档进行词法解析，你可能需要把它先转换为`String`然后再传入 Lexer。然而，在 _MLexer_ 中，你只需要通过继承`com.mivik.mlexer.Document`，提供一个继承自`com.mivik.mlexer.Cursor`的光标类型，然后实现几个类似于`get(Cursor indicator)`、`moveForward(Cursor indicator)`的方法，就可以把你的文档直接传给 _MLexer_ 啦！

## 支持本项目

如果这个项目有帮到你，就给它一个⭐️吧！

## 📝 开源许可证

Copyright © 2020 [Mivik](https://github.com/Mivik).

本项目使用 [GNU General Public License v3.0](./LICENSE.md) 开源许可证.
