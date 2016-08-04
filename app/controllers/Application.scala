package controllers

import play.api.mvc.{Action, Controller}
import play.api.routing.JavaScriptReverseRouter


class Application extends Controller {

  def jsRoutes = Action { implicit request =>

    Ok(JavaScriptReverseRouter("jsRoutes")(
      routes.javascript.AuthenticationController.signOut,
      routes.javascript.DashboardController.getAll
    )
    )
      .as("text/javascript")
  }

  def index = Action { implicit request =>
    Redirect(routes.AuthenticationController.loginPage)
  }

}
