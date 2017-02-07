
import akka.actor.{Actor, Address, Props, ReceiveTimeout, Terminated}
import akka.remote.routing.RemoteRouterConfig
import akka.routing.{ActorRefRoutee, RoundRobinPool, RoundRobinRoutingLogic, Router}
import communication._
import kafkaCluster.producers.Producer

class MasterActor extends Actor {

  //Producers
  val strProducerCO2 = Producer[String]("CO2")
  val strProducerTemp = Producer[String]("TMP")
  val strProducerPLG = Producer[String]("PLG")
  val strProducerBD = Producer[String]("DB")
  val strProducerCO = Producer[String]("CO")
  val strProducerVLL = Producer[String]("VLL")
  val strProducerAL = Producer[String]("AL")
  //val connMysql = MysqlDB()
  def receive: Receive = {

    case msj: AlarmMessage => signalingActivation(msj)
    case msj: CO2Message =>
      strProducerCO2.sendStream(Stream.continually{msj.toString}.take(1))
      strProducerBD.sendStream(Stream.continually{msj.toString}.take(1))
      if (msj.value.toInt >  400) {
        val al = new AlarmMessage(msj.id_node,"O1","01","CHK")
        strProducerAL.sendStream(Stream.continually {
          al.toString
        }.take(1))
      }
    case msj: COMessage =>
      strProducerCO.sendStream(Stream.continually{msj.toString}.take(1))
      strProducerBD.sendStream(Stream.continually{msj.toString}.take(1))
      if (msj.value.toInt >  200) {
        val al = new AlarmMessage(msj.id_node,"O1","01","CHK")
        strProducerAL.sendStream(Stream.continually {
          al.toString
        }.take(1))
      }
    case msj: PLGMessage =>
      strProducerPLG.sendStream(Stream.continually{msj.toString}.take(1))
      strProducerBD.sendStream(Stream.continually{msj.toString}.take(1))
      if (msj.value.toInt >  750) {
        val al = new AlarmMessage(msj.id_node,"O1","01","CHK")
        strProducerAL.sendStream(Stream.continually {
          al.toString
        }.take(1))
      }
    case msj: TmpMessage =>
      strProducerTemp.sendStream(Stream.continually{msj.toString}.take(1))
      strProducerBD.sendStream(Stream.continually{msj.toString}.take(1))
      if (msj.value.toInt >  40) {
        val al = new AlarmMessage(msj.id_node,"O1","01","CHK")
        strProducerAL.sendStream(Stream.continually {
          al.toString
        }.take(1))
      }
    case msj: VLLMessage =>
      strProducerVLL.sendStream(Stream.continually{msj.toString}.take(1))
      strProducerBD.sendStream(Stream.continually{msj.toString}.take(1))
      if (msj.value.toInt >  100) {
        val al = new AlarmMessage(msj.id_node,"O1","01","CHK")
        strProducerAL.sendStream(Stream.continually {
          al.toString
        }.take(1))
      }
    case msj: String => //Data gets from the sensors
      println(msj)

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

}
