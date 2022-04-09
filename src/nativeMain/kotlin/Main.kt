import io.FilePath
import type_system.repl
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    when (args.firstOrNull() ?: usage()) {
        "-f" -> {
            val path = args.getOrNull(1) ?: usage()
            val runtime = FilePath(path).readText().let(Lexer::lex)
            runtime.run()
        }
        "-r" -> repl()
    }
}

fun usage(): Nothing {
    println("Usage: Makto <flag> <file-path>?")
    println("flags:\n\t-f: interpret file\n\t-r: start repl")
    exitProcess(0)
}

fun String.withoutComments(): String =
    replace(Regex("""\([^)]*\)"""), "")