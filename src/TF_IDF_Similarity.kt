import org.apache.lucene.search.similarities.TFIDFSimilarity
import kotlin.math.log

class TF_IDF_Similarity : TFIDFSimilarity(){

    override fun tf(frequency : Float): Float {
        return log(frequency.toDouble(), 10.0 ).toFloat()
    }

    override fun idf(docFrequency: Long, docCount: Long): Float {
        return (1 + log((docCount/docFrequency).toDouble() , 10.0)).toFloat()
    }

    override fun lengthNorm(p0: Int): Float = 1F

    
}