package com.imooc.spark

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 启动时要把mysql的驱动加到脚本参数里面去
  */

object HiveContextApp {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
    val sc = new SparkContext(sparkConf)

//    sparkConf.setAppName("HiveContextApp").setMaster("local[2]")

    val hiveContext = new HiveContext(sc)

    hiveContext.table("emp").show
    sc.stop()
  }

}
