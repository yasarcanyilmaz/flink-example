package me.ycan.flink.util

import com.typesafe.config.ConfigFactory

trait Config {

  private val config = ConfigFactory.load()

  private val pathConfig = config.getConfig("path")
  val userEventsCsvPath: String = pathConfig.getString("input")
  val outputPath: String = pathConfig.getString("output")

  private val csvConfig = config.getConfig("csv")
  val fieldDelimiter: String = csvConfig.getString("fieldDelimiter")


  val VIEW_EVENT: String = "view"

}
