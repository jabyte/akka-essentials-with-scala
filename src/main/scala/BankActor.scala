import akka.actor.{Actor, ActorSystem, Props}

object BankActor extends App {
  val actorSystem = ActorSystem("defaultActorSystem")
  val account = actorSystem.actorOf(Props[Account], "JabirAccount")
  val person = actorSystem.actorOf(Props[Person], "Jabir")

  class Person extends Actor {
    override val receive = {
      case _ => "Executed"
    }
  }

  object Account {

    case class Deposit(amount: Float)

    case class Withdraw(amount: Float)

    case object Statement

  }

  class Account extends Actor {

    import Account._

    var balance: Float = 0

    override val receive = {
      case Deposit(amount) =>
        if (amount <= 0) println(s"You cannot deposit $amount.")
        else {
          balance += amount
          println(s"Deposit of $amount successful. Available balance is $balance.")
        }

      case Withdraw(amount) =>
        if (amount <= 0) println(s"You cannot withdraw zero or negative amount.")
        else if (amount > balance) println("Insufficient account balance. Please request a lesser amount.")
        else {
          balance -= amount
          println(s"Withdrawal of $amount successful. Available balance is $balance.")
        }

      case Statement => println(s"Your account balance is $balance")
    }
  }

  import Account._

  account ! Deposit(1000)
  account ! Withdraw(350)
  account ! Statement
  ///////////////////////
  account ! Deposit(-1000)
  account ! Withdraw(1)
  account ! Statement
}
