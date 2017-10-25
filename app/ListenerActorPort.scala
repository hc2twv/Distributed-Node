import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import akka.io.Udp.{Bind, Bound, Received, Unbind}
import akka.io.{IO, Udp}
import com.typesafe.config.ConfigFactory
import communication._

/**
  * Created by hc2twv on 15/12/16.
  */
class ListenerActorPort(nextActor: ActorRef) extends Actor {
  import context.system
  IO(Udp) ! Udp.Bind(self, new InetSocketAddress("127.0.0.1", 25555)) //Receptor de todos los paquetes...
  var cntTMP =0
  var cntCO2 =0
  var cntPLG = 0
  var cntCO = 0
  var cntVLL = 0
  def receive = {
    case Udp.Bound(local) =>
      context.become(ready(sender()))
  }

  def ready(socket: ActorRef): Receive = {
    case Udp.Received(data, remote) =>
      //val processed = // parse data etc., e.g. using PipelineStage
      //  socket ! Udp.Send(data, remote) // example server echoes back
      //nextActor ! processed
      nextActor ! sendMessageValuetoKafka(data.decodeString("US-ASCII"))
    case Udp.Unbind  => socket ! Udp.Unbind
    case Udp.Unbound => context.stop(self)
  }

  def sendMessageValuetoKafka(msj: String): Message ={
    val dat = msj.split(":")//ID:ID_NODE:MAC:TIPO(T(TEMP)):VALOR:CHK
    println("Sensor: "+msj)
    val typeMessage = dat(3) match {
      case "T" =>
        cntTMP = cntTMP+1
        new TmpMessage(dat(0),dat(1),dat(2),dat(3),dat(4),dat(5))
      case "V" =>
        cntVLL = cntVLL +1
        new VLLMessage(dat(0),dat(1),dat(2),dat(3),dat(4),dat(5))
      case "P" =>
        cntPLG = cntPLG+1
        new PLGMessage(dat(0),dat(1),dat(2),dat(3),dat(4),dat(5))
      case "C" =>
        cntCO = cntCO +1
        new COMessage(dat(0),dat(1),dat(2),dat(3),dat(4),dat(5))
      case "X" =>
        cntCO2 = cntCO2+1
        new CO2Message(dat(0),dat(1),dat(2),dat(3),dat(4),dat(5))
      case _ => new Message {msj}
    }
    println("Contadores: "+cntTMP+" "+cntVLL+" "+cntPLG+" "+cntCO+" "+cntCO2)
    return typeMessage
  }
}

