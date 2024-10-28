package controllers

import cats.effect.IO
import core.models.Similarity
import core.services.DocumentSimilarityService
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.circe._
import org.http4s.multipart._
import io.circe.generic.auto._
import io.circe.syntax._

class DocumentSimilarityController(documentSimilarityService: DocumentSimilarityService) {

  val route: HttpRoutes[IO] = HttpRoutes.of[IO] { case req @ POST -> Root / "similarity" =>
    for {
      form   <- req.as[Multipart[IO]]
      files   = form.parts.filter(_.filename.isDefined)
      result <- if (files.size == 2) {
                  val contentIO = files.map(_.bodyText.compile.string).toList
                  contentIO match {
                    case List(content1, content2) =>
                      documentSimilarityService.checkSimilarity(content1, content2).flatMap {
                        score =>
                          Ok(Similarity(score).asJson)
                      }
                    case _                        => BadRequest("Expected exactly two files")
                  }
                } else {
                  BadRequest("Expected exactly two files in the request")
                }
    } yield result
  }
}
