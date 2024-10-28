package core.models

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._

case class Similarity(score: BigDecimal)
