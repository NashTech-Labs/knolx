package controllers

import javax.inject.Inject

import models.{KSession}
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc.{AnyContent, Action, Controller}
import services.{KSessionService}
import utils.Constants._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
  * Created by rahul on 9/8/16.
  */
class SessionsController @Inject()(kSessionService: KSessionService) extends Controller {

  val sessionsForm = Form(
    mapping(
      "topic" -> text,
      "date" -> nonEmptyText,
      "uid" -> longNumber,
      "id" -> optional(longNumber)
    )(KSession.apply)(KSession.unapply))


  def getAllSessions: Action[AnyContent] = Action.async {
    implicit request =>
      kSessionService.getAll.map {
        users =>
          implicit val jsonFormat = Json.format[KSession]
          Ok(Json.stringify(Json.toJson(users)).replaceAll("\\s+", ""))
      }
  }


  def createSession: Action[AnyContent] = Action.async {
    implicit request =>
      sessionsForm.bindFromRequest.fold(
        formWithErrors => {
          Logger.error("Sign-In badRequest.")
          Future.successful(BadRequest(""))
        },
        validData => {
          Logger.info("Submitting response from user as date")
          Future(Ok(""))
        })
  }

}
