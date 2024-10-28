package core.models

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{HashingTF, IDF}
import org.apache.spark.sql.{Dataset, Row}

trait DataPreparator {
  def prepareData[T](dataset: Dataset[T]): Array[Row]
}

object TextPreparator extends DataPreparator {

  override def prepareData[String](dataset: Dataset[String]): Array[Row] = {

    val cleanAll             = new CleanAllTransformer()
    val pipelineCleanData    = new Pipeline().setStages(Array(cleanAll))
    val modelCleanData       = pipelineCleanData.fit(dataset)
    val transformedCleanData = modelCleanData.transform(dataset)

    val hashingTF =
      new HashingTF().setInputCol("filtered_words").setOutputCol("rawFeatures")
    val idf       = new IDF().setInputCol("rawFeatures").setOutputCol("features")

    val pipeline        = new Pipeline().setStages(Array(hashingTF, idf))
    val model           = pipeline.fit(transformedCleanData)
    val transformedData = model.transform(transformedCleanData)
    val result          = transformedData.select("features").collect()

    result
  }
}
