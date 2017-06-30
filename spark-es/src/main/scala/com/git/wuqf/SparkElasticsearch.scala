package com.git.wuqf

//import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.elasticsearch.spark._
import org.elasticsearch.spark.rdd.EsSpark


/**
  * Created by Administrator on 2017/6/20.
  */
object Save {

  //Logger.getLogger("org").setLevel(Level.DEBUG)

  def main(args: Array[String]): Unit = {
    var sc = initSpark();
    utilSave(sc);
  }

  def utilSave(sc :SparkContext): Unit = {

    val numbers = Map("one" -> 1, "two" -> 2, "three" -> 3)

    val rdd = sc.makeRDD(Seq(numbers))
    EsSpark.saveToEs(rdd, "spark/docs")
  }

  def directSave(sc :SparkContext): Unit = {
    val numbers = Map("one" -> 10, "two" -> 20, "three" -> 30)
    val airports = Map("arrival" -> "Otopeni1", "SFO" -> "San Fran1")
    sc.makeRDD(Seq(numbers, airports)).saveToEs("spark/docs")
  }

  def initSpark(): SparkContext = {
    val conf = new SparkConf().setAppName("spark-es").setMaster("spark://10.10.20.189:7077")
      .set("es.index.auto.create", "true")
      .set("es.nodes", "10.10.20.189")
      .setJars(List("D:\\git\\spark-demo\\spark-es\\target\\spark-es-1.0-SNAPSHOT.jar", "C:\\Users\\Administrator\\.m2\\repository\\org\\elasticsearch\\elasticsearch-hadoop\\5.4.2\\elasticsearch-hadoop-5.4.2.jar"));

    var sc = new SparkContext(conf);
    return sc;
  }

  def queryAll(sc: SparkContext): Unit = {
    val rdd = EsSpark.esRDD(sc,"spark/docs")
    rdd.foreach(line => {
      val key = line._1
      val value = line._2
      println("------------------key:" + key)
      for (tmp <- value) {
        val key1 = tmp._1
        val value1 = tmp._2
        println("------------------key1:" + key1)
        println("------------------value1:" + value1)
      }
    })
  }

}