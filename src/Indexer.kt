import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.StringField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.search.similarities.Similarity
import org.apache.lucene.store.FSDirectory
import java.io.File
import java.nio.file.Paths

/**
 * @author Masoud Nasiripour on 17/12/2021
 */
class Indexer(
    var titles: ArrayList<String>,
    var authors: ArrayList<String>,
    var contents: ArrayList<String>,
    var docs: ArrayList<String>,
    val path : ArrayList<String>,
    var analyzerType: Analyzer,
    var similarity : Similarity?
) {

    init {
        val indexDir = File("IndexDir")

        if (!indexDir.exists())
            indexDir.mkdirs()

        val icw = IndexWriterConfig(analyzerType)
        if (similarity != null)
            icw.similarity = similarity

        val dir = FSDirectory.open(Paths.get(indexDir.path))

        val indexWriter = IndexWriter(dir, icw)

        val docs = getDocs(titles, authors, contents, docs)

        indexWriter.deleteAll()

        indexWriter.addDocuments(docs)

        indexWriter.setChanges()
    }

    private fun IndexWriter.setChanges() {
        commit()
        close()
    }

    private fun getDoc(title: String, author: String, content: String, document: String, path : String): Document {

        val doc = Document()
        doc.add(StringField(Const.TITLE_KEY, title, Field.Store.YES))
        doc.add(StringField(Const.AUTHOR_KEY, author, Field.Store.YES))
        doc.add(TextField(Const.CONTENT_KEY, content, Field.Store.YES))
        doc.add(TextField(Const.DOC_KEY, document, Field.Store.YES))
        doc.add(TextField(Const.PATH_KEY, path, Field.Store.YES))
        return doc
    }

    private fun getDocs(
        titles: ArrayList<String>,
        authors: ArrayList<String>,
        contents: ArrayList<String>,
        docs: ArrayList<String>
    ): ArrayList<Document> {

        val allDocs = ArrayList<Document>()
        for (i in 0 until docs.size) {
            allDocs.add(getDoc(titles[i], authors[i], contents[i], docs[i], path[i]))
        }
        return allDocs
    }


}