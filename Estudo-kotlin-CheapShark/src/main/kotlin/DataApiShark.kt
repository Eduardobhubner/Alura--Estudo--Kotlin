data class DataApiShark(val title: String, val thumb: String){
    override fun toString(): String {
        return "Jogo -- (titulo='$title', capa='$thumb')"
    }
}
