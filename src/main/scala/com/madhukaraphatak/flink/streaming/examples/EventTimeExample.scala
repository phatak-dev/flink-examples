package com.madhukaraphatak.flink.streaming.examples

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time


object EventTimeExample {

  case class Stock(time:Long, symbol:String,value:Double)

  def main(args: Array[String]) {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val source = env.socketTextStream("localhost",9000)
    val parsedStream = source.map(value => {
      val columns = value.split(",")
      Stock(columns(0).toLong, columns(1),columns(2).toDouble)
    })

    val timedValue = parsedStream.assignAscendingTimestamps(_.time)

    val keyedStream = timedValue.keyBy(_.symbol)

    val timeWindow = keyedStream.timeWindow(Time.seconds(10)).max("value").name("timedwindow")

    timeWindow.print.name("print sink")

    env.execute()

  }

}
