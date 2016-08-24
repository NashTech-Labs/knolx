package controllers

import javax.inject.Inject

import models.User
import play.api.libs.json.Json
import play.api.mvc.{AnyContent, Action, Controller}
import services.{UserService}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class UsersController @Inject()(userService: UserService) extends Controller {


  def getAllUsers: Action[AnyContent] = Action.async {
    implicit request =>
      userService.getAll.map {
        users =>
          implicit val jsonFormat = Json.format[User]
          Ok(Json.stringify(Json.toJson(users)).replaceAll("\\s+", ""))
      }
  }


}
