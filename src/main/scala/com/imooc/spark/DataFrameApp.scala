package com.imooc.spark

import org.apache.spark.sql.SparkSession

object DataFrameApp {
  /**
    * DAtaFrameAPI基本操作
    */
  def main(args: Array[String]): Unit = {

    val path = args(0)
    val spark = SparkSession.builder().appName("DataFrameApp").master("local[2]").getOrCreate()
    val df = spark.read.json(path)
    df.printSchema()
    df.show()
    df.select("name").show()
    df.select(df.col("name"),(df.col("age" ) + 10).as("age2")).show()
    df.filter(df.col("age") > 19).show()

    //分组bin聚合 select age,count(1) from table group by age
    df.groupBy("age").count().show()
    spark.stop()
  }
}
