package core.models

import org.apache.spark.sql.Encoders

case class TextDocument(filename: String, content: String)
object TextDocument {
  implicit val documentEncoder = Encoders.product[TextDocument]
}
