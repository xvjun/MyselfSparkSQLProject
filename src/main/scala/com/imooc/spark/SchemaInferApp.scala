package com.imooc.spark

import org.apache.spark.sql.SparkSession

/**
  *Schema Infer
  */
object SchemaInferApp {

  def main(args: Array[String]): Unit = {
    val path = args(0)
    val spark = SparkSession.builder().appName("DataFrameApp").master("local[2]").getOrCreate()
    val df = spark.read.format("json").load(path)
    df.printSchema()
    df.show()
    spark.stop()
  }

}
