package com.log

import org.apache.spark.sql.SparkSession

import scala.collection.mutable

/**
  * 数据的第一步清晰：抽取我们所需要的列数据
  */
object SparkStatFormatJob2 {
  def main(args: Array[String]): Unit = {
    val path = args(0)
    val out_path = args(1)

    val spark = SparkSession.builder().master("local[2]").appName("SparkStatFormatJob2").getOrCreate()
    val access = spark.sparkContext.textFile(path)
    access.map(line => Splits(line)).filter(line => !line.equals("")).saveAsTextFile(out_path)
    spark.stop()
  }


  private def Splits(line: String): String = {
    val splits = line.split(" ")
    if(splits.length < 14){return ""}
    val ip = splits(0)
    val time = splits(3) + " " + splits(4)
    val G_P = splits(5).replace("\"","")
    val flow = splits(9)
    val Access_side = splits(14).replace("\"","").replace("(","").replace(")","").replace(";","")
    var url = splits(11).replace("\"","").replace(";","")
    val Response_time = splits(splits.length-1)
    val temp = mutable.HashSet("class","video","article","learn","code","wap","course","lesson","ceping")
    val temp_B = mutable.HashSet("compatible","Linux","Android","Windows","Macintosh","iPad","iPhone")

    if(G_P.equals("HEAD") || !temp_B.contains(Access_side))(return "")
    if(url.equals("-") && !ip.equals("10.100.0.1")){url = "http://www.imooc.com"}
    else{
      val sp = url.split("/")
      if(sp.length == 5){
        val judge = temp.contains(sp(3))
        if(judge == false){return ""}
        if(sp(3).equals("course")){url = "http://www.imooc.com/course/0"}
        else if(sp(3).equals("wap")){url = "http://www.imooc.com/wap/0"}
        else if(sp(4).equals("publish")){url = "http://www.imooc.com/article/0"}

        else if(sp(4).contains("?")){url = "http://www.imooc.com/"+sp(3)+"/"+sp(4).split("[?]")(0)}
        else if(sp(4).contains("_")){url = "http://www.imooc.com/"+sp(3)+"/"+sp(4).split("_")(0)}

        else if(sp(4).contains("%")){url = "http://www.imooc.com/code/2039"}
        else if(sp(4).contains("-")){url = "http://www.imooc.com/article/1809"}
      }else{return ""}
    }
    val data = DateUtils.parse(time) + "\t" + url + "\t" + flow + "\t" + ip + "\t" + G_P + "\t" + Access_side + "\t" + Response_time
    return data
  }
}
