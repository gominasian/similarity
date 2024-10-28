import cats.effect.{IO, IOApp}
import controllers.DocumentSimilarityController
import core.adapters.FileInputAdapter
import core.models.TextPreparator
import core.services.DocumentSimilarityServiceImpl
import org.apache.spark.sql.SparkSession
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits._

object Server extends IOApp.Simple {

  implicit val spark: SparkSession = SparkSession.builder
    .appName("Document Similarity Pipeline")
    .config("spark.master", "local")
    .getOrCreate()

  val documentSimilarityService =
    new DocumentSimilarityServiceImpl(new FileInputAdapter, TextPreparator)
  val similarityController      = new DocumentSimilarityController(documentSimilarityService)

  val httpApp = similarityController.route.orNotFound

  def run: IO[Unit] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
}
