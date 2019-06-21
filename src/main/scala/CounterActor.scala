import akka.actor.{Actor, ActorSystem, Props}

object CounterActor extends App {

  val actorSystem = ActorSystem("defaultActorSystem")
  val counter = actorSystem.actorOf(Props[Counter], "counterActor")

  class Counter extends Actor {
    var currentValue: Int = 0

    override def receive: Receive = {
      case Increment(value) =>
        if (value > 0) {
          val prevValue = currentValue
          currentValue += value
          println(s"$prevValue incremented by $value giving us $currentValue")
        }
        else println("You must be nuts to increment by negative value!")

      case Decrement(value) => {
        if (currentValue >= value) {
          val prevValue = currentValue
          currentValue -= value
          println(s"$prevValue decremented by $value. We now have $currentValue")
        }
        else println(s"Sorry cannot decrement. $value is grater than $currentValue.")
      }

      case Print => println(s"Current value is $currentValue")
    }
  }

  case class Increment(amount: Int)

  case class Decrement(amount: Int)

  case class Print()

  counter ! Increment(10)
  counter ! Increment(-4)
  counter ! Decrement(2)
  counter ! Decrement(10)
  counter ! Print
}
