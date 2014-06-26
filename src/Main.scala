import java.io.FileReader
import javax.script.ScriptEngineManager

import akka.actor.{Props, Actor, ActorSystem}
import jdk.nashorn.api.scripting.ScriptObjectMirror

class ExampleActor extends Actor {
  def receive = {
    case _ => {
      println("Received message")
    }
  }
}

object Main {

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem.create("actor-system")

    val exampleActor = actorSystem.actorOf(Props[ExampleActor])
    exampleActor ! "hello"

    val factory = new ScriptEngineManager()
    // create a Nashorn script engine
    val engine = factory.getEngineByName("nashorn")
    // evaluate JavaScript statement
    engine.eval(new FileReader("main.js"))

    val som: ScriptObjectMirror = engine.get("fnToCallLater").asInstanceOf[ScriptObjectMirror]

    println(som.call(null))
  }

}
