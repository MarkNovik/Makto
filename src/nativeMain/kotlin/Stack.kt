class Stack<T> {
    private data class Node<T>(val value: T, val next: Node<T>?) {
        override fun toString(): String = if (next == null) "$value" else "$value -> $next"
    }

    private var head: Node<T>? = null

    infix fun contentEqualsTo(other: Stack<T>): Boolean = this.head == other.head

    fun push(v: T) {
        head = Node(v, head)
    }

    fun peekOrNull(): T? = head?.value
    fun peek(): T = peekOrNull() ?: throw EmptyStackException

    fun popOrNull(): T? = head?.value.also { head = head?.next }
    fun pop(): T = popOrNull() ?: throw EmptyStackException

    fun dup() = perform { (a) -> ret(a, a) }
    fun swap() = perform { (a, b) -> ret(a, b) }
    fun over() = perform { (a, b) -> ret(a, b, a) }
    fun drop() {
        pop()
    }

    private fun ret(vararg ts: T) = ts.toList()
    private fun perform(action: (Stack<T>) -> List<T>) = action(this).forEach(this::push)


    operator fun component1() = pop()
    operator fun component2() = pop()
    operator fun component3() = pop()

    override fun toString(): String =
        "[${head ?: ""}]"
}

object EmptyStackException : Exception("The stack was empty")