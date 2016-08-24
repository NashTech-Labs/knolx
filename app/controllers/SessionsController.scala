package controllers

import java.sql.Date
import javax.inject.Inject

import akka.actor.FSM.Failure
import akka.actor.Status.Success
import models.{KSessionView, User, KSession}
import net.sf.ehcache.search.expression.And
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages
import play.api.libs.json.Json
import play.api.mvc.{AnyContent, Action, Controller}
import services.{CommitmentService, CacheService, UserService, KSessionService}
import utils.Constants._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.i18n.Messages.Implicits._
import play.api.Play.current


class SessionsController @Inject()(webJarAssets: WebJarAssets, cacheService: CacheService,
                                   kSessionService: KSessionService, userService: UserService,
                                   commitmentService: CommitmentService) extends Controller {

  val sessionsForm: Form[KSession] = Form(
    mapping(
      "topic" -> optional(text),
      "date" -> sqlDate,
      "slot" -> number,
      "status" -> boolean,
      "uid" -> longNumber,
      "id" -> optional(longNumber)
    )(KSession.apply)(KSession.unapply))



  def getAllUsersWithStatusList:Action[AnyContent] = Action.async {
    kSessionService.getAllUserList.map(list => Ok(views.html.tables(webJarAssets,list)))
  }

  def changeStatus(id:Long,topic:String):Action[AnyContent] = Action.async {

    implicit request=>
      Logger.debug("change status controller is called")
      kSessionService.upDateSessionStatus(id,topic).flatMap(x =>
        kSessionService.getAllUserList.map(list => Ok(views.html.tables(webJarAssets, list))))
  }


  def updateSession(id:Long,topic:String):Action[AnyContent] = Action.async {
    implicit request =>
      Logger.debug("ksession is get" + id +"  " + topic)
      kSessionService.getUserKsession(id,topic).
        map(kSession => Ok(views.html.editSessionForm(kSession,sessionsForm))
        )
  }

  def deleteSession(id:Long): Action[AnyContent] = Action.async {
    kSessionService.delete(id).flatMap { id =>
      kSessionService.getAllUserList.map(list => Ok(views.html.tables(webJarAssets, list)))
    }
  }
    def createSession: Action[AnyContent] = Action.async {
      implicit request =>
        sessionsForm.bindFromRequest.fold(
          formWithErrors => {
            Logger.error("BadRequest." + formWithErrors)
            Future.successful(Redirect(routes.SessionsController.getAllUsersWithStatusList()))
          },
          validData => {
            Logger.info("Scheduling session")
            kSessionService.createSession(validData)
            Future.successful(Redirect(routes.SessionsController.getAllUsersWithStatusList()))
          }

        )
    }

}
