package controllers

import javax.inject.Inject

import play.api.mvc.{Action, Controller}
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.cache._

import models._

import services.UserService

import utils.Constants._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global





/**
  * Created by deepti on 26/7/16.
  */
class DashboardController @Inject()(cache: CacheApi,webJarAssets: WebJarAssets,userService:UserService) extends Controller {

  def dashboard = Action.async {
    implicit request =>
      Option.apply(cache.get[String]("id")) match {
        case Some(email)=>Future(Ok(views.html.dashboard(webJarAssets,Some(email))))
         case None => Future(Redirect(routes.HomeController.homePage).flashing("INVALID" -> INVALID))
      }

  }

}
