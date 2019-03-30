package com.logs

import org.apache.spark.sql.{SaveMode, SparkSession}

object SparkStatCleanJobYARN {
  def main(args: Array[String]): Unit = {

    if(args.length != 2){
      println("Usage:SparkStatCleanJobYRAN <inputPath> <outputPath>")
      System.exit(1)
    }
    val Array(inputPath,outputPath) = args



    val spark = SparkSession.builder().getOrCreate()
    val accessRDD = spark.sparkContext.textFile(inputPath)
    //    .filter(x=> x.equals(Row(0)).unary_!) 过滤掉R(0)
    val accessdf = spark.createDataFrame(accessRDD.map(x => AccessConvertUtil.parseLog(x)),
      AccessConvertUtil.struct)
    //    accessdf.printSchema()
    //    accessdf.show()
    //coalesce:表示文件个数，会影响性能
    accessdf.coalesce(1).write.format("parquet").mode(SaveMode.Overwrite).partitionBy("h").save(outputPath)

    spark.stop()
  }
}
