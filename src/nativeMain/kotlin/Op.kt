import type_system.*
import kotlin.system.exitProcess

sealed interface Op {
    fun operate(runtime: Runtime)
}

object Print : Op {
    override fun operate(runtime: Runtime) {
        println(runtime.stack.pop())
    }

    override fun toString(): String =
        "Print"
}

value class Push(val what: MaktoValue) : Op {
    override fun operate(runtime: Runtime) {
        runtime.stack.push(what)
    }

    override fun toString(): String =
        "Push($what)"
}

object GoTo : Op {
    override fun operate(runtime: Runtime) {
        val label = runtime.stack.pop()
        if (label !is MaktoSymbol) error("$label is not a symbol")
        runtime.goTo(label.value)
    }

    override fun toString(): String = "`goto`"
}

object TryGoTo : Op {
    override fun operate(runtime: Runtime) {
        val label = runtime.stack.pop()
        if (label !is MaktoSymbol) error("$label is not a symbol")
        val cond = runtime.stack.pop()
        if (cond !is MaktoBoolean) error("Checking non-boolean value $cond with try_goto")
        if (cond.value) runtime.goTo(label.value)
    }

    override fun toString(): String = "`try_goto`"
}

enum class MathIntrinsic : Op {
    Plus,
    Minus,
    Div,
    Times;

    override fun operate(runtime: Runtime) = when (this) {
        Plus -> {
            val a = runtime.stack.pop()
            val b = runtime.stack.pop()
            if (a !is Addable || b !is Addable) error("Cannot add $a to $b")
            runtime.stack.push(b + a)
        }
        Minus -> {
            val a = runtime.stack.pop()
            val b = runtime.stack.pop()
            if (a !is Subtractable || b !is Subtractable) error("Cannot subtract $b from $a")
            runtime.stack.push(b - a)
        }
        Div -> {
            val a = runtime.stack.pop()
            val b = runtime.stack.pop()
            if (a !is Divisible || b !is Divisible) error("Cannot divide $a by $b")
            runtime.stack.push(b / a)
        }
        Times -> {
            val a = runtime.stack.pop()
            val b = runtime.stack.pop()
            if (a !is Multiplicable || b !is Multiplicable) error("Cannot multiply $a by $b")
            runtime.stack.push(b * a)
        }
    }
}

object Eq : Op {
    override fun operate(runtime: Runtime) = with(runtime.stack) {
        push(MaktoBoolean(pop().value == pop().value))
    }
}

object Read : Op {
    override fun operate(runtime: Runtime) {
        val input = readlnOrNull() ?: ""
        runtime.stack.push(MaktoValue.wrap(input))
    }
}

object Return : Op {
    override fun operate(runtime: Runtime) {
        runtime.`return`()
    }

    override fun toString(): String = "`return`"
}

object End : Op {
    override fun operate(runtime: Runtime) {
        exitProcess(0)
    }

    override fun toString(): String = "`end`"
}

enum class StackIntrinsic(val action: Stack<MaktoValue>.() -> Unit) : Op {
    Dup(Stack<MaktoValue>::dup),
    Swap(Stack<MaktoValue>::swap),
    Over(Stack<MaktoValue>::over),
    Drop(Stack<MaktoValue>::drop);

    override fun operate(runtime: Runtime) {
        runtime.stack.action()
    }
}