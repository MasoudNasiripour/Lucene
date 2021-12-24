import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * @author Masoud Nasiripour on 17/12/2021
 */
object Const {

    const val qry_path = "cran.qry.txt"
    const val docs_path = "cran.all.1400.txt"

    const val TITLE_KEY = "title"
    const val AUTHOR_KEY = "author"
    const val CONTENT_KEY = "content"
    const val DOC_KEY = "document"
    const val PATH_KEY = "path"

    fun deleteFiles(root: File) {
        if (root.listFiles()!!.isNotEmpty()) {
            for (j in root.listFiles()!!) {
                j.delete()
            }
        }
        root.delete()
    }

    fun renameFile(from : File, dir : String, newName : String){
        val to = File(dir, newName)
        if (from.exists())
            from.renameTo(to)
    }

    fun isNewFileCreated(file: File): Boolean = file.createNewFile()

    fun createTXTfiles(txt: ArrayList<String>): Boolean? {
        var state: Boolean? = false
        var root = File("Docs")

        if (!root.exists())
            root.mkdirs()

        if (root.listFiles().isEmpty()) {
            for (i in 0 until txt.size - 1) {
                val file = File(root, "Content_${i}.txt")
                if (isNewFileCreated(file)) {
                    val fileWriter = FileWriter(file)
                    fileWriter.append(txt[i])
                    fileWriter.flush()
                    fileWriter.close()
                    state = true
                    println(state)
                } else {
                    state = false
                    println(state)
                }
            }
        } else {
            if (root.listFiles().size > 0) {
                for (j in root.listFiles()) {
                    j.delete()
                }
            }
            root.delete()
            createTXTfiles(txt)
        }

        return state
    }

    fun numberOfStringLines(doc: String): Int = doc.lines().size



    fun numberOfLines(file: File): Int {
        val br_numOfLines = BufferedReader(FileReader(file))
        var count = 0
        while (br_numOfLines.readLine() != null)
            count++

        return count
    }

}