package controllers

import play.api.mvc.{AnyContent, Action, Controller}
import play.api.routing.JavaScriptReverseRouter


class Application extends Controller {

 def javascriptRoutes: Action[AnyContent] = Action { implicit request =>

    Ok(JavaScriptReverseRouter("jsRoutes")(
      routes.javascript.DashboardController.getAllUsers,
      routes.javascript.DashboardController.getAllSessions,
      routes.javascript.DashboardController.renderTablePage
    )
    )
      .as("text/javascript")
  }

  def index: Action[AnyContent] = Action { implicit request =>
    Redirect(routes.AuthenticationController.loginPage())
  }

}
