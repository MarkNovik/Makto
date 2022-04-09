package type_system

value class MaktoString(override val value: String) : Addable {

    override fun plus(other: Addable): MaktoString =
        MaktoString("${this.value}${other.value}")

    override fun debug(): String = "MaktoString(\"$value\")"
    override fun toString(): String = value
}