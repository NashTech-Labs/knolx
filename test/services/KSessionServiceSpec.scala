package services


import java.sql.Date

import models.KSession
import org.mockito.Mockito._
import org.specs2.mock.Mockito
import play.api.test.{PlaySpecification, WithApplication}
import repo.{UserRepository, KSessionRepository}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


class KSessionServiceSpec extends PlaySpecification with Mockito{

  val kSessionRepository = mock[KSessionRepository]
  val userRepository = mock[UserRepository]
  val kSessionService = new KSessionService(kSessionRepository,userRepository )

  val kSession1 = KSession(Some("Kafka Connect"),new Date(1472063400000L),2,false,1,None)
  val kSession2 = KSession(Some("Generic Programming"),new Date(1472063400000L),1,false,1,Some(2))

  "get all Knolx sessions" in new WithApplication() {

    when(kSessionRepository.getAll).thenReturn(Future(List(kSession1,kSession2)))
  val result = await(kSessionService.getAll())
    result === List(kSession1,kSession2)
  }

  "get user-id by Date" in new WithApplication() {

    when(kSessionRepository.getAllByDate(new Date(1472063400000L))).thenReturn(Future(List(kSession1,kSession2)))
    val result = await(kSessionService.getUserIDByDate(new Date(1472063400000L)))
    result === List(1)
  }

}
