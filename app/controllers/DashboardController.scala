
package controllers

import javax.inject.Inject
import com.knoldus.SchedulerReminder
import models.{KSession, User}
import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._
import play.api.mvc.{Action, AnyContent, Controller}
import services.{CacheService, CommitmentService, KSessionService, UserService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class DashboardController @Inject()(cacheService: CacheService, webJarAssets: WebJarAssets, commitmentService: CommitmentService,
                                    userService: UserService, kSessionService: KSessionService) extends Controller {

  val sessionsForm = Form(
    mapping(
      "topic" -> optional(text),
      "date" -> sqlDate,
      "slot" -> number(1),
      "status" -> boolean,
      "uid" -> longNumber,
      "id" -> optional(longNumber)
    )(KSession.apply)(KSession.unapply))

  /**
    * Action for rendering dashboard of user
    **/


  def homePage: Action[AnyContent] = Action.async {
    val schedulerReminder = new SchedulerReminder(kSessionService, commitmentService, userService)
    schedulerReminder.sendReminder()
    Future(Redirect(routes.DashboardController.renderDashBoard()))

  }

  def renderDashBoard: Action[AnyContent] = Action.async {
    implicit request =>
      cacheService.getCache.fold(Future.successful(Redirect(routes.AuthenticationController.loginPage())
        .flashing("INVALID" -> Messages("please sign in")))) { email => userService.getNameAndCategoryByEmail(email).
        map(name => name.fold(Ok(views.html.dashboard(webJarAssets, None, None))) { tupleOfNameAndCategory => Ok(views.html.dashboard(webJarAssets, Some(tupleOfNameAndCategory._2), Some(tupleOfNameAndCategory._1))) })
      }
  }

  def renderTablePage: Action[AnyContent] = Action.async {
    implicit request =>
      kSessionService.getAllUserList.map(list => Ok(views.html.tables(webJarAssets,list)))
  }



  def renderKnolxForm: Action[AnyContent] = Action.async {
    implicit request =>
      userService.getAll.flatMap { (list: List[User]) => {
        userService.getId(cacheService.getCache.get).map(value =>
          Ok(views.html.adminKnolexForm(webJarAssets, sessionsForm, list, value.get.toString))
        )
      }
      }
  }

}
