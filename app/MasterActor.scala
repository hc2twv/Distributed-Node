
import java.util.{Calendar, Date}

import akka.actor.{Actor, Address, Props, ReceiveTimeout, Terminated}
import akka.remote.routing.RemoteRouterConfig
import akka.routing.{ActorRefRoutee, RoundRobinPool, RoundRobinRoutingLogic, Router}
import communication._
import kafkaCluster.producers.Producer
import org.joda.time.Days
import org.scalatest.time.Minutes

class MasterActor extends Actor {

  //Producers
  val strProducerCO2 = Producer[String]("CO2")
  val strProducerTemp = Producer[String]("TMP")
  val strProducerPLG = Producer[String]("PLG")
  val strProducerBD = Producer[String]("DB")
  val strProducerCO = Producer[String]("CO")
  val strProducerVLL = Producer[String]("VLL")
  val strProducerAL = Producer[String]("AL")

  var initialDate = Calendar.getInstance().getTime();
  var alarm = 0;
  var finalDate = Calendar.getInstance().getTime();
  //val connMysql = MysqlDB()
  def receive: Receive = {

    case msj: AlarmMessage => signalingActivation(msj)
    case msj: CO2Message =>
      if(alarm == 0) {
        initialDate = Calendar.getInstance().getTime();
      }
      if(diferenciaEnMin(Calendar.getInstance().getTime(), initialDate) < 15) {
        strProducerCO2.sendStream(Stream.continually {
          msj.toString
        }.take(1))
        strProducerBD.sendStream(Stream.continually {
          msj.toString
        }.take(1))
        /*if (msj.value.toFloat > 400) {
          val al = new AlarmMessage(msj.id_node, "O1", "01", "CHK")
          strProducerAL.sendStream(Stream.continually {
            al.toString
          }.take(1))
        }*/
      }
    case msj: COMessage =>
      if(alarm == 0) {
        initialDate = Calendar.getInstance().getTime();
      }
      if(diferenciaEnMin(Calendar.getInstance().getTime(), initialDate) < 15) {
        strProducerCO.sendStream(Stream.continually {
          msj.toString
        }.take(1))
        strProducerBD.sendStream(Stream.continually {
          msj.toString
        }.take(1))
        /*if (msj.value.toFloat > 200) {
          val al = new AlarmMessage(msj.id_node, "O2", "01", "CHK")
          strProducerAL.sendStream(Stream.continually {
            al.toString
          }.take(1))
        }*/
      }
    case msj: PLGMessage =>
      if(alarm == 0) {
        initialDate = Calendar.getInstance().getTime();
      }
      if(diferenciaEnMin(Calendar.getInstance().getTime(), initialDate) < 15) {
        strProducerPLG.sendStream(Stream.continually {
          msj.toString
        }.take(1))
        strProducerBD.sendStream(Stream.continually {
          msj.toString
        }.take(1))
        /*if (msj.value.toFloat > 750) {
          val al = new AlarmMessage(msj.id_node, "O3", "01", "CHK")
          strProducerAL.sendStream(Stream.continually {
            al.toString
          }.take(1))
        }*/
      }
    case msj: TmpMessage =>
      if(alarm == 0) {
        initialDate = Calendar.getInstance().getTime();
      }
      if(diferenciaEnMin(Calendar.getInstance().getTime(), initialDate) < 15) {
        strProducerTemp.sendStream(Stream.continually {
          msj.toString
        }.take(1))
        strProducerBD.sendStream(Stream.continually {
          msj.toString
        }.take(1))
        if (diferenciaEnMin(Calendar.getInstance().getTime(), initialDate) > 2) {
          if (msj.value.toFloat > 35) {
            val al = new AlarmMessage(msj.id_node, "O4", "01", "CHK")
            strProducerAL.sendStream(Stream.continually {
              al.toString
            }.take(1))
          }
        }
        /*if (msj.value.toFloat > 40) {
          val al = new AlarmMessage(msj.id_node, "O4", "01", "CHK")
          strProducerAL.sendStream(Stream.continually {
            al.toString
          }.take(1))
        }*/
      }
    case msj: VLLMessage =>
      if(alarm == 0) {
        initialDate = Calendar.getInstance().getTime();
      }
      if(diferenciaEnMin(Calendar.getInstance().getTime(), initialDate) < 15) {
        strProducerVLL.sendStream(Stream.continually {
          msj.toString
        }.take(1))
        strProducerBD.sendStream(Stream.continually {
          msj.toString
        }.take(1))
        /*if (msj.value.toFloat > 100) {
          val al = new AlarmMessage(msj.id_node, "O5", "01", "CHK")
          strProducerAL.sendStream(Stream.continually {
            al.toString
          }.take(1))
        }*/
      }
    case msj: Message => //Data gets from the sensors
      println(msj)
      initialDate = Calendar.getInstance().getTime()
      alarm = 1
      println("Here")
      if (alarm == 1)
        alarm= 0

    case ReceiveTimeout =>
    // ignore

  }

  def signalingActivation(msj: AlarmMessage): Unit ={
    import scala.io.Source
    for (line <- Source.fromFile("conf/signaling").getLines()){
      val selection = context.actorSelection(line)
      selection ! msj
    }
  }

  def diferenciaEnMin(fechaMayor: Date, fechaMenor: Date): Int = {
    val diferenciaEn_ms = fechaMayor.getTime - fechaMenor.getTime
    val min = diferenciaEn_ms / (1000 * 60)
    //println(min)
    return min.toInt
  }

}
