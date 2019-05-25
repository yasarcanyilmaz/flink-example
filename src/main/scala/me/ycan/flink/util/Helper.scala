package me.ycan.flink.util

import me.ycan.flink.model._
import me.ycan.flink.model.types._
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.scala.{DataSet, _}

import scala.reflect.ClassTag
import scala.util.Try


object Helper extends Config {

  def userEventMapper(line: String): Try[UserEvent] = Try {

    val fields = line.split(fieldDelimiter)

    val maybeDate = fields(0)
    val maybeProductId = fields(1)
    val maybeEventName = fields(2)
    val maybeUserId = fields(3)

    UserEvent(maybeDate.toLong, maybeProductId.toLong, maybeEventName, maybeUserId.toLong)
  }

  private def calculateCounts[T: ClassTag : TypeInformation, K: TypeInformation](ds: DataSet[UserEvent],
                                                                                 mapper: UserEvent => K,
                                                                                 reducer: Iterator[UserEvent] => T): DataSet[T] = {
    ds.groupBy(mapper)
      .reduceGroup(reducer)
  }

  private val productViewMapper = (user: UserEvent) => user.productId
  private val productViewReducer = (users: Iterator[UserEvent]) => {
    val distinctProducts = users.toList.distinct
    CountResult(productViewMapper(distinctProducts.head).toString, distinctProducts.size)
  }

  private val eventCountMapper = (user: UserEvent) => user.eventName
  private val eventCountReducer = (users : Iterator[UserEvent]) => {
    val distinctEventViews = users.toList.distinct
    CountResult(eventCountMapper(distinctEventViews.head), distinctEventViews.size)

  }


  def distinctCounter(ds: DataSet[UserEvent], fieldType: DistinctCountable): DataSet[CountResult] = {
    fieldType match {
      case ProductId => calculateCounts[CountResult, ProductIdType](ds, productViewMapper, productViewReducer)
      case EventName => calculateCounts[CountResult, EventNameType](ds, eventCountMapper, eventCountReducer)
      case UserId    => calculateCounts[Option[CountResult], UserIdType](ds, topNUserMapper, topNUserReducer).filter(_.isDefined).map(_.get)

    }
  }


  private val topNUserMapper = (user: UserEvent) => user.userId
  private val topNUserReducer = (users : Iterator[UserEvent]) => {
    val eventList = users.toList
    val distinctEventTypeSize = eventList.map(_.eventName).distinct.size

    if(distinctEventTypeSize == 4) {
      val counts = eventList.size
      val userId = eventList.head.userId.toString
      Some(CountResult(userId, counts))
    } else None

  }

}
