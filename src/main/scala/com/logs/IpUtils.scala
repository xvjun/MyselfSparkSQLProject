package com.logs

import com.ggstar.util.ip.IpHelper

/**
  * ip地址工具类
  */

object IpUtils {

  def getCity(ip:String)={
    IpHelper.findRegionByIp(ip)
  }

  def main(args: Array[String]): Unit = {
    println(getCity("125.120.150.55"))
  }
}
