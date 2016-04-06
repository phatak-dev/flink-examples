package com.madhukaraphatak.flink.streaming.examples.sessionwindow

import com.madhukaraphatak.flink.streaming.examples.Models.Session
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows
import org.apache.flink.streaming.api.windowing.triggers.PurgingTrigger
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow

import scala.util.Try


object SessionWindowExample {

    def main(args: Array[String]) {


      val env = StreamExecutionEnvironment.getExecutionEnvironment

      val source = env.socketTextStream("localhost", 9000)

      //session map

      val values = source.map(value => {
        val columns = value.split(",")
        val endSignal = Try(Some(columns(2))).getOrElse(None)
        Session(columns(0), columns(1).toDouble, endSignal)
      })

      val keyValue = values.keyBy(_.sessionId)

      // create global window

      val sessionWindowStream = keyValue.
        window(GlobalWindows.create()).
        trigger(PurgingTrigger.of(new SessionTrigger[GlobalWindow]()))

      sessionWindowStream.sum("value").print()

      env.execute()


    }
}
