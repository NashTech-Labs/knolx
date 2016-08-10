package controllers

import play.api.mvc.{AnyContent, Action, Controller}
import play.api.routing.JavaScriptReverseRouter


class Application extends Controller {

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
    Redirect(routes.AuthenticationController.loginPage())
  }

}
