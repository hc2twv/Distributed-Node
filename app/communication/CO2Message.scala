package communication

/**
  * Created by hc2twv on 15/12/16.
  */
case class CO2Message(id_node:String,id: String, mac: String,tipo:String, value: String, chk: String) extends Message {
  def this() = this("", "", "", "", "", "")

  def this(id: String) = this(id, "", "", "", "", "")

  override def toString: String = id_node + ":" + id + ":" + mac + ":" + tipo + ":" + value + ":" + chk
}
