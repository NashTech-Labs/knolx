package com.knoldus

import java.util.Calendar

import akka.actor.{Actor, Props}
import play.api.Play.current
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._
import services.CommitmentService
import services.MailService._
import utils.Helpers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object WeeklyReminderActor {

  def props(commitmentService: CommitmentService): Props =

    Props(new WeeklyReminderActor(commitmentService))

  case object Tick2

}

class WeeklyReminderActor(commitmentService: CommitmentService) extends Actor {

  import WeeklyReminderActor._

  val interval = 7.day

  def receive: Receive = {
    case Tick2 =>
      val currentMonth = Helpers.findCurrentMonth()
      val cal = Calendar.getInstance()
      val list = commitmentService.joinService()

      list.map {
        value => if (!value.isEmpty) {
          value.map { tuple => if (!tuple._3.isDefined) {
            sendMail(List(tuple._1), "schedule your KnolX", Messages("schedule.head", tuple._2.trim))

          }
          else {
            cal.setTime(tuple._3.get.date)
            if (cal.get(Calendar.MONTH) != currentMonth && tuple._3.get.status == true) {
              sendMail(List(tuple._1), "schedule your KnolX", Messages("schedule.tail", tuple._2.trim))

            }
          }
          }
        }

      }
      context.system.scheduler.scheduleOnce(interval, self, Tick2)
  }
}
