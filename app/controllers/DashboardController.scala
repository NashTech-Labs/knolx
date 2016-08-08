
package controllers

import javax.inject.Inject


import play.api.Play.current
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._
import play.api.libs.json.{Json, Writes}
import play.api.mvc.{Result, Action, AnyContent, Controller}
import play.api.routing.JavaScriptReverseRouter
import play.api.libs.json
import models.User
import services.{CacheService, UserService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class DashboardController @Inject()(cacheService: CacheService, webJarAssets: WebJarAssets, userService: UserService)
  extends Controller {

  /**
    * Action for rendering dashboard of user
    **/

  def renderDashBoard: Action[AnyContent] = Action.async {
    implicit request =>
      cacheService.getCache.fold(Future.successful(Redirect(routes.AuthenticationController.loginPage())
        .flashing("INVALID" -> Messages("please signin")))) { email => userService.getNameByEmail(email)
        .map(name => Ok(views.html.dashboard(webJarAssets, Some(name.get))))
      }
  }

  def getAll: Action[AnyContent] = Action.async {
    implicit request =>
      userService.getAll.map {
        users =>
           implicit val jsonFormat = Json.format[User]
         Ok(Json.stringify(Json.toJson(users)).replaceAll("\\s+",""))
      }
  }

  def renderTablePage : Action[AnyContent] = Action.async{
    implicit request =>
      Future.successful(Ok(views.html.tables(webJarAssets)))
  }

}
