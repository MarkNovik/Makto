package type_system

sealed interface MaktoNumber : Mathable {
    override val value: Number
}

value class MaktoInteger(override val value: Long) : MaktoNumber {

    override fun plus(other: Addable): Addable = when (other) {
        is MaktoString -> MaktoString("${this.value}${other.value}")
        is MaktoInteger -> MaktoInteger(this.value + other.value)
        is MaktoFloat -> MaktoFloat(this.value + other.value)
    }

    override fun minus(other: Subtractable): Subtractable = when (other) {
        is MaktoInteger -> MaktoInteger(this.value - other.value)
        is MaktoFloat -> MaktoFloat(this.value - other.value)
    }

    override fun times(other: Multiplicable): Multiplicable = when (other) {
        is MaktoInteger -> MaktoInteger(this.value * other.value)
        is MaktoFloat -> MaktoFloat(this.value * other.value)
    }

    override fun div(other: Divisible): Divisible = when (other) {
        is MaktoInteger -> MaktoInteger(this.value / other.value)
        is MaktoFloat -> MaktoFloat(this.value / other.value)
    }

    override fun debug(): String = "MaktoInteger($value)"
    override fun toString(): String = "$value"
}

value class MaktoFloat(override val value: Double) : MaktoNumber {

    override fun plus(other: Addable): Addable = when (other) {
        is MaktoString -> MaktoString("${this.value}${other.value}")
        is MaktoNumber -> MaktoFloat(this.value + other.value.toDouble())
    }

    override fun minus(other: Subtractable): Subtractable = when (other) {
        is MaktoNumber -> MaktoFloat(this.value - other.value.toDouble())
    }

    override fun times(other: Multiplicable): Multiplicable = when (other) {
        is MaktoNumber -> MaktoFloat(this.value * other.value.toDouble())
    }

    override fun div(other: Divisible): Divisible = when (other) {
        is MaktoNumber -> MaktoFloat(this.value / other.value.toDouble())
    }

    override fun debug(): String = "MaktoFloat($value)"
    override fun toString(): String = "$value"
}