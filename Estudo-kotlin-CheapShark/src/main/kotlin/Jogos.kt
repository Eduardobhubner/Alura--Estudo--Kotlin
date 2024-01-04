import com.google.gson.annotations.SerializedName

data class Jogos(
    val titulo: String,
    val capa: String
) {
    override fun toString(): String {
        return "Jogos(titulo='$titulo', capa='$capa')"
    }

}