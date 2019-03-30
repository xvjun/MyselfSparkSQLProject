package com.imooc

import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  * Created by rana on 29/9/16.
  */
object app extends App {
  println("Application started...")

  val conf = new SparkConf().setAppName("spark-custom-datasource")
  val spark = SparkSession.builder().config(conf).master("local[2]").getOrCreate()

  val df = spark.sqlContext.read.format("com.datasource").option("path","D://datasource/*").load()
//    .load("D://datasource.txt")


  df.createOrReplaceTempView("test")
  spark.sql("select * from test where salary = 50000").show()
  spark.sql("select * from test").show()
//  spark.sql("select * from test where salary = 50000").write.option("mode","SaveMode.Overwrite").option("format","json").save("D://datasource2")

  println("Application Ended...")
}