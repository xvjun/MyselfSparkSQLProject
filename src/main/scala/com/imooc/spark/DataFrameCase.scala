package com.imooc.spark

import org.apache.spark.sql.SparkSession

object DataFrameCase {
  /**
    * DataFrameAPI的使用
    */
  def main(args: Array[String]): Unit = {
    val path = args(0)
    val spark = SparkSession.builder().appName("DataFrameApp").master("local[2]").getOrCreate()
    val rdd = spark.sparkContext.textFile(path)

    import spark.implicits._
    val studentdf = rdd.map(_.split("\\|")).map(line => Student(line(0).toInt, line(1), line(2), line(3))).toDF()
    studentdf.printSchema()
    studentdf.show(30,false)

  }
  case class Student(id: Int, name: String, phone:String, email: String)
}
