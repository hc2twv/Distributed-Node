package kafkaCluster.producers
/*
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util.Random

object ProducerStreamData {
  def main(args: Array[String]): Unit = {
    val strProducerTemp = Producer[String]("TMP")
    val strProducerCO2 = Producer[String]("CO2")
    val strProducerGlp = Producer[String]("PLG")
    val strProducerBD = Producer[String]("BD")
    val strProducerAlarm = Producer[String]("AL")

    lazy val f = Future { Thread.sleep(30000); true } //Esperar valores para transmitir

    while (true){

      val msgStreamTmp = Stream.continually{
        //Random.alphanumeric.take(10).mkString
        //ID qwsqws : MAC Xbee : Value : Cheksum
        Random.nextInt(100).toString()+":"+Random.alphanumeric.take(10).mkString+":"+Random.nextInt(100).toString()+":"+"0000"
      }.take(1)



      //for (ln <- io.Source.stdin.getLines) strProducer.send(ln)
      strProducerTemp.sendStream(msgStreamTmp) //Producer Tmp
      //strProducerCO2.sendStream(msgStreamCo2) //Producer Co2
      //strProducerGlp.sendStream(msgStreamGlp) //Producer Glp

      //Envio a la base
      strProducerBD.sendStream(msgStreamTmp) //Producer Tmp
      //strProducerBD.sendStream(msgStreamCo2) //Producer Co2
      //strProducerBD.sendStream(msgStreamGlp) //Producer Glp

      Await.result(f, 30 seconds)
    }
  }
}*/
