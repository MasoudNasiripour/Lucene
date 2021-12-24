import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/**
 * created by Masoud Nasiripour on 17/12/2021
 */

class QueryParser(val path : String) {

    fun getQueries() : ArrayList<String>{
        val file = File(path)
        val br = BufferedReader(FileReader(file))
        var line: String? = ""
        var current_text = ""
        var current_line_number = 0
        val numberOfLines = Const.numberOfLines(file)
        val quries = ArrayList<String>()

        line = br.readLine()

        while (line != null) {
            current_line_number += 1
            if (line.contains(".W")) {
                line = br.readLine()
                current_line_number += 1
                if (line != null)
                    while (!line!!.contains(".I") && current_line_number < numberOfLines) {
                        current_text += " ${line}"
                        line = br.readLine()
                        current_line_number += 1
                    }
                quries.add(current_text.trim())
                current_text = ""
            }
            line = br.readLine()
        }
        return quries
    }

    fun getNameOfQueries() : ArrayList<String>{
        val file = File(path)
        val br = BufferedReader(FileReader(file))
        val names = ArrayList<String>()
        var line: String? = ""
        line = br.readLine()
        while (line != null){
            if (line.contains(".I")){
                names.add(line)
            }
            line = br.readLine()
        }
        return names
    }
}