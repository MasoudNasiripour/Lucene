import java.io.*

/**
* created by Masoud Nasiripour on 17/12/2021
*/

class DocParser(val path: String) {

    fun sepDocs(sep: String): ArrayList<String> {
        val file = File(path)
        val numberOfLines = Const.numberOfLines(file)
        val br = BufferedReader(FileReader(file))
        var line: String? = ""
        var current_doc = ""
        var current_line_number = 0
        val docs = ArrayList<String>()
        line = br.readLine()

        while (line != null) {

            current_line_number += 1

            if (current_line_number == 1) {
                current_doc = line
            } else if (current_line_number in 2 until numberOfLines) {
                if (line.contains(sep)) {
                    docs.add(current_doc)
                    current_doc = line
                } else
                    current_doc += "\n${line}"
            } else {
                current_doc += "\n${line}"
                docs.add(current_doc)
            }
            line = br.readLine()
        }
        return docs
    }

    fun headers(): ArrayList<String> {
        val file = File(path)
        val br = BufferedReader(FileReader(file))
        var line: String? = ""
        var current_text = ""
        var current_line_number = 0
        val numberOfLines = Const.numberOfLines(file)
        val titles = ArrayList<String>()

        line = br.readLine()

        while (line != null) {
            current_line_number += 1
            if (line.contains(".T")) {
                line = br.readLine()
                current_line_number += 1
                if (line != null)
                    while (!line!!.contains(".A") && current_line_number < numberOfLines) {
                        current_text += "\n${line}"
                        line = br.readLine()
                        current_line_number += 1
                    }
                titles.add(current_text)
                current_text = ""
            }
            line = br.readLine()
        }
        return titles
    }

    fun authors(): ArrayList<String> {
        val file = File(path)
        val br = BufferedReader(FileReader(file))
        var line: String? = ""
        var current_text = ""
        var current_line_number = 0
        val numberOfLines = Const.numberOfLines(file)
        val authors = ArrayList<String>()

        line = br.readLine()

        while (line != null) {
            current_line_number += 1
            if (line.contains(".A")) {
                line = br.readLine()
                current_line_number += 1
                if (line != null)
                    while (!line!!.contains(".B") && current_line_number < numberOfLines) {
                        current_text += "\n${line}"
                        line = br.readLine()
                        current_line_number += 1
                    }
                authors.add(current_text)
                current_text = ""
            }
            line = br.readLine()
        }
        return authors
    }

    fun text(): ArrayList<String> {
        val file = File(path)
        val br = BufferedReader(FileReader(file))
        var line: String? = ""
        var current_text = ""
        var current_line_number = 0
        val numberOfLines = Const.numberOfLines(file)
        val texts = ArrayList<String>()

        line = br.readLine()

        while (line != null) {
            current_line_number += 1
            if (line.contains(".W")) {
                line = br.readLine()
                current_line_number += 1
                if (line != null)
                    while (!line!!.contains(".I") && current_line_number < numberOfLines) {
                        current_text += "\n${line}"
                        line = br.readLine()
                        current_line_number += 1
                    }
                texts.add(current_text)
                current_text = ""
            }
            line = br.readLine()
        }
        return texts
    }

    fun getPath(): ArrayList<String>{

        val file = File(path)
        val br = BufferedReader(FileReader(file))
        val path = ArrayList<String>()
        var line: String? = ""
        line = br.readLine()
        while (line != null){
            if (line.contains(".I")){
                path.add(line)
            }
            line = br.readLine()
        }
        return path

    }

}