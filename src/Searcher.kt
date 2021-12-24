import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.benchmark.quality.trec.TrecJudge
import org.apache.lucene.document.Document
import org.apache.lucene.search.ScoreDoc
import org.apache.lucene.search.similarities.Similarity
import java.awt.Color
import java.io.*
import java.nio.file.Paths

/**
 * @author Masoud Nasiripour on 17/12/2021
 */
class Searcher(
    private val strQueries: ArrayList<String>,
    private val strQueryNames : ArrayList<String>,
    private val analyzer: Analyzer,
    var similarity: Similarity?
) {
    private val reader = DirectoryReader.open(FSDirectory.open(Paths.get("IndexDir")))
    private var searcher = IndexSearcher(reader)

    init {
        if (similarity != null)
            searcher.similarity = similarity
    }

    fun doSearch(
        searchType: String,
        fileName: String
    ) {
        val retrievalDocsFW = PrintWriter("RD.txt") // Retrieval Documents

        val luceneQueries = luceneQueryMaker(strQueries, searchType)

        val qrelsFolder = File("QRels")
        val rdPath =  File("RD.txt").absolutePath

        if (!qrelsFolder.exists())
            qrelsFolder.mkdirs()
        val file = File(qrelsFolder, "QRel${fileName}.txt")
        Const.isNewFileCreated(file)
        val writer = FileWriter(file)
        for (i in 0 until luceneQueries.size) {
            val topDocs = searcher.search(luceneQueries[i], 100)
            val scores = topDocs.scoreDocs
            makeOutput(i+1, scores, searcher, retrievalDocsFW)
            for (score in scores) {
                val resultTemplate = "${i+1} 0 ${score.doc} ${score.score.toInt()}\n"
                writer.append(resultTemplate)
            }
        }
        if (BufferedReader(FileReader(file)).readLines().isEmpty()) {
            print(Colors.RED_BRIGHT + "No Document was Match to your Query ${Colors.RESET}")
        } else {
            println("\n${Colors.GREEN_BOLD}Result File Created Successfully:${Colors.RESET}\n")
            println("${Colors.BLACK_BOLD}The path of QRel file is: " + Colors.RESET + file.absolutePath + "\n")
            println("${Colors.BLACK_BOLD}The path of Retrieval Document file is: " + Colors.RESET + rdPath + "\n")
            println("**************************${Colors.BLACK_BOLD} Evaluation${Colors.RESET} **************************\n")
            val evalCommand = makeEvalCommand(file.absolutePath, rdPath)
            println("${Colors.BLACK_BOLD}For evaluate the Retrieval Function " +
                    "do the following step with terminal : ${Colors.RESET} \n" +
                    "1. Go to trec_eval-9.0.7 folder with terminal\n" +
                    "2. then type \"make\" in terminal for compile terc_eval folder \n" +
                    "3. finally run below command : \n" +
                    "\"$evalCommand\"")
        }
        writer.flush()
        writer.close()
        retrievalDocsFW.close()
        reader.close()
    }

    private fun makeOutput(QNumber: Int, scores: Array<ScoreDoc>, searcher : IndexSearcher, writer : PrintWriter) {
        for (i in 0 until scores.size){
            val doc = searcher.doc(scores[i].doc)
            val path = doc.get(Const.PATH_KEY)
            if (path != null)
                writer.println("${QNumber} 0 ${path.replace(".I","")} ${i+1} ${scores[i].score} MasoudNasiripour")
        }

    }

    private fun makeEvalCommand(qrelDir : String, outputDir : String) =
        "./trec_eval -m all_trec ${qrelDir} ${outputDir}"


    private fun luceneQueryMaker(strQueries: ArrayList<String>, searchType: String): ArrayList<Query> {

        val luceneQueries = ArrayList<Query>()

        for (i in 0 until strQueries.size) {
            val queryParser = QueryParser(searchType, analyzer)
            queryParser.allowLeadingWildcard = true
            val str = strQueries[i].trim()
            val query = queryParser.parse(str)
            luceneQueries.add(query)
        }
        return luceneQueries
    }

}