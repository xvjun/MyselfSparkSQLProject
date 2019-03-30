package com.imooc.spark

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}


object DataFrameRDDApp {
  /**
    * DAtaFrame和RDD基本操作
    */
  def main(args: Array[String]): Unit = {

    val path = args(0)
    val spark = SparkSession.builder().appName("DataFrameApp").master("local[2]").getOrCreate()
//    inforReflection(path, spark)
    program(path,spark)
    spark.stop()

  }

  private def program(path: String, spark: SparkSession): Unit ={
    val rdd = spark.sparkContext.textFile(path)
    val infoRDD = rdd.map(_.split(",")).map(line => Row(line(0).toInt, line(1), line(2).toInt))
    val schema = StructType(Array(StructField("id",IntegerType,true),
      StructField("name",StringType,true),
      StructField("age",IntegerType,true)))
    val infodf = spark.createDataFrame(infoRDD,schema)
    infodf.show()
    infodf.filter(infodf.col("age") > 30).show()

    infodf.createOrReplaceTempView("infos")
    spark.sql("select * from infos where age > 30")
  }

  private def inforReflection(path: String, spark: SparkSession) = {
    val rdd = spark.sparkContext.textFile(path)
    //导入隐式转换
    import spark.implicits._

    val infodf = rdd.map(_.split(",")).map(line => Info(line(0).toInt, line(1), line(2).toInt)).toDF()
    infodf.show()
    infodf.filter(infodf.col("age") > 30).show()
    infodf.createOrReplaceTempView("infos")
    spark.sql("select * from infos where age > 30")
  }

  case class Info(id: Int, name: String, age:Int)

  }
