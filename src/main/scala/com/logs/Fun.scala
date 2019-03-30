package com.logs

import org.apache.spark.sql.{DataFrame}
import org.apache.spark.sql.functions.count
import org.apache.spark.sql.SparkSession
import scala.collection.mutable.ArrayBuffer

object Fun {

  def main(args: Array[String]): Unit = {
    all_information ("spark_Clean_output")
  }

  def all_information(path:String): Unit ={
    val spark = SparkSession.builder().appName("imooc_information").
      config("spark.sql.sources.partitionColumnTypeInference.enabled","false").
      master("local[2]").getOrCreate()
    val accessdf = spark.read.format("parquet").load(path)
    accessdf.createOrReplaceTempView("access_logs")

    val imoocNDF = spark.sql("select ip,cmsType,cmsId,province,city,h,url,time,G_P,Access_side,sum(traffic) as traffics,avg(Response_time) as Response_times from access_logs group by ip,cmsType,cmsId,province,city,h,url,time,G_P,Access_side")
        imoocNDF.show()
//    println(imoocNDF.count())
//    add(imoocNDF,"insert into all_informationed values (?,?,?,?,?,?,?,?,?,?,?,?)")

//    val spark = SparkSession.builder().appName("imooc_information").
//      config("spark.sql.sources.partitionColumnTypeInference.enabled","false").
//      master("local[2]").getOrCreate()
//    val accessdf = spark.read.format("parquet").load(path)
//    accessdf.createOrReplaceTempView("access_logs")
//
//    val imoocNDF = spark.sql("select ip,url,time,G_P,Access_side,sum(traffic) as traffics,avg(Response_time) as Response_times from access_logs group by ip,url,time,G_P,Access_side order by traffics desc")
//    //    imoocNDF.show()
//    println(imoocNDF.count())
//    add(imoocNDF,"insert into ip_informationed values (?,?,?,?,?,?,?)")
  }

  def information(path:String): Unit ={
    val spark = SparkSession.builder().appName("imooc_information").
      config("spark.sql.sources.partitionColumnTypeInference.enabled","false").
      master("local[2]").getOrCreate()
    val accessdf = spark.read.format("parquet").load(path)
    accessdf.createOrReplaceTempView("access_logs")

    val imoocNDF = spark.sql("select cmsType,cmsId,province,city,h,Access_side,count(1) as times,sum(traffic) as all_traffic,avg(Response_time) as avg_Response_time from access_logs group by cmsType,cmsId,province,city,h,Access_side order by times desc,all_traffic desc,avg_Response_time desc")
//    imoocNDF.show()
    add(imoocNDF,"insert into information values (?,?,?,?,?,?,?,?,?)")
  }

  def ip_information(path:String): Unit ={
    val spark = SparkSession.builder().appName("imooc_information").
      config("spark.sql.sources.partitionColumnTypeInference.enabled","false").
      master("local[2]").getOrCreate()
    val accessdf = spark.read.format("parquet").load(path)
    accessdf.createOrReplaceTempView("access_logs")

    val imoocNDF = spark.sql("select ip,url,time,G_P,Access_side,sum(traffic) as traffics,avg(Response_time) as Response_times from access_logs group by ip,url,time,G_P,Access_side order by traffics desc")
//    imoocNDF.show()
    println(imoocNDF.count())
    add(imoocNDF,"insert into ip_information values (?,?,?,?,?,?,?)")
  }















  def class_top(path:String): Unit ={
    val spark = SparkSession.builder().appName("imooc_information").
      config("spark.sql.sources.partitionColumnTypeInference.enabled","false").
      master("local[2]").getOrCreate()
    val accessdf = spark.read.format("parquet").load(path)


    accessdf.createOrReplaceTempView("access_logs")

//    val imoocNDF = spark.sql("select cmsType,cmsId,province,city,count(1) " +
//      "as times,sum(traffic) as all_traffic from access_logs group by cmsType," +
//      "cmsId,province,city order by times desc,all_traffic desc")
    val imoocNDF = spark.sql("select * from access_logs where cmsType=\"\"")
    imoocNDF.show()
//    add(imoocNDF,"insert into class_Top values (?,?,?,?,?,?)")
  }

  def localhost_time_Top(path:String): Unit ={
    val spark = SparkSession.builder().appName("imooc_information").
      config("spark.sql.sources.partitionColumnTypeInference.enabled","false").
      master("local[2]").getOrCreate()
    val accessdf = spark.read.format("parquet").load(path)


    accessdf.createOrReplaceTempView("access_logs")
    val imoocNDF = spark.sql("select province,city,h,Access_side,G_P," +
      "count(1) as times,sum(traffic) as all_traffic,avg(Response_time) as agv_reponse_time " +
      "from access_logs group by province,city,h,Access_side,G_P order by times desc,all_traffic desc")
//    imoocNDF.show()
    add(imoocNDF,"insert into localhost_time_Top values (?,?,?,?,?,?,?,?)")
  }


  def imooc_information(path:String): Unit ={
    val spark = SparkSession.builder().appName("imooc_information").
      config("spark.sql.sources.partitionColumnTypeInference.enabled","false").
      master("local[2]").getOrCreate()
    val accessdf = spark.read.format("parquet").load(path)

    accessdf.createOrReplaceTempView("access_logs")

    val imoocNDF = spark.sql("select distinct cmsType,cmsId from access_logs")
//    System.out.println(imoocNDF.count())

    add(imoocNDF,"insert into temp_imooc values(?,?)")


    spark.stop()

  }

  def TopNStatFun(path:String): Unit ={

    val spark = SparkSession.builder().appName("TopNStatJob").
      config("spark.sql.sources.partitionColumnTypeInference.enabled","false").
      master("local[2]").getOrCreate()
    val accessdf = spark.read.format("parquet").load(path)

    accessdf.createOrReplaceTempView("access_logs")
    val videoAccessTopNDF = spark.sql("select cmsId,count(1) as times from access_logs" +
      " where h='2016111009' and cmsType='video' group by h,cmsId order by times desc")
    val list = add(videoAccessTopNDF,"")
    spark.stop()
  }

  /**
    * 统计视频的TopN
    * @param spark
    * @param accessdf
    */

  def dataframe_VideoAccessTopNStat(spark:SparkSession, accessdf:DataFrame): Unit ={
    import spark.implicits._
    val videoAccessTopNDF = accessdf.filter($"h" === "2016111009" && $"cmsType" === "video")
      .groupBy("h","cmsId").agg(count("cmsId").as("times")).orderBy($"times".desc)
  }

  private def add(videoAccessTopNDF: DataFrame,sql:String): Unit = {
    val list = new ArrayBuffer[ArrayBuffer[Object]]

//    var i=0
    try {
      videoAccessTopNDF.foreachPartition(partitionOfRecords => {
        partitionOfRecords.foreach(info => {
//          i=i+1
//          if(i < 534732){}
//          else{
            val tplist = new ArrayBuffer[Object]
            for(i <- 0 until info.length){
              tplist.append(info.getAs[Object](i))}
            list.append(tplist)
//          }

        })
        StatDAO.carryOutIDU(sql,list)
      })
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

}
