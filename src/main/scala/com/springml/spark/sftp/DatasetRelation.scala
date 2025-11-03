package com.springml.spark.sftp

import org.apache.log4j.Logger
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SQLContext}
import org.apache.spark.sql.sources.{BaseRelation, TableScan}
import org.apache.spark.sql.types.StructType

case class DatasetRelation(
    fileLocation: String,
    fileType: String,
    inferSchema: String,
    header: String,
    delimiter: String,
    quote: String,
    escape: String,
    multiLine: String,
    rowTag: String,
    customSchema: StructType,
    sqlContext: SQLContext) extends BaseRelation with TableScan {

    private val logger = Logger.getLogger(classOf[DatasetRelation])

    val df: DataFrame = read()

    private def read(): DataFrame = {
      var reader = sqlContext.read
      if (customSchema != null) {
        reader = reader.schema(customSchema)
      }

      var df: DataFrame = null

      df = fileType match {
        case "avro" => reader.format("avro").load(fileLocation)
        case "txt" => reader.format("text").load(fileLocation)
        case "xml" => reader.format(constants.xmlClass)
          .option(constants.xmlRowTag, rowTag)
          .load(fileLocation)
        case "csv" =>
          reader.option("header", header)
            .option("delimiter", delimiter)
            .option("quote", quote)
            .option("escape", escape)
            .option("multiLine", multiLine)
            .option("inferSchema", inferSchema)
            .csv(fileLocation)
        case other =>
          reader.format(other).load(fileLocation)
      }
      df
    }

  override def schema: StructType = df.schema

  override def buildScan(): RDD[Row] = df.rdd
}
