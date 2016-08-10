
package controllers

import javax.inject.Inject

import models.{KSession, User}
import play.api.Play.current
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}
import services.{CacheService, KSessionService, UserService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future



class DashboardController @Inject()(cacheService: CacheService, webJarAssets: WebJarAssets, userService: UserService,kSessionService: KSessionService)
  extends Controller {

  /**
    * Action for rendering dashboard of user
    **/

  def renderDashBoard: Action[AnyContent] = Action.async {
    implicit request =>
      cacheService.getCache.fold(Future.successful(Redirect(routes.AuthenticationController.loginPage())
        .flashing("INVALID" -> Messages("please sign in")))) { email => userService.getNameAndCategoryByEmail(email).
        map(name => name.fold(Ok(views.html.dashboard(webJarAssets, None, None)))
        { tupleOfNameAndCategory => Ok(views.html.dashboard(webJarAssets, Some(tupleOfNameAndCategory._2), Some(tupleOfNameAndCategory._1))) })
      }
  }

  def getAllUsers: Action[AnyContent] = Action.async {
    implicit request =>
      userService.getAll.map {
        users =>
           implicit val jsonFormat = Json.format[User]
         Ok(Json.stringify(Json.toJson(users)).replaceAll("\\s+",""))
      }
  }

  def getAllSessions :Action[AnyContent] = Action.async {
    implicit request =>
      kSessionService.getAll.map {
        users =>
          implicit val jsonFormat = Json.format[KSession]
          Ok(Json.stringify(Json.toJson(users)).replaceAll("\\s+",""))
      }
  }

  def renderTablePage : Action[AnyContent] = Action.async{
    implicit request =>
      Future.successful(Ok(views.html.tables(webJarAssets)))
  }

}
