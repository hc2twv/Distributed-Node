package communication

/**
  * Created by hc2twv on 15/12/16.
  */
@SerialVersionUID(100L)
case class AlarmMessage(id: String, status: String, direction: String, chk: String) extends Serializable with Message {
  def this() = this("","","","")
  override def toString: String = id + ":" +status+":"+direction+":"+chk
}

