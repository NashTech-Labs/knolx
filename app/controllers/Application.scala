package controllers


import java.sql.Date
import play.api.mvc.{Action, AnyContent, Controller}
import play.api.routing.JavaScriptReverseRouter


class Application extends Controller {

  def javascriptRoutes: Action[AnyContent] = Action { implicit request =>
    Ok(JavaScriptReverseRouter("jsRoutes")(
      routes.javascript.UsersController.getAllUsers,
      routes.javascript.SessionsController.changeStatus,
      routes.javascript.SessionsController.deleteSession,
        routes.javascript.SessionsController.getAllUsersWithStatusList,
      routes.javascript.DashboardController.renderKnolxForm,
      routes.javascript.SessionsController.createSession,
      routes.javascript.SessionsController.updateSession

    )
    )
      .as("text/javascript")
  }

  def index: Action[AnyContent] = Action { implicit request =>

    Redirect(routes.AuthenticationController.loginPage())
  }

}
