package com.imooc.spark

import java.sql.DriverManager
import java.sql.ResultSetMetaData

object SparkSQLThriftServerApp {



  /**
    * 通过jdbc编程
    *
    * @param args
    */

  def main(args: Array[String]): Unit = {

    Class.forName("org.apache.hive.jdbc.HiveDriver")

    val conn = DriverManager.getConnection("jdbc:hive2://192.168.13.137:10000","root","234520")
    val pstmt = conn.prepareStatement("select empno,ename,sal from emp")
    val rs = pstmt.executeQuery()

//方法1

    val rsmd = rs.getMetaData
    val count = rsmd.getColumnCount
    val store = new Array[String](count)
    var i = 0
    for( i <- 0 until store.length ) {
      store(i) = rsmd.getColumnName(i + 1)
    }
    for( i <- 0 until store.length ) {
      System.out.printf("\t%s", store(i))
    }
    System.out.println()
    while (rs.next()) {
      var i = 0
      while (i < store.length) {
        System.out.printf("\t%s", rs.getString(store(i)))
        i = i+1
      }
      System.out.println()
    }

//方法2

    while(rs.next()){
      println("empno:" + rs.getInt("empno") +
      " , ename:" + rs.getString("ename") +
      " , sal:" + rs.getDouble("sal"))
    }

    rs.close()
    pstmt.close()
    conn.close()

  }

}
