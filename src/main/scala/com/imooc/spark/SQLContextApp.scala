package com.imooc.spark

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object SQLContextApp {
  def main(args: Array[String]): Unit = {
    val path = args(0)
    val sparkConf = new SparkConf()
    sparkConf.setAppName("SQLContextApp").setMaster("local[2]")
    val sc = new SparkContext(sparkConf)
    val sqlContext = new SQLContext(sc)
    val people = sqlContext.read.format("json").load(path)
    people.printSchema()
    people.show()
    sc.stop()
  }

}
