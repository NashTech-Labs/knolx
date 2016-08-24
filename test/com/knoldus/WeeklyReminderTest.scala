package com.knoldus

import java.sql.Date

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestKit}
import com.knoldus.WeeklyReminderActor.Tick2
import models.KSession
import org.mockito.Mockito._
import org.scalatest.WordSpecLike
import org.scalatest.mock.MockitoSugar
import services.CommitmentService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class WeeklyReminderTest extends TestKit(ActorSystem("system")) with WordSpecLike with MockitoSugar {


  val mockedCommitmentService = mock[CommitmentService]
  val weekly = Props(classOf[WeeklyReminderActor], mockedCommitmentService)
  val actor = TestActorRef(weekly)

  "weekly reminder" must {
    "send reminder to schedule KnolX" in {

      when(mockedCommitmentService.joinService()) thenReturn
        Future(List(("admin@gmail.com", "admin", Some(KSession(Some("Spark"),
          new Date(61443340200000L), 1, true, 1, Some(1))))))


      actor ! Tick2
      expectNoMsg()
    }


    "send reminder to schedule KnolX to new users" in {
      when(mockedCommitmentService.joinService()) thenReturn
        Future(List(("admin@gmail.com", "admin",
          None)))

      actor ! Tick2
      expectNoMsg()
    }
  }


}
