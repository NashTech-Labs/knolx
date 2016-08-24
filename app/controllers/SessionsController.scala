package controllers

import java.sql.Date
import javax.inject.Inject

import models.{KSessionView, User, KSession}
import net.sf.ehcache.search.expression.And
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
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

  def getAllSessions: Action[AnyContent] = Action.async {
    implicit request =>
      kSessionService.createView.map {
        view =>
          implicit val jsonFormat = Json.format[KSessionView]
          Ok(Json.stringify(Json.toJson(view)).replaceAll("\\s+", ""))
      }
  }


  def updateSession: Action[AnyContent] = Action.async {
    implicit request =>
      sessionsForm.bindFromRequest.fold(
        formWithErrors => {
          Logger.error("BadRequest." + formWithErrors)
          Future.successful(BadRequest("BadRequest"))
        },
        validData => {
          kSessionService.upDateSession(validData)
          Future.successful(Ok(views.html.tables(webJarAssets, sessionsForm)))
        }
      )
  }

  def createSession: Action[AnyContent] = Action.async {
    implicit request =>
      sessionsForm.bindFromRequest.fold(
        formWithErrors => {
          Logger.error("BadRequest." + formWithErrors)
          Future.successful(BadRequest("BadRequest"))
        },
        validData => {
          Logger.info("Scheduling session")
          kSessionService.createSession(validData)
          userService.getId(cacheService.getCache.get).flatMap(id => userService.getAll().map(list => Ok(views.html.adminKnolexForm(webJarAssets, sessionsForm, list, id.get.toString))))

        }
      )

  }

}
