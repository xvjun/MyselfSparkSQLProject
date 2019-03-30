package com.imooc
import org.apache.spark.sql.SparkSession
object test extends App {

  val spark = SparkSession.builder().appName("DataFrameApp").master("local[2]").getOrCreate()


  var hbasetable = spark.read.format("com.Hbase").options(Map(
    "sparksql_table_schema" -> "(row_key string, c1 string, c2 string, c3 string)",
    "hbase_table_name" -> "lxw1234",
    "hbase_table_schema" -> "(:key , f1:c2 , f2:c2 , f3:c3 )"
  )).load()

  //sparksql_table_schema参数为sparksql中表的定义
  //hbase_table_name参数为HBase中表名
  //hbase_table_schema参数为HBase表中需要映射到SparkSQL表中的列族和列，这里映射过//去的字段要和sparksql_table_schema中定义的一致，包括顺序。


  hbasetable.printSchema()


  hbasetable.registerTempTable("lxw1234")


  spark.sql("SELECT * from lxw1234").collect


  spark.sql("SELECT row_key,concat(c1,'|',c2,'|',c3) from lxw1234").collect


}
