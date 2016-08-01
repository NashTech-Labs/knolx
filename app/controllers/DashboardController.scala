
package controllers

import javax.inject.Inject

import play.api.Play.current
import play.api.cache.CacheApi
import play.api.i18n.Messages.Implicits._
import play.api.mvc.{Action, AnyContent, Controller}

import repo.UserRepo

import services.{CacheService, UserService}

import utils.Constants

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
  * Created by deepti on 26/7/16.
  */

class DashboardController @Inject()(cacheService: CacheService, webJarAssets: WebJarAssets, userService: UserService) extends Controller {

   def renderDashBoard:Action[AnyContent] = Action.async {
    implicit request =>
      cacheService.getCache.fold(Future.successful(Redirect(routes.AuthenticationController.renderHomePage())
        .flashing("INVALID" -> Constants.INVALID))) { email => userService.getNameByEmail(email)
        .map(name => Ok(views.html.dashboard(webJarAssets, Some(name)))) }

  }

}