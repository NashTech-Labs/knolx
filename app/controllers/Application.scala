package controllers

import play.api.mvc.{AnyContent, Action, Controller}
import play.api.routing.JavaScriptReverseRouter


class Application extends Controller {

 def jsRoutes: Action[AnyContent] = Action { implicit request =>

    Ok(JavaScriptReverseRouter("jsRoutes")(
      routes.javascript.DashboardController.getAll
    )
    )
      .as("text/javascript")
  }

  def index: Action[AnyContent] = Action { implicit request =>
    Redirect(routes.AuthenticationController.loginPage())
  }

}
