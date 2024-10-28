package core.ports

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

trait FileInputPort {
  def getTextFilesFromPath(path: String)(implicit
    sparkSession: SparkSession
  ): DataFrame
}
