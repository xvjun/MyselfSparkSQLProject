package com.logs

import java.sql.{Connection, PreparedStatement, ResultSet}

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
  * 个维度统计的DAO操作
  */

object StatDAO {

  def carryOutIDU(sql:String, list:ArrayBuffer[ArrayBuffer[Object]]): Unit ={
    var connection:Connection = null
    var pstmt:PreparedStatement = null

    try{
      connection = MySQLUtils.getConnection()
      connection.setAutoCommit(false) //禁止自动提交
//      var sql = "insert into h_video_access_topn_stat(h,cms_id,times) values (?,?,?)"
      pstmt = connection.prepareStatement(sql)

      for(eles <- list){
        var i = 0
        for(ele <- eles){
          i = i+1
          pstmt.setObject(i,ele)
        }
        pstmt.addBatch()
      }

      pstmt.executeBatch() //批量处理
      connection.commit()

    } catch {
      case e: Exception => null
    } finally {
      MySQLUtils.release(connection,pstmt)
    }
  }

  def carryOutQuery(sql:String, list:ArrayBuffer[Object]) :ResultSet = {

    var connection:Connection = null
    var pstmt:PreparedStatement = null
    var rs:ResultSet = null

    try{
      connection = MySQLUtils.getConnection()
      pstmt = connection.prepareStatement(sql)
      var i = 0
      for(ele <- list){
          i = i+1
          pstmt.setObject(i,ele)
        }
      rs = pstmt.executeQuery()
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      MySQLUtils.release(connection,pstmt)
    }
    return rs
  }

}
