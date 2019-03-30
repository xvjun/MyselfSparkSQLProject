package com.logs


import java.util

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result
import org.apache.hadoop.hbase.client.{Put, ResultScanner}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.mutable.ArrayBuffer

object ToHbase {

  def main(args: Array[String]): Unit = {
    all_information ("spark_Clean_output")

//    val hbase = new HbaseUtils();
//    hbase.putData("all_information","rk2去","data","traffics","我是")
//
////    val hbase = new HbaseUtils();
//    val scanner =  hbase.getResultScann("all_information")
//    var result = scanner.next()
//    //数据不为空时输出数据
//    while (result != null) {
//      println(result.toString)
//      println(Bytes.toString(result.getRow))
//      println(Bytes.toString(result.getValue(Bytes.toBytes("data"), Bytes.toBytes("traffics"))))
////      println(s"rowkey:${Bytes.toString(result.getRow)},列簇:data:,value:${Bytes.toString(result.getValue(Bytes.toBytes("data"), Bytes.toBytes("traffics")))}")
//      result = scanner.next()
//    }
//    //通过scan取完数据后，记得要关闭ResultScanner，否则RegionServer可能会出现问题(对应的Server资源无法释放)
//    scanner.close()
}

  def all_information(path:String): Unit ={
    val spark = SparkSession.builder().appName("imooc_information").
      config("spark.sql.sources.partitionColumnTypeInference.enabled","false").
      master("local[2]").getOrCreate()
    val accessdf = spark.read.format("parquet").load(path)
    accessdf.createOrReplaceTempView("access_logs")

    val imoocNDF = spark.sql("select ip,cmsType,cmsId,province,city,h,url,time,G_P,Access_side,sum(traffic) as traffics,avg(Response_time) as Response_times from access_logs group by ip,cmsType,cmsId,province,city,h,url,time,G_P,Access_side")
//            imoocNDF.show(false)
    //    println(imoocNDF.count())
    add(imoocNDF,"all_information")

  }

  def add(videoAccessTopNDF: DataFrame,TableName:String ): Unit ={
    try {
      var hbase = new HbaseUtils();
      videoAccessTopNDF.foreachPartition(partitionOfRecords => {
//        sum=sum+1

//        if(sum==1){

          val puts = new util.ArrayList[Put]
          partitionOfRecords.foreach(info => {
            var rowkey = ""
            var value1 = 0l
            var value2 = 0.0
            var i = null
            for (i <- 0 until info.length) {
              if (i >= 10) {
                if (i == 10) {
                  value1 = info.getAs[Long](i)
                }
                if(i==11){
                  value2 = info.getAs[Double](i)
                }
              } else {
                rowkey = rowkey + "_" + info.getAs[String](i)
              }
            }

            val put: Put = new Put(Bytes.toBytes(rowkey))
            put.addColumn(Bytes.toBytes("data"), Bytes.toBytes("traffics"), Bytes.toBytes(value1))
            put.addColumn(Bytes.toBytes("data"), Bytes.toBytes("Response_times"), Bytes.toBytes(value2))
            puts.add(put)
          })

          hbase.putDatas("all_information", puts);
//        }

      })
    }catch {
      case e: Exception => e.printStackTrace()
    }
  }
}
