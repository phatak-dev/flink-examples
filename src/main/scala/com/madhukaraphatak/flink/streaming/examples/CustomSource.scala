package com.madhukaraphatak.flink.streaming.examples

import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext
import org.apache.flink.streaming.api.scala._

import scala.util.Random

object CustomSource {

  def generateRandomStringSource(out:SourceContext[String]) = {
     val lines = Array("how are you","you are how", " i am fine")
    while (true) {
      val index = Random.nextInt(3)
      Thread.sleep(200)
      out.collect(lines(index))
    }
  }


  def main(args: Array[String]) {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val customSource = env.addSource(generateRandomStringSource _)

    customSource.print()

    env.execute()


  }

}
