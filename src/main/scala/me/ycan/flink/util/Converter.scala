package me.ycan.flink.util

import me.ycan.flink.model.UserEvent
import me.ycan.flink.util.Helper.userEventMapper

import scala.util.Try

trait Converter[T] {
  def convert(v: String): Try[T]
}

object Converters {
  implicit val userLoader: Converter[UserEvent] = (v: String) => userEventMapper(v)
}
