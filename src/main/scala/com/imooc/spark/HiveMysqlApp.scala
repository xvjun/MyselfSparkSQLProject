package com.imooc.spark

import org.apache.spark.sql.SparkSession

/**
  * 使用Hive和mysql联合使用
  */
object HiveMysqlApp {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataFrameApp").master("local[2]").getOrCreate()

    val Hivedf = spark.table("emp")

    val Mysqldf = spark.read.format("jdbc").option("url","jdbc:mysql://192.168.13.137:3306").option("dbtable", "testSpark.dept").option("user", "root").option("password", "root").load()

    val rsdf = Hivedf.join(Mysqldf,Hivedf.col("deptno") === Mysqldf.col("deptno"))

    rsdf.show()
  }
}
