package com.knoldus

import java.sql.Date

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestKit}
import com.knoldus.DailyReminderActor.Tick1
import models.User
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.WordSpecLike
import org.scalatest.mock.MockitoSugar
import services.{KSessionService, UserService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DailyReminderTest extends TestKit(ActorSystem("system")) with WordSpecLike with MockitoSugar {

  val mockedKSessionService = mock[KSessionService]
  val mockedUserService = mock[UserService]
  val daily = Props(classOf[DailyReminderActor], mockedKSessionService,mockedUserService)
  val actor = TestActorRef(daily)

  "daily reminder" must{

    "send reminder for upcoming KnolX" in {

      when(mockedKSessionService.getUserIDByDate(any[Date])) thenReturn
        (Future(List(1L)))


      when(mockedUserService.getUserByID(List(1))) thenReturn
        (Future(List(User("rahul@gmail.com", "rahul1234", "rahul", "consultant",0,false, Some(1)))))

      actor ! Tick1
      expectNoMsg()

    }

    "send reminder for upcoming KnolX when no user is there" in {

      when(mockedKSessionService.getUserIDByDate(any[Date])) thenReturn
        (Future(List()))


      when(mockedUserService.getUserByID(List())) thenReturn
        (Future(List()))

      actor ! Tick1
      expectNoMsg()

    }
  }
}
