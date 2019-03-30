package com.imooc.spark

import org.apache.spark.sql.SparkSession

object SparkSessionApp {
  /**
    * SparkSession的使用
    */
  def main(args: Array[String]): Unit = {
    val path = args(0)
//    val path1 = args(1)
    val spark = SparkSession.builder().master("local[2]")
      .appName("SparkSessionApp").getOrCreate()
    val people = spark.read.json(path)
    people.printSchema()
    people.show()




    spark.stop()
  }
}
