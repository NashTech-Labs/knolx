package controllers


import java.sql.Date
import play.api.mvc.{Action, AnyContent, Controller}
import play.api.routing.JavaScriptReverseRouter


class Application extends Controller {

  def javascriptRoutes: Action[AnyContent] = Action { implicit request =>
    Ok(JavaScriptReverseRouter("jsRoutes")(
      routes.javascript.UsersController.getAllUsers,
      routes.javascript.SessionsController.getAllSessions,
      routes.javascript.DashboardController.renderTablePage,
      routes.javascript.DashboardController.renderKnolxForm,
      routes.javascript.SessionsController.createSession,
      routes.javascript.SessionsController.renderKnolxByDate,
      routes.javascript.SessionsController.updateSession,
      routes.javascript.SessionsController.doneKnolx
    )
    )
      .as("text/javascript")
  }

  def index: Action[AnyContent] = Action { implicit request =>


    Redirect(routes.AuthenticationController.loginPage())
  }

}
