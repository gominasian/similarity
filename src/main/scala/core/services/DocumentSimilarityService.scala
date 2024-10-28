package core.services

import core.adapters.FileInputAdapter
import core.models._
import org.apache.spark.ml.linalg.Vector
import org.apache.spark.sql.{Row, SparkSession}

import scala.io.StdIn

trait DocumentSimilarityService {
  def checkSimilarity(): Option[BigDecimal]
}
class DocumentSimilarityServiceImpl(
  fileInputAdapter: FileInputAdapter,
  dataPreparator: DataPreparator
)(implicit sparkSession: SparkSession)
    extends DocumentSimilarityService {

  override def checkSimilarity(): Option[BigDecimal] = for {
    path            <- Option(getUserInput("Enter path: "))
    documents        = fileInputAdapter.getTextFilesFromPath(path)
    preparedDocs     = dataPreparator.prepareData(documents)
    indexToCompare0 <- getUserInput("Choose first document to compare: ").toIntOption
    indexToCompare1 <- getUserInput("Choose second document to compare: ").toIntOption
    evaluation       = compareDocumentsByIndex(preparedDocs, indexToCompare0, indexToCompare1)
  } yield {
    println(
      s"For file #$indexToCompare0 and file #$indexToCompare1 cosine similarity is ${evaluation
          .setScale(2, BigDecimal.RoundingMode.HALF_UP)}"
    )
    evaluation

  }

  private def getUserInput(message: String): String =
    StdIn.readLine(message)

  private def compareDocumentsByName(
    documents: Array[Row],
    documentName0: String,
    documentName1: String
  ): BigDecimal = {
    val v0 = finalValueToVector(
      documents.filter(row => row.getAs[String](0) == documentName0)(0)
    )
    val v1 = finalValueToVector(
      documents.filter(row => row.getAs[String](0) == documentName1)(0)
    )
    defineCosineSimilarity(v0, v1)
  }

  private def compareDocumentsByIndex(
    documents: Array[Row],
    documentIndex0: Int,
    documentIndex1: Int
  ): BigDecimal = {
    val v0 = finalValueToVector(
      documents(documentIndex0)
    )
    val v1 = finalValueToVector(
      documents(documentIndex1)
    )
    defineCosineSimilarity(v0, v1)
  }

  private def finalValueToVector(row: Row): Vector = row.getAs[Vector](row.size - 1)

  private def defineCosineSimilarity(vec1: Vector, vec2: Vector): BigDecimal = {
    val dotProduct = vec1.toArray.zip(vec2.toArray).map { case (x, y) => x * y }.sum
    val magnitude1 = math.sqrt(vec1.toArray.map(x => x * x).sum)
    val magnitude2 = math.sqrt(vec2.toArray.map(y => y * y).sum)
    BigDecimal(dotProduct / (magnitude1 * magnitude2))
  }

}
