package es.oliva.samuel.camerax_mvvm.core


enum class eMediaType(val value: Int) {
    Picture(0),
    Video(1);

    companion object {
        fun fromInt(value: Int): eMediaType {
            return entries.find { it.value == value } ?: Picture
        }
    }
}
