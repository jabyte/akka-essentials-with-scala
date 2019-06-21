import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object SleepyRunner extends App {

  object Sleepy {

    sealed trait Action

    case object Sleep extends Action

    case object Wakeup extends Action

  }

  class Sleepy extends Actor {

    import Sleepy._

    override def receive: Receive = sleep

    def sleep: Receive = {
      case Sleep =>
      case Wakeup => context.become(wakeup)
    }

    def wakeup: Receive = {
      case Wakeup => sender ! "Alright, let me wakeup."
      case Sleep => context.become(sleep)
    }
  }

  object Runner {

    import Sleepy.Action

    case class Starter(sleepy: ActorRef, action: Action)

  }

  class Runner extends Actor {

    import Runner._

    override def receive: Receive = {
      case Starter(sleepy, action) => sleepy ! action
      case msg => println(msg)
    }
  }

  val system = ActorSystem("system")
  val sleepy = system.actorOf(Props[Sleepy], "sleepy")
  val runner = system.actorOf(Props[Runner], "runner")

  import Runner._
  import Sleepy._

  runner ! Starter(sleepy, Sleep)
  runner ! Starter(sleepy, Sleep)
  runner ! Starter(sleepy, Sleep)
  runner ! Starter(sleepy, Sleep)
  runner ! Starter(sleepy, Wakeup)
  runner ! Starter(sleepy, Wakeup)
  runner ! Starter(sleepy, Wakeup)
}
