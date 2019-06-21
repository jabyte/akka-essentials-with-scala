import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object CounterStateless extends App {

  object Counter {

    sealed trait Operation

    case object Increment extends Operation

    case object Decrement extends Operation

    case object Print extends Operation

  }

  class Counter extends Actor {

    import Counter._

    override def receive: Receive = operationHandler(0)

    def operationHandler(currentValue: Int): Receive = {
      case Increment =>
        context.become(operationHandler(currentValue + 1))
        sender ! currentValue
      case Decrement =>
        context.become(operationHandler(currentValue - 1))
        sender ! currentValue
      case Print => sender ! currentValue
    }
  }

  object Jabyte {

    import Counter.Operation

    case class FireUp(counter: ActorRef, operation: Operation)

  }

  class Jabyte extends Actor {

    import Jabyte._

    override def receive: Receive = {
      case FireUp(counter, operation) => counter ! operation
      case result => println(result)
    }
  }

  val actorSystem = ActorSystem("counterActorSystem")
  val counter = actorSystem.actorOf(Props[Counter], "counterActor")
  val jabyte = actorSystem.actorOf(Props[Jabyte], "jabyteActor")

  import Jabyte._
  import Counter._

  jabyte ! FireUp(counter, Increment)
  jabyte ! FireUp(counter, Increment)
  jabyte ! FireUp(counter, Increment)
  jabyte ! FireUp(counter, Increment)
  jabyte ! FireUp(counter, Print)
  jabyte ! FireUp(counter, Decrement)
  jabyte ! FireUp(counter, Print)

}
