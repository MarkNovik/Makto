import kotlin.test.Test
import kotlin.test.assertTrue

class StackTests {
    @Test
    fun `swap swaps correctly`() {
        val sA = Stack<Int>()
        sA.push(1)
        sA.push(2)
        val sB = Stack<Int>()
        sB.push(2)
        sB.push(1)
        sB.swap()
        assertTrue { sB contentEqualsTo sA }
    }
}