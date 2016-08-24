package com.knoldus

import java.util.Calendar

import akka.actor.{Actor, ActorSystem, Props}
import play.api.Play.current
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._
import services.MailService._
import services.{CommitmentService, KSessionService, UserService}
import utils.Helpers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


object DailyReminderActor {

  def props(kSessionService: KSessionService, userService: UserService): Props =
    Props(new DailyReminderActor(kSessionService, userService))

  case object Tick1

}

object WeeklyReminderActor {

  def props(kSessionService: KSessionService, commitmentService: CommitmentService, userService: UserService): Props =

    Props(new WeeklyReminderActor(kSessionService, commitmentService, userService))

  case object Tick2

}

class DailyReminderActor(kSessionService: KSessionService, userService: UserService) extends Actor {

  import DailyReminderActor._

  val interval = 24.hour

  def receive: Receive = {
    case Tick1 =>
      val date = Helpers.find()
      val uidList = kSessionService.getUserIDByDate(date)
      val userList = uidList.flatMap(uids => userService.getUserByID(uids))
      userList.map {
        users => if (!users.isEmpty) {
          users.map {
            user => sendMail(List(user.email), "KnolX reminder", Messages("remind", user.name.trim, date))
              context.system.scheduler.scheduleOnce(interval, self, Tick1)
          }
        }
        else {
          context.system.scheduler.scheduleOnce(interval, self, Tick1)
        }
      }
  }
}


class WeeklyReminderActor(kSessionService: KSessionService, commitmentService: CommitmentService, userService: UserService) extends Actor {

  import WeeklyReminderActor._

  val interval = 7.days

  def receive: Receive = {
    case Tick2 =>
      val currentMonth = Helpers.findCurrentMonth()
      val cal = Calendar.getInstance()
      val list = commitmentService.joinService()

      list.map {
        value => if (!value.isEmpty) {
          value.map { tuple => if (!tuple._3.isDefined) {
            sendMail(List(tuple._1), "schedule your KnolX", Messages("schedule.head", tuple._2.trim))
            context.system.scheduler.scheduleOnce(interval, self, Tick2)
          }
          else {
            cal.setTime(tuple._3.get.date)
            if (cal.get(Calendar.MONTH) != currentMonth && tuple._3.get.status == true) {
              sendMail(List(tuple._1), "schedule your KnolX", Messages("schedule.tail", tuple._2.trim))
              context.system.scheduler.scheduleOnce(interval, self, Tick2)
            }
          }
          }
        }
      }
  }
}


class SchedulerReminder {

  val system = ActorSystem("system")

  def sendReminder(kSessionService: KSessionService, commitmentService: CommitmentService, userService: UserService): Unit = {
    val dailyReminder = system.actorOf(DailyReminderActor.props(kSessionService, userService), "reminder-actor1")
    val weeklyReminder = system.actorOf(WeeklyReminderActor.props(kSessionService, commitmentService, userService), "reminder-actor2")
    dailyReminder ! DailyReminderActor.Tick1
    weeklyReminder ! WeeklyReminderActor.Tick2
  }

}




