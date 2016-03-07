package com.madhukaraphatak.flink.streaming.examples

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time


object WindowedStreamingWordCount {

  def main(args: Array[String]) {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    // create a stream using socket

    val socketStream = env.socketTextStream("localhost",9000)

    // implement word count

    val wordsStream = socketStream.flatMap(value => value.split("\\s+")).map(value => (value,1))

    val keyValuePair = wordsStream.keyBy(0).timeWindow(Time.seconds(15))

    val countStream = keyValuePair.sum(1)

    countStream.print()

    env.execute()

  }

}
