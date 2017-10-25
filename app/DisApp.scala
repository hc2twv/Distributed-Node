import akka.actor.{ActorSystem, Address, Props}
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._
/**
  * Created by hc2twv on 15/12/16.
  */
object DisApp extends App{


    val system = ActorSystem("Sys", ConfigFactory.load("application"))
    //Activamos el master
    val master = system.actorOf(Props(classOf[MasterActor]), name="master")
    println("This is a master: " + master)
    //Activate the listener the data of the sensors
    val listendata = system.actorOf(Props(classOf[ListenerActorPort], master),name="listendata")


    //Consumers
    val BasicConsumerAl = kafkaCluster.consumers.ConsKafka("192.168.0.161","AL",master,"1","6000")
    BasicConsumerAl.subscribe("AL")
    BasicConsumerAl.read("AL")



    //println(BasicConsumerAl.read("AL"))
    //new Thread(BasicConsumerAl).start()

    //import system.dispatcher
    //system.scheduler.scheduleOnce(1.second, master, new AlarmMessage("2","3","3","3"))




}
