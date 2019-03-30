package com.imooc.spark

import org.apache.spark.sql.SparkSession

object ParquetApp {

  def main(args: Array[String]): Unit = {

    val path = args(0)
    val spark = SparkSession.builder().appName("DataFrameApp").master("local[2]").getOrCreate()
    val df = spark.read.format("parquet").load(path)

    df.printSchema()
    df.show()

    df.select("name", "favorite_color").show()
    df.select("name", "favorite_color").write.format("json").save("file///root/data")

    spark.stop()

  }

}
