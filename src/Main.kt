import org.apache.lucene.analysis.core.SimpleAnalyzer
import org.apache.lucene.analysis.core.WhitespaceAnalyzer
import org.apache.lucene.analysis.en.EnglishAnalyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.search.similarities.LMDirichletSimilarity
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity
import org.apache.lucene.search.similarities.Similarity

import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Masoud Nasiripour on 17/12/2021
 */

fun main(args: Array<String>) {

    // doc parsers
    val docParser = DocParser(Const.docs_path)
    val docs = docParser.sepDocs(".I")
    val strTitles = docParser.headers()
    val strAuthors = docParser.authors()
    val strContents = docParser.text()
    val strPath = docParser.getPath()


    println(
        Colors.GREEN_BOLD + "Please choose your Similarity Type(1/2/3/4): \n" +
                Colors.BLACK + "1. Smoothing(JM)\n" +
                Colors.BLACK + "2. Smoothing(Drishlet)\n" +
                Colors.BLACK + "3. TF-IDF\n" +
                Colors.BLACK + "4. Use Lucene default similarity" +
                Colors.RESET

    )

    print(Colors.BLACK_BOLD + "Your Choose : " + Colors.RESET)
    val simType = Scanner(System.`in`).nextInt()



    when(simType){
        1-> {
            println("Loading..." + Colors.RESET)
            println(Colors.WHITE + "LMJelinekMercerSimilarity Chosen" + Colors.RESET)
            indexing(strTitles, strAuthors, strContents, docs, QueryParser(Const.qry_path).getQueries(),strPath , LMJelinekMercerSimilarity(0.5f), args)

        }
        2->{
            println("Loading..." + Colors.RESET)
            println(Colors.WHITE + "LMDirichletSimilarity Chosen" + Colors.RESET)
            indexing(strTitles, strAuthors, strContents, docs, QueryParser(Const.qry_path).getQueries(),strPath , LMDirichletSimilarity(), args)

        }
        3-> {
            println("Loading..." + Colors.RESET)
            println(Colors.WHITE + "TF_IDF_Similarity Chosen" + Colors.RESET)
            indexing(strTitles, strAuthors, strContents, docs, QueryParser(Const.qry_path).getQueries(),strPath , TF_IDF_Similarity(), args)

        }
        4-> {
            println("Loading..." + Colors.RESET)
            println(Colors.WHITE + "Lucene default Similarity Chosen" + Colors.RESET)
            indexing(strTitles, strAuthors, strContents, docs, QueryParser(Const.qry_path).getQueries(),strPath , null, args)

        }
        else -> {
            println(Colors.RED_BRIGHT + "Entered Number is Wrong! Please try again..." + Colors.RESET)
            println("Starting Lucene..." + Colors.RESET)
            main(args)
        }
    }


}

