package controllers

import javax.inject.Inject

import play.api.mvc.{Action, Controller}
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

import utils.Constants._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

/**
  * Created by deepti on 26/7/16.
  */
class DashboardController @Inject()(webJarAssets: WebJarAssets) extends Controller {

  def dashboard = Action.async {
    implicit request =>

      Try(request2session.apply("id").toString).toOption match {
        case Some(name) => Future(Ok(views.html.dashboard(webJarAssets)))
        case None => Future(Redirect(routes.HomeController.homePage).flashing("INVALID" -> INVALID))
      }
  }

}
