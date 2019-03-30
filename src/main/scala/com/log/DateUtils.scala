package com.log

import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import org.apache.commons.lang3.time.FastDateFormat

/**
  * 日期时间解析
  */

object DateUtils {
  /**
    * 原格式：[10/Nov/2016:00:01:02 +0800]
    * 转换后格式：2016-11-10 00:01:02
    */

  val YYYYMMDDHHSS_TIME_FORMAT = FastDateFormat.getInstance("dd/MMM/yyyy:HH:mm:ss Z",
    Locale.ENGLISH)
  val TARGET_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")

  //时间转换
  def parse(time:String) ={
    TARGET_FORMAT.format(new Date(getTime(time)))
  }

  def getTime(time:String)={
    try{
      YYYYMMDDHHSS_TIME_FORMAT.parse(time.substring(time.indexOf("[") + 1,
        time.lastIndexOf("]"))).getTime
    }catch{
      case e:Exception => {
        e.printStackTrace()
        0l
      }
    }
  }

  def main(args: Array[String]): Unit = {
    println(parse("[10/Nov/2016:00:01:02 +0800]"))
  }

}
