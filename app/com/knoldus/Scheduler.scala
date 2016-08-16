package com.knoldus

import akka.actor.{Actor, ActorSystem, Props}
import play.api.Play.current
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._
import services.MailService._
import services.{KSessionService, UserService}
import utils.Helpers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


object ReminderActor {

  def props(kSessionService: KSessionService, userService: UserService): Props =
    Props(new ReminderActor(kSessionService, userService))

  case object Tick

}

class ReminderActor(kSessionService: KSessionService, userService: UserService) extends Actor {

  import ReminderActor._

  val interval = 24.hour

  def receive: Receive = {
    case Tick =>
      val date = Helpers.find()
      val uidList = kSessionService.getUserIDByDate(date)
      val userList = uidList.flatMap(uids => userService.getUserByID(uids))
      userList.map {
        users => if (!users.isEmpty) {
          users.map {
            user => sendMail(List(user.email), "KnolX reminder", Messages("remind", user.name, date))
          }
        } else {
          context.system.scheduler.scheduleOnce(interval, self, Tick)
        }
      }
  }
}

class Scheduler {

  val system = ActorSystem("system")

  def sendReminder(kSessionService: KSessionService, userService: UserService): Unit = {
    val reminder = system.actorOf(ReminderActor.props(kSessionService, userService))
    reminder ! ReminderActor.Tick
  }

}






