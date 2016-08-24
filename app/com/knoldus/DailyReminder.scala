package com.knoldus

import akka.actor.{Actor, ActorLogging, Props}
import play.api.Play.current
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._
import services.MailService._
import services.{KSessionService, UserService}
import utils.Helpers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object DailyReminderActor {

  def props(kSessionService: KSessionService, userService: UserService): Props =
    Props(new DailyReminderActor(kSessionService, userService))

  case object Tick1

}


class DailyReminderActor(kSessionService: KSessionService, userService: UserService) extends Actor with ActorLogging {

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
            user => {
              sendMail(List(user.email), "KnolX reminder", Messages("remind", user.name.trim, date))

            }
          }
        }
      }
      context.system.scheduler.scheduleOnce(interval, self, Tick1)
  }
}