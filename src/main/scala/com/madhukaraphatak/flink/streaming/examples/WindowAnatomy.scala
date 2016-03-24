package com.madhukaraphatak.flink.streaming.examples

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows
import org.apache.flink.streaming.api.windowing.triggers.{CountTrigger, PurgingTrigger}
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow


object WindowAnatomy {
  def main(args: Array[String]) {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val source = env.socketTextStream("localhost",9000)

    val values = source.flatMap(value => value.split("\\s+")).map(value => (value,1))

    val keyValue = values.keyBy(0)

    // define the count window without purge

    val tumblingWindowStreamWithoutPurge = keyValue.window(GlobalWindows.create()).
      trigger(CountTrigger.of(2))

    tumblingWindowStreamWithoutPurge.sum({ println("without purge") ; 1}).print()

    // define the count with purge

    val tumblingWindowStreamWithPurge = keyValue.window(GlobalWindows.create()).
      trigger(PurgingTrigger.of(CountTrigger.of[GlobalWindow](2)))

    tumblingWindowStreamWithoutPurge.sum(1).print()

    tumblingWindowStreamWithPurge.sum(1).print()

    env.execute()

  }
}
