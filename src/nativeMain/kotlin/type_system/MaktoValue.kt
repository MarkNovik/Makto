package type_system

sealed interface MaktoValue {
    val value: Any

    fun debug(): String

    companion object {
        fun wrap(input: String): MaktoValue = when {
            input.startsWith("#") -> MaktoInteger(input.drop(1).toLong(16))
            input.toLongOrNull() != null -> MaktoInteger(input.toLong())
            input.toDoubleOrNull() != null -> MaktoFloat(input.toDouble())
            input.toBooleanStrictOrNull() != null -> MaktoBoolean(input.toBooleanStrict())
            else -> MaktoString(input)
        }
    }
}

sealed interface Addable : MaktoValue {
    operator fun plus(other: Addable): Addable
}

sealed interface Subtractable : MaktoValue {
    operator fun minus(other: Subtractable): Subtractable
}

sealed interface Multiplicable : MaktoValue {
    operator fun times(other: Multiplicable): Multiplicable
}

sealed interface Divisible : MaktoValue {
    operator fun div(other: Divisible): Divisible
}

sealed interface Mathable : Addable, Subtractable, Multiplicable, Divisible