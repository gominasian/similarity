package core.adapters

import core.models.TextDocument
import core.ports.FileInputPort
import org.apache.spark.sql.functions.input_file_name
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.sparkproject.dmg.pmml.text.TextDocument

import java.nio.file.Paths

class FileInputAdapter extends FileInputPort {

  override def getTextFilesFromPath(path: String)(implicit
    sparkSession: SparkSession
  ): DataFrame =
    sparkSession.read
      .option("recursiveFileLookup", "true")
      .textFile(path)
      .withColumn("filePath", input_file_name())

}
