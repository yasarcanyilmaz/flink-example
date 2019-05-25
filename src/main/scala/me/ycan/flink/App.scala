package me.ycan.flink

import me.ycan.flink.model._
import me.ycan.flink.util.Config
import me.ycan.flink.util.Converters._
import me.ycan.flink.util.Helper._
import me.ycan.flink.util.ImplicitOps._
import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.slf4j.LoggerFactory

object App extends Config {

  def main(args: Array[String]): Unit = {

    val logger = LoggerFactory.getLogger(getClass.getSimpleName)

    val flink: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    logger.info("Reading data from input...")
    val userEvents = flink.readTextFile(userEventsCsvPath)
      .map(_.convertTo[UserEvent])
      .filter(_.isSuccess)
      .map(_.get)

    logger.info("Creating Unique Product View counts by ProductId")
    distinctCounter(userEvents.filter(_.eventName == "view"), ProductId)
      .writeToCSVWithKnowParameters(s"$outputPath/q1/")

    logger.info("Unique Event counts")
    distinctCounter(userEvents, EventName)
      .writeToCSVWithKnowParameters(s"$outputPath/q2/")

    logger.info("Top 5 Users who fulfilled all the events (view,add,remove,click)")
    distinctCounter(userEvents, UserId)
      .setParallelism(1)
      .sortPartition(_.value, Order.DESCENDING)
      .first(5)
      .map(_.key)
      .writeToCSVWithKnowParameters(s"$outputPath/q3/")

    logger.info("All events of #UserId : 47")
    userEvents
      .filter(_.userId == 47)
      .map(r => CountResult(r.eventName, r.productId))
      .writeToCSVWithKnowParameters(s"$outputPath/q4/")

    logger.info("Product Views of #UserId : 47")
    userEvents
      .filter(e => e.userId == 47 && e.eventName == VIEW_EVENT)
      .map(_.productId)
      .distinct()
      .writeToCSVWithKnowParameters(s"$outputPath/q5/")

    flink.execute(getClass.getSimpleName)

  }

}