fun indexing(
    titles: ArrayList<String>,
    authors: ArrayList<String>,
    contents: ArrayList<String>,
    docs: ArrayList<String>,
    strQueries: ArrayList<String>,
    path: ArrayList<String>,
    sim : Similarity?,
    args : Array<String>
) {

    val inputAnalyserType = Scanner(System.`in`)

    println(
        Colors.GREEN_BOLD + "Please choose your Analysis Type(1/2/3/4/5): \n" +
                Colors.BLACK + "1. StandardAnalyser (Without stop words)\n" +
                Colors.BLACK + "2. SimpleAnalyzer (With stop word)\n" +
                Colors.BLACK + "3. EnglishAnalyzer (With Family words)\n" +
                Colors.BLACK + "4. WhitespaceAnalyzer (Without Family words)\n" +
                Colors.BLACK + "5. Back Step" +
                Colors.RESET

    )

    print(Colors.BLACK_BOLD + "Your Choose : " + Colors.RESET)


    when (inputAnalyserType.nextInt()) {
        1 -> {
            println("Loading..." + Colors.RESET)
            Indexer(titles, authors, contents, docs, path , StandardAnalyzer(), sim)
            val searcher = Searcher(strQueries, QueryParser(Const.qry_path).getNameOfQueries() ,StandardAnalyzer(),sim)
            println(Colors.WHITE + "StandardAnalyser Chosen" + Colors.RESET)
            search(titles, authors, contents, docs, strQueries,path , sim , searcher,args)
        }
        2 -> {
            println("Loading..." + Colors.RESET)
            Indexer(titles, authors, contents, docs, path, SimpleAnalyzer(),sim)
            val searcher = Searcher(strQueries,QueryParser(Const.qry_path).getNameOfQueries() ,  SimpleAnalyzer(),sim)
            println(Colors.WHITE + "SimpleAnalyser Chosen" + Colors.RESET)
            search(titles, authors, contents, docs, strQueries,path , sim ,searcher,args)

        }
        3 -> {
            println("Loading..." + Colors.RESET)
            Indexer(titles, authors, contents, docs, path , EnglishAnalyzer(),sim)
            val searcher = Searcher(strQueries, QueryParser(Const.qry_path).getNameOfQueries() ,EnglishAnalyzer(),sim)
            println(Colors.WHITE + "EnglishAnalyser Chosen" + Colors.RESET)
            search(titles, authors, contents, docs, strQueries, path ,sim ,searcher,args)

        }

        4 -> {
            println("Loading..." + Colors.RESET)
            Indexer(titles, authors, contents, docs,path , WhitespaceAnalyzer(),sim)
            val searcher = Searcher(strQueries, QueryParser(Const.qry_path).getNameOfQueries() ,WhitespaceAnalyzer(),sim)
            println(Colors.WHITE + "WhitespaceAnalyser Chosen" + Colors.RESET)
            search(titles, authors, contents, docs, strQueries,path , sim ,searcher,args)

        }

        5-> {
            println("Loading..." + Colors.RESET)
            main(args)
        }

        else -> {
            println(Colors.RED_BRIGHT + "Entered Number is Wrong! Please try again..." + Colors.RESET)
            indexing(titles, authors, contents, docs, strQueries,path ,sim, args)
        }
    }

}

fun search(
    titles: ArrayList<String>,
    authors: ArrayList<String>,
    contents: ArrayList<String>,
    docs: ArrayList<String>,
    strQueries: ArrayList<String>,
    path: ArrayList<String>,
    sim : Similarity?,
    searcher: Searcher,
    args : Array<String>
) {

    val inputSearchType = Scanner(System.`in`)

    println(
        Colors.GREEN_BOLD + "Please Select the search type(1/2/3/4/5):\n" +
                Colors.BLACK + "1. Search in Titles\n" +
                Colors.BLACK + "2. Search in Authors\n" +
                Colors.BLACK + "3. Search in Document Text\n" +
                Colors.BLACK + "4. Search in All(titles & Authors & Documents)\n" +
                Colors.BLACK + "5. Back step" +
                Colors.RESET
    )


    print(Colors.BLACK_BOLD + "Your Choose : " + Colors.RESET)

    when (inputSearchType.nextInt()) {
        1 -> {
            println("Loading..." + Colors.RESET)
            println(Colors.WHITE + "Start Searching in Titles..."+
                    Colors.RESET)
            searcher.doSearch(Const.TITLE_KEY, "Title")
        }
        2 -> {
            println("Loading..." + Colors.RESET)
            println(Colors.WHITE + "Start Searching in Authors..."+
                    Colors.RESET)
            searcher.doSearch(Const.AUTHOR_KEY, "Author")
        }
        3 -> {
            println("Loading..." + Colors.RESET)
            println(Colors.WHITE + "Start Searching in Contents..."+
                    Colors.RESET)
            searcher.doSearch(Const.CONTENT_KEY, "Content")
        }
        4 -> {
            println("Loading..." + Colors.RESET)
            println(Colors.WHITE + "Start Searching in All(titles & Authors & Documents)... "+
                    Colors.RESET)
            searcher.doSearch(Const.DOC_KEY, "Doc")
        }

        5->{
            println("Loading..." + Colors.RESET)
            indexing(titles, authors, contents, docs, strQueries, path , sim ,args)
        }

        else -> {
            println(Colors.RED_BRIGHT + "Entered Number is Wrong! Please try again..." + Colors.RESET)
            search(titles, authors, contents, docs, strQueries,path , sim ,searcher,args)
        }
    }
}