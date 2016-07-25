package controllers

import javax.inject._

import models.{Login, User}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.inject.Injector
import services.UserService
import utils.Constants._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(webJarAssets: WebJarAssets, userService: UserService) extends Controller {

  val signUpForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "emailId" -> email,
      "password" -> nonEmptyText(MIN_LENGTH_OF_PASSWORD),
      "name" -> nonEmptyText(MIN_LENGTH_OF_NAME),
      "address" -> nonEmptyText,
      "designation" -> optional(text))(User.apply)(User.unapply))

  val loginForm = Form(
    mapping(

      "emailId" -> email,
      "password" -> nonEmptyText
    )(Login.apply)(Login.unapply))

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */

  def homePage = Action.async {
    implicit request =>
      Future(Ok(views.html.home(webJarAssets, loginForm)))

  }

 def signin = Action.async{

    implicit request =>
      loginForm.bindFromRequest.fold(
        formwithErrors => {
          Future(BadRequest)
        },
      userData => {
        val res = userService.validateUser(userData.emailId, userData.password)
        res.map { x => if (x == true)
          Ok("success")

        else
          Ok("failure")

        }
      }
      )

  }


}
