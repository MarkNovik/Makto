import type_system.MaktoValue
import kotlin.system.exitProcess

typealias Labels = Map<String, Int>

class Runtime(
    val ops: List<Op>,
    private val labels: Labels
) {
    private var pos = 0
    private var prevPos = Stack<Int>()
    val stack = Stack<MaktoValue>()

    fun goTo(label: String) {
        prevPos.push(pos)
        pos = labels[label]!!
    }

    fun `return`() {
        pos = prevPos.popOrNull() ?: exitProcess(0)
    }

    fun run() {
        while (pos in ops.indices) {
            ops[pos].operate(this)
            pos++
        }
    }
}

