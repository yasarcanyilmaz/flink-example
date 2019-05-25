package me.ycan.flink.util

import org.apache.flink.api.java.operators.DataSink
import org.apache.flink.api.scala.DataSet
import org.apache.flink.core.fs.FileSystem.WriteMode

import scala.util.Try

object ImplicitOps {

  implicit class StringToEventOps(s: String){
    def convertTo[T](implicit converter: Converter[T]): Try[T] = {
      converter.convert(s)
    }
  }

  implicit class DataSetOps[T](s: DataSet[T]) extends Config {
    def writeToCSVWithKnowParameters(path: String): DataSink[_] = {
      s.setParallelism(1)
        .writeAsText(path, WriteMode.OVERWRITE)
    }
  }

}
