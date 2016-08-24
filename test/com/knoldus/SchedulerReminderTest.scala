package com.knoldus

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.WordSpecLike
import org.scalatest.mock.MockitoSugar
import services.{CommitmentService, KSessionService, UserService}


class SchedulerReminderTest extends TestKit(ActorSystem("system")) with WordSpecLike with MockitoSugar{

  val mockedCommitmentService = mock[CommitmentService]
  val mockedKSessionService = mock[KSessionService]
  val mockedUserService = mock[UserService]
  val probe = TestProbe()
  val schedulerReminder = new SchedulerReminder(mockedKSessionService,mockedCommitmentService,mockedUserService) {
    override val dailyReminder: ActorRef = probe.ref
    override val weeklyReminder : ActorRef = probe.ref
  }
  schedulerReminder.sendReminder()
}
