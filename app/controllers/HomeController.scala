package controllers

import javax.inject._


import models.{Login, User}

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, Controller}
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.inject.Injector
import play.api.Logger

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

      "emailId" -> nonEmptyText,
      "password" -> nonEmptyText(MIN_LENGTH_OF_PASSWORD),
      "name" -> nonEmptyText(MIN_LENGTH_OF_NAME),
      "designation" -> optional(text),
      "id" -> optional(longNumber)
    )(User.apply)(User.unapply))

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

  def homePage: Action[AnyContent] = Action.async {
    implicit request =>
      Logger.debug("Redirecting HomePage")
      Future(Ok(views.html.home(webJarAssets, loginForm, signUpForm)))

  }

  def signIn: Action[AnyContent] = Action.async {

    implicit request =>
      Logger.debug("signingIn in progress. ")
      loginForm.bindFromRequest.fold(

        formWithErrors => {
          Logger.error("Sign-In badRequest.")
          Future(BadRequest(views.html.home(webJarAssets, formWithErrors, signUpForm)))
        },
        validData => {
          val isValid = userService.addUser(validData.emailId, validData.password)
          isValid.map { validatedEmail => if (validatedEmail)
            Redirect(routes.DashboardController.dashboard).withSession("id" -> validData.emailId)
          else {

            Logger.error("User Not Found")
            Redirect(routes.HomeController.homePage).flashing("ERROR" -> WRONG_LOGIN_DETAILS)
          }

          }
        }

      )
  }


  def signUp: Action[AnyContent] = Action.async {

    implicit request =>
      Logger.debug("signingUp in progress. ")
      signUpForm.bindFromRequest.fold(
        formWithErrors => {
          Logger.error("Sign-up badRequest.")
          Future(BadRequest(views.html.home(webJarAssets, loginForm, formWithErrors)))
        },
        validData => {
          val encodedUserdata = userService.encodePassword(validData, validData.password)

          userService.validateEmail(encodedUserdata.emailId).flatMap(value => if (value == true) {

            val isInserted = userService.signUpUser(encodedUserdata)
            isInserted.map(value => if (value) {

              Redirect(routes.DashboardController.dashboard).withSession("id" -> encodedUserdata.emailId)
            }
            else {

              Redirect(routes.HomeController.homePage).flashing("ERROR_DURING_SIGNUP" -> ERROR_DURING_SIGNUP)
            })
          }
          else {
            Future(Redirect(routes.HomeController.homePage).flashing("ENTERED_EMAIL_EXISTS" -> ENTERED_EMAIL_EXISTS))
          })

        }
      )
  }

  def signOut: Action[AnyContent] = Action.async {
    Future {
      Redirect(routes.HomeController.homePage).withNewSession.flashing("SUCCESS" -> LOGOUT_SUCCESSFUL)
    }

  }

}


