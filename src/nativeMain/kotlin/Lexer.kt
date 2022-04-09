import type_system.MaktoFloat
import type_system.MaktoInteger
import type_system.MaktoString
import type_system.MaktoSymbol

private typealias Chars = MutableList<Char>

class Lexer private constructor(private val src: String) {
    private val integerParts = ('0'..'9') + '-'
    private val floatParts = integerParts + '.'
    private val quotes = "\'\""
    private val intrinsics = mapOf(
        "+" to MathIntrinsic.Plus,
        "-" to MathIntrinsic.Minus,
        "*" to MathIntrinsic.Times,
        "/" to MathIntrinsic.Div,
        "=" to Eq,
        "return" to Return,
        "end" to End,
        "goto" to GoTo,
        "try_goto" to TryGoTo,
        "print" to Print,
        "read" to Read,
        "dup" to StackIntrinsic.Dup,
        "swap" to StackIntrinsic.Swap,
        "over" to StackIntrinsic.Over,
        "drop" to StackIntrinsic.Drop
    )

    private fun Char.allowedInLabelName() = isLetterOrDigit() || this == '_'
    private fun Char.isHexDigit(): Boolean = this in "0123456789abcdefABCDEF"

    private fun Chars.parse(
        condition: (Char) -> Boolean,
        result: (String) -> Op,
        consumed: Char? = null,
        afterLoop: (Chars) -> Unit = {}
    ) {
        val str = buildString {
            consumed?.let(this@buildString::append)
            while (this@parse.isNotEmpty() && condition(this@parse.first())) {
                append(this@parse.removeFirst())
            }
            afterLoop(this@parse)
        }
        res += result(str)
    }

    private fun Chars.parseInteger(consumed: Char) = parse(
        { it in integerParts },
        { if (it == "-") MathIntrinsic.Minus else Push(MaktoInteger(it.toLong())) },
        consumed,
    )

    private fun Chars.parseHex() = parse(
        { it.isHexDigit() },
        { Push(MaktoInteger(it.toLong(16))) }
    )

    private fun Chars.parseFloat(consumed: Char) = parse(
        { it in floatParts },
        { Push(MaktoFloat(it.toDouble())) },
        consumed
    )

    private fun Chars.parseLabel() {
        val name = buildString {
            while (this@parseLabel.isNotEmpty() && this@parseLabel.first().allowedInLabelName())
                append(removeFirst())
        }
        labels += name to res.lastIndex
    }

    private fun Chars.parseString(delimiter: Char) = parse(
        { it != delimiter },
        { Push(MaktoString(it)) },
        null,
        { removeFirst() }
    )

    private fun Chars.parseWord(consumed: Char) = parse(
        { !it.isWhitespace() },
        { intrinsics[it] ?: Push(MaktoSymbol(it)) },
        consumed
    )

    private val res = mutableListOf<Op>()
    private val labels = mutableMapOf<String, Int>()
    private fun lexImpl(): Runtime {
        val chars: Chars = src.withoutComments().toMutableList()
        with(chars) {
            while (chars.isNotEmpty()) {
                when (val c = chars.removeFirst()) {
                    '#' -> parseHex()
                    in integerParts -> parseInteger(c)
                    in floatParts -> parseFloat(c)
                    '@' -> parseLabel()
                    in quotes -> parseString(c)
                    else -> if (!c.isWhitespace()) parseWord(c)
                }
            }
        }

        return Runtime(res, labels)
    }

    companion object {
        fun lex(src: String) = Lexer(src).lexImpl()
    }
}