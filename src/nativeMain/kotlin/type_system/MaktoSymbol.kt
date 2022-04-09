package type_system

value class MaktoSymbol(override val value: String) : MaktoValue {
    override fun debug(): String = "MaktoSymbol(`$value`)"
    override fun toString(): String = "`$value`"
}