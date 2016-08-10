
package controllers

import javax.inject.Inject


import play.api.Logger
import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._
import play.api.libs.json.{Json, Writes}
import play.api.mvc.{Result, Action, AnyContent, Controller}

import play.api.routing.JavaScriptReverseRouter
import play.api.libs.json
import models.{User}
import services.{MailService, KSessionService, CacheService, UserService}


import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future



class DashboardController @Inject()(cacheService: CacheService, webJarAssets: WebJarAssets,
                                    userService: UserService,kSessionService: KSessionService,mailService: MailService )
  extends Controller {

  /**
    * Action for rendering dashboard of user
    **/

  val knolxForm = Form(
    tuple(
      "userId" -> text,
      "date_1" -> date,
      "date_2" -> date,
      "date_3" -> date
    ))

  def renderDashBoard: Action[AnyContent] = Action.async {
    implicit request =>
      cacheService.getCache.fold(Future.successful(Redirect(routes.AuthenticationController.loginPage())
        .flashing("INVALID" -> Messages("please sign in")))) { email => userService.getNameAndCategoryByEmail(email).
        map(name => name.fold(Ok(views.html.dashboard(webJarAssets, None, None)))
        { tupleOfNameAndCategory => Ok(views.html.dashboard(webJarAssets, Some(tupleOfNameAndCategory._2), Some(tupleOfNameAndCategory._1))) })
      }
  }

  def renderTablePage : Action[AnyContent] = Action.async{
    implicit request =>
      Future.successful(Ok(views.html.tables(webJarAssets)))
  }

  def renderKnolxForm: Action[AnyContent] = Action.async {
    implicit request =>
        userService.getAll.map((list: List[User]) => Ok(views.html.adminKnolexForm(webJarAssets, knolxForm, list)))
  }

  def mailKnolxScheduler: Action[AnyContent] = Action.async {
    implicit request =>
      require(knolxForm.value.isEmpty,"")
    knolxForm.bindFromRequest.fold(
      formWithErrors => {
        Logger.error("Sign-In badRequest.")
        Future.successful(BadRequest(""))
      },
      validData => {
       mailService.sendHtmlEmail(List(validData._1),"Schedule Knolx","<a href = 'https://www.google.co.in'>click here</a>")//send lin which will render a form with default date and email
        Future.successful(Redirect(routes.AuthenticationController.loginPage()))
      })
  }
}
