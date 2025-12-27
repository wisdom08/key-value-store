package kvstore.persistence

import java.io.File

class AppendOnlyLog(
    private val file: File
) {

    init {
        if (!file.exists()) {
            file.parentFile?.mkdirs()
            file.createNewFile()
        }
    }

    fun appendPut(key: String, value: String) {
        file.appendText("PUT $key $value\n")
    }

    fun appendDelete(key: String) {
        file.appendText("DELETE $key\n")
    }

    fun replay(onPut: (String, String) -> Unit, onDelete: (String) -> Unit) {
        file.forEachLine { line ->
            val parts = line.split(" ")
            when (parts[0]) {
                "PUT" -> onPut(parts[1], parts[2])
                "DELETE" -> onDelete(parts[1])
            }
        }
    }
}
