package controllers

import javax.inject._

import utils.Constants
import models.{Login, User}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.inject.Injector
import services.UserService
import utils.Constants._
import play.api.Logger
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

      "emailId" -> email,
      "password" -> nonEmptyText(MIN_LENGTH_OF_PASSWORD),
      "name" -> nonEmptyText(MIN_LENGTH_OF_NAME),

      "designation" -> optional(text),
      "id" -> optional(longNumber))(User.apply)(User.unapply))

  val loginForm = Form(
    mapping(

      "emailId" -> email,
      "password" -> nonEmptyText(MIN_LENGTH_OF_PASSWORD)
    )(Login.apply)(Login.unapply))

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */

  def homePage = Action.async {
    implicit request =>
      Logger.debug("Redirecting HomePage")
      Future(Ok(views.html.home(webJarAssets, loginForm, signUpForm)))

  }

  def signIn = Action.async {

    implicit request =>

      Logger.debug("signingIn in progress. ")
      loginForm.bindFromRequest.fold(
        formwithErrors => {
          Logger.error("Sign-In badRequest.")
          Future(BadRequest(views.html.home(webJarAssets, formwithErrors, signUpForm)))
        },
        userData => {
          val res = userService.validateUser(userData.emailId, userData.password)
          res.map { x => if (x == true) {
            Redirect(routes.DashboardController.dashboard).withSession("id" -> userData.emailId)

          }
          else {
            Logger.error("User Not Found")
            Redirect(routes.HomeController.homePage).flashing("ERROR" -> WRONG_LOGIN_DETAILS)
          }
          }
        }

      )
  }


  def signUp = Action.async {

    implicit request =>
      Logger.debug("signingUp in progress. ")
      signUpForm.bindFromRequest.fold(
        formwithErrors => {
          Logger.error("Sign-up badRequest.")
          Future(Ok("BadRequest"))
        },
        userData => {

          userService.validateEmail(userData.emailId).flatMap(value => if (value == true) {

            val res = userService.signUpUser(userData)
            res.map(value => if (value) {

              Ok("userregisterd")
            }
            else {

              Ok("user not registerd")
            })
          }
          else {
            Future(Ok("user alerady registerd"))
          })
        }
      )

  }

  def signOut = Action.async {
    Future{Redirect(routes.HomeController.homePage).withNewSession.flashing("SUCCESS" -> LOGOUT_SUCCESSFUL)}
  }

}


