import java.io.FileReader
import javax.script.ScriptEngineManager

import akka.actor.{Props, Actor, ActorSystem}
import jdk.nashorn.api.scripting.ScriptObjectMirror

class JsActor(jsFile: String) extends Actor {
  val factory = new ScriptEngineManager()
  val engine = factory.getEngineByMimeType("nashorn")

  val reader = new FileReader(jsFile)

  println(engine)

  try {
    engine.eval(reader)
  } catch {
    case e:Exception => {
      e.printStackTrace
      println(s"exception ${e.getStackTrace}")
    }
  }

  val receiveFn: ScriptObjectMirror = engine.get("receive").asInstanceOf[ScriptObjectMirror]

  def receive = {
    case msg =>
      receiveFn.call(null, msg.asInstanceOf[Object])
  }

}

object Main {

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem.create("actor-system")

    val exampleActor = actorSystem.actorOf(Props(classOf[JsActor], "main.js"))
    exampleActor ! "hello"
  }

}
