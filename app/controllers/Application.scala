package controllers

import javax.inject.Inject
import services.{KSessionService, UserService}
import play.api.mvc.{Action, AnyContent, Controller}
import play.api.routing.JavaScriptReverseRouter
import com.knoldus.Scheduler

class Application @Inject()(scheduler: Scheduler,kSessionService: KSessionService,userService: UserService) extends Controller {

 def javascriptRoutes: Action[AnyContent] = Action { implicit request =>
    Ok(JavaScriptReverseRouter("jsRoutes")(
      routes.javascript.UsersController.getAllUsers,
      routes.javascript.SessionsController.getAllSessions,
      routes.javascript.DashboardController.renderTablePage,
      routes.javascript.DashboardController.renderKnolxForm,
      routes.javascript.SessionsController.createSession
    )
    )
      .as("text/javascript")
 }

  def index: Action[AnyContent] = Action { implicit request =>

    scheduler.sendReminder(kSessionService,userService)
    Redirect(routes.AuthenticationController.loginPage())
  }

}
