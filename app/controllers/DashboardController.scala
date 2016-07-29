package controllers

import javax.inject.Inject

import play.api.cache.CacheApi
import play.api.mvc.{Action, Controller}
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import repo.UserRepo
import services.UserService
import utils.Constants

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global





/**
  * Created by deepti on 26/7/16.
  */

class DashboardController @Inject()(cache: CacheApi,webJarAssets: WebJarAssets,userService:UserService) extends Controller {

  def dashboard = Action.async {
    implicit request =>

    cache.get[String]("id").fold(Future(Redirect(routes.HomeController.homePage).flashing("INVALID" ->Constants.INVALID)))
    {email =>userService.getNameByEmail(email).map(name => Ok(views.html.dashboard(webJarAssets,name.capitalize)))}
  }

}
