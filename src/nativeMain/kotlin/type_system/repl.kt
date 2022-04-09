package type_system

import Lexer
import Runtime

fun repl() {
    val runtime = Runtime(emptyList(), emptyMap())
    println("Welcome to makto REPL")
    println("Makto REPL doesn't support labels, (try_)goto and return/end")
    while (true) {
        print("makto> ")
        val input = readlnOrNull()?.let { Lexer.lex(it).ops } ?: continue
        input.forEach {
            if (it in replUnsupported)
                println("$it is unsupported in repl")
            else
                it.operate(runtime)
        }
        println(runtime.stack)
    }
}

val replUnsupported = listOf(
    "goto",
    "try_goto",
    "return",
    "end"
).map { Lexer.lex(it).ops.single() }