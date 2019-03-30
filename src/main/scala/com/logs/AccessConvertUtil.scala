package com.logs

import org.apache.spark.sql.Row
import org.apache.spark.sql.types._

/**
  * 日志转换in->out 工具类
  */

object AccessConvertUtil {
  //out格式

  val struct = StructType(Array(
    StructField("url",StringType),
    StructField("cmsType",StringType),
    StructField("cmsId",LongType),
    StructField("traffic",LongType),
    StructField("ip",StringType),
    StructField("province",StringType),
    StructField("city",StringType),
    StructField("time",StringType),
    StructField("h",StringType),
    StructField("G_P",StringType),
    StructField("Access_side",StringType),
    StructField("Response_time",DoubleType)
  ))

  /**
    * in->out
    * @param log in的每一行记录
    */

  def parseLog(log:String):Row ={
    var url = ""
    var traffic = 0l
    var cmsId = 0l
    var ip = ""
    var province = ""
    var city = ""
    var time = ""
    var h = ""
    var G_P = ""
    var Access_side = ""
    var Response_time = 0.0
    var cmsType = ""

    try{
      val splits = log.split("\t")

      url = splits(1)
      traffic = splits(2).toLong
      ip = splits(3)
      val tp_city = IpUtils.getCity(ip).split(" ")
      city = tp_city(1)
      province = tp_city(0)
      time = splits(0)
      h = time.substring(0,13).replace("-","").replace(" ","")
      G_P = splits(4)
      Access_side = splits(5)
      Response_time = splits(6).toDouble

      val cms = url.split("/")
      if(cms.length > 4){
        cmsType = cms(3)
//        if(cms(4).equals("new") || cms(4).equals("details") || cms(4).equals("publish")){cmsId = 0l}
        if(cmsType.equals("lesson") || cmsType.equals("class")){
          if(cms(4).split("\\.").length > 1){cmsId = cms(4).split("\\.")(0).toLong}
          else cmsId = 0l
        }
        else cmsId = cms(4).toLong
      }

    } catch {
      case e:RuntimeException =>  cmsId = 0l
      case e:Exception => {Row(0)}
    }
    Row(url,cmsType,cmsId,traffic,ip,province,city,time,h,G_P,Access_side,Response_time)
  }
}
