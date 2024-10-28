import core.adapters.FileInputAdapter
import core.models.TextPreparator
import core.services.DocumentSimilarityServiceImpl
import org.apache.spark.sql.SparkSession

object SimilarityCheck extends App {

  val documentSimilarityService =
    new DocumentSimilarityServiceImpl(new FileInputAdapter, TextPreparator)

  implicit val spark: SparkSession = SparkSession.builder
    .appName("Document Similarity Pipeline")
    .config("spark.master", "local")
    .getOrCreate()

  documentSimilarityService.checkSimilarityConsole()
}
