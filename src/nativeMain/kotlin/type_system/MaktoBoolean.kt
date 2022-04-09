package type_system

value class MaktoBoolean(override val value: Boolean) : MaktoValue {
    override fun debug(): String = "MaktoBoolean($value)"
    override fun toString(): String = "$value"
}