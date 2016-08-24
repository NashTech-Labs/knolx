package com.knoldus

import akka.actor.ActorSystem
import services.{CommitmentService, KSessionService, UserService}


class SchedulerReminder(kSessionService: KSessionService, commitmentService: CommitmentService, userService: UserService) {

  val system = ActorSystem("system")
  val weeklyReminder = system.actorOf(WeeklyReminderActor.props(commitmentService), "reminder-actor2")
  val dailyReminder = system.actorOf(DailyReminderActor.props(kSessionService, userService), "reminder-actor1")

  def sendReminder(): Unit = {
    dailyReminder ! DailyReminderActor.Tick1
    weeklyReminder ! WeeklyReminderActor.Tick2
  }

}





