package me.ycan.flink.model

case class UserEvent(date: Long, productId: Long, eventName: String, userId: Long)

case class CountResult(key: String, value: Long){
  override def toString: String = s"$key|$value"
}

sealed trait DistinctCountable

case object ProductId extends DistinctCountable
case object EventName extends DistinctCountable
case object UserId extends DistinctCountable


