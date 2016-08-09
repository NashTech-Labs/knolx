package controllers

import javax.inject.Inject

import models.User

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.AnyContent
import play.api.mvc.{Result, Action, AnyContent, Controller}
import play.api.Play.current
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import repo.{UserRepository, KSessionRepository}

import services.{UserService, CacheService}

import scala.concurrent.Future


class KsessionController  @Inject()(webJarAssets: WebJarAssets,userRepository: UserRepository) extends Controller{

  val knolxForm = Form(
    tuple(
      "userId" -> text,
      "date" -> date
    ))

  def renderKnolxForm:Action[AnyContent] = Action.async{
    implicit request =>
      userRepository.getAll.map((list: List[User]) => Ok(views.html.adminKnolexForm(webJarAssets,knolxForm,list)))
  }


}