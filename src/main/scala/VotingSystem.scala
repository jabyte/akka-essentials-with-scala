import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object VotingSystem extends App {

  val system: ActorSystem = ActorSystem("actorSystem")

  case class Vote(candidate: String)

  case object VoteStatusRequest

  case class VoteStatusReply(candidate: Option[String])

  case class AggregateVotes(citizens: Set[ActorRef])

  object Citizen {

  }

  class Citizen extends Actor {
    override def receive: Receive = voted("")

    def voted(candidate: String): Receive = {
      case Vote(candidate) => context.become(voted(candidate))
      case VoteStatusRequest => sender ! candidate
    }

    def voteStatus: Receive = {
      case
    }
  }

  class VoteAggregator extends Actor {

    override def receive: Receive = aggregateVotes


    def aggregateVotes: Receive = {
      case AggregateVotes(citizens) =>
        citizens.map(c => c ! VoteStatusRequest)
      case VoteStatusReply(candidate) =>
    }
  }

  val alice: ActorRef = system.actorOf(Props[Citizen])
  val bob: ActorRef = system.actorOf(Props[Citizen])
  val charlie: ActorRef = system.actorOf(Props[Citizen])
  val daniel: ActorRef = system.actorOf(Props[Citizen])

  alice ! Vote("Martin")
  bob ! Vote("Jonas")
  charlie ! Vote("Roland")
  daniel ! Vote("Roland")

  val voteAggregator = system.actorOf(Props[VoteAggregator])
  voteAggregator ! AggregateVotes(Set(alice, bob, charlie, daniel))
}

