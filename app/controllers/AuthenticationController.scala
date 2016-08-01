package controllers

import javax.inject._

import models.User
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Security, Action, AnyContent, Controller}
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.inject.Injector
import play.api.Logger
import play.api.cache._
import services.{CacheService, UserService}
import utils.Constants._
import utils.Helpers
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.i18n.Messages


/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */



@Singleton
class AuthenticationController @Inject()(cacheService: CacheService, webJarAssets: WebJarAssets, userService: UserService) extends Controller {

  val signUpForm = Form(
    mapping(
      "emailId" -> nonEmptyText,
      "password" -> nonEmptyText(MIN_LENGTH_OF_PASSWORD),
      "name" -> nonEmptyText(MIN_LENGTH_OF_NAME),
      "designation" -> optional(text),
      "id" -> optional(longNumber)
    )(User.apply)(User.unapply))

  val loginForm = Form(
    tuple(
      "emailId" -> email,
      "password" -> nonEmptyText(MIN_LENGTH_OF_PASSWORD)
    ))

  /**
    * Create an Action to render an Home page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */

  def renderHomePage: Action[AnyContent] = Action.async {
    implicit request =>
      Logger.debug("Redirecting renderHomePage")
      cacheService.getCache.fold(Future.successful(Ok(views.html.home(webJarAssets, loginForm, signUpForm)))
      ) { email => userService.getNameByEmail(email).map(name => Ok(views.html.dashboard(webJarAssets, Some(name)))) }

  }

  def signIn: Action[AnyContent] = Action.async {
    implicit request =>
      Logger.debug("signingIn in progress. ")
      loginForm.bindFromRequest.fold(
        formWithErrors => {
          Logger.error("Sign-In badRequest.")
          Future.successful(BadRequest(views.html.home(webJarAssets, formWithErrors, signUpForm)))
        },
        validData => {

          val encodedPassword: String = Helpers.passwordEncoder(validData._2)
          val isValid: Future[Boolean] = userService.validateUser(validData._1, encodedPassword)

          isValid.map { validatedEmail => if (validatedEmail) {
            cacheService.setCache("id", validData._1)
            Redirect(routes.DashboardController.renderDashBoard)
          }
          else {
            Logger.error("User Not Found")
            Redirect(routes.AuthenticationController.renderHomePage).flashing("ERROR" -> Messages("wrong.login"))
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
          val encodedUserdata: User = validData.copy(email = validData.email.toLowerCase(),
            password = Helpers.passwordEncoder(validData.password), name = validData.name, designation = validData.designation)
          userService.validateEmail(encodedUserdata.email).flatMap(value => if (!value) {
            val isInserted: Future[Boolean] = userService.signUpUser(encodedUserdata)
            isInserted.map(value => if (value) {
              cacheService.setCache("id", validData.email)
              Redirect(routes.DashboardController.renderDashBoard)
            }
            else {
              Redirect(routes.AuthenticationController.renderHomePage).flashing("SIGNUP.ERROR" -> Messages("signup.error"))
            })
          }
          else {

            Future(Redirect(routes.AuthenticationController.renderHomePage).flashing("EMAIL.EXISTS" -> Messages("email.exists")))

          })
        }
      )
  }


  def signOut: Action[AnyContent] = Action.async {

    cacheService.remove("id")
    Future.successful{
      Redirect(routes.AuthenticationController.renderHomePage).flashing("SUCCESS" -> Messages("logout.success"))


    }

  }
}


