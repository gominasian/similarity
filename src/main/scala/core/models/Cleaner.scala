package core.models

import org.apache.spark.ml.Transformer
import org.apache.spark.ml.feature.{RegexTokenizer, StopWordsRemover}
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Dataset}

class CleanAllTransformer(override val uid: String) extends Transformer {
  def this() = this(Identifiable.randomUID("cleanAllTransformer"))

  private val tokenizer = new RegexTokenizer()
    .setPattern("\\W")
    .setInputCol("value")
    .setOutputCol("words")

  private val stopWordsRemover = new StopWordsRemover()
    .setInputCol("words")
    .setOutputCol("filtered_words")

  private def lowercase(dataFrame: DataFrame): DataFrame =
    dataFrame.withColumn("value", lower(col("value")))

  private def removePunctuation(dataFrame: DataFrame): DataFrame =
    dataFrame.withColumn("value", regexp_replace(col("value"), "[^\\w\\s]", ""))

  private def tokenize(dataFrame: DataFrame): DataFrame =
    tokenizer.transform(dataFrame)

  private def removeStopWords(dataFrame: DataFrame): DataFrame =
    stopWordsRemover.transform(dataFrame)

  private def removeNumbers(dataFrame: DataFrame): DataFrame =
    dataFrame.withColumn("filtered_words", expr("filter(filtered_words, x -> x NOT RLIKE '\\\\d')"))

  private def trimSpaces(dataFrame: DataFrame): DataFrame =
    dataFrame.withColumn("filtered_words", expr("transform(filtered_words, x -> trim(x))"))

  override def transform(dataset: Dataset[_]): DataFrame = {
    val ds = dataset.toDF()

    val cleanedData = ds
      .transform(lowercase)
      .transform(removePunctuation)
      .transform(tokenize)
      .transform(removeStopWords)
      .transform(removeNumbers)
      .transform(trimSpaces)
      .filter(size(col("filtered_words")) > 0)

    cleanedData
  }

  override def copy(extra: ParamMap): CleanAllTransformer = defaultCopy(extra)

  override def transformSchema(schema: StructType): StructType = schema
}
