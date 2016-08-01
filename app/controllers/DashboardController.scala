
package controllers

import javax.inject.Inject
import play.api.i18n.Messages
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.mvc.{Action, AnyContent, Controller}
import services.{CacheService, UserService}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
  * Created by deepti on 26/7/16.
  */

class DashboardController @Inject()(cacheService: CacheService, webJarAssets: WebJarAssets, userService: UserService) extends Controller {

   def renderDashBoard:Action[AnyContent] = Action.async {
    implicit request =>
      cacheService.getCache.fold(Future.successful(Redirect(routes.AuthenticationController.renderHomePage())
        .flashing("INVALID" -> Messages("please.signin")))) { email => userService.getNameByEmail(email)
        .map(name => Ok(views.html.dashboard(webJarAssets, Some(name)))) }

  }

}