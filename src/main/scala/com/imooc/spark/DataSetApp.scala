package com.imooc.spark

import org.apache.spark.sql.SparkSession

object DataSetApp {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("DataFrameApp").master("local[2]").getOrCreate()
    val path = args(0)
    val df = spark.read.option("header","true").option("inferSchema","true").csv(path)

    import spark.implicits._

    val ds = df.as[Sales]
    ds.map(line => line.itemId).show()
    df.show()
    spark.stop()
  }

  case class Sales(transactionId:Int,customerId:Int,itemId:Int,amountPaid:Double)

}
