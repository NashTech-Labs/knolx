package controllers

import javax.inject._

import models.User
import play.api.Logger
import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._
import play.api.mvc.{Action, AnyContent, Controller}
import services.{CacheService, KSessionService, UserService}
import utils.Constants._
import utils.Helpers
import com.knoldus.Scheduler

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class AuthenticationController @Inject()(cacheService: CacheService, scheduler: Scheduler,
                                         webJarAssets: WebJarAssets, userService: UserService, kSessionService: KSessionService) extends Controller {

  val signUpForm = Form(
    mapping(
      "emailId" -> nonEmptyText,
      "password" -> nonEmptyText(MIN_LENGTH_OF_PASSWORD),
      "name" -> nonEmptyText(MIN_LENGTH_OF_NAME),
      "designation" -> text,
      "category" -> ignored(0),
      "isBanned" -> ignored(false),
      "id" -> optional(longNumber)
    )(User.apply)(User.unapply))

  val loginForm = Form(
    tuple(
      "emailId" -> email,
      "password" -> nonEmptyText(MIN_LENGTH_OF_PASSWORD)
    ))


  /**
    * Create an Action to render an Home page with login and signup option
    */

  def loginPage: Action[AnyContent] = Action.async {
    implicit request =>
      //scheduler.sendReminder(kSessionService,userService)
      Logger.debug("Redirecting renderHomePage")
      cacheService.getCache.fold(Future.successful(Ok(views.html.home(webJarAssets, loginForm, signUpForm)))
      ) { email => userService.getNameAndCategoryByEmail(email).
        map(name => name.fold(Ok(views.html.dashboard(webJarAssets, None, None))) { tupleOfNameAndCategory => Ok(views.html.dashboard(webJarAssets, Some(tupleOfNameAndCategory._2), Some(tupleOfNameAndCategory._1))) })


      }
  }

  /**
    * Create an Action for sign in option
    */
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
          userService.validateUser(validData._1, encodedPassword)
            .map { validatedEmail => if (validatedEmail) {
              cacheService.setCache("id", validData._1)
              Redirect(routes.DashboardController.renderDashBoard())
            }
            else {
              Logger.error("User Not Found")
              Redirect(routes.AuthenticationController.loginPage()).flashing("ERROR" -> Messages("wrong.login"))
            }
            }
        }
      )
  }


  /**
    * Create an Action for signup option
    */
  def signUp: Action[AnyContent] = Action.async {
    implicit request =>
      Logger.debug("signingUp in progress. ")
      signUpForm.bindFromRequest.fold(
        formWithErrors => {
          Logger.error("Sign-up badRequest.")
          Future.successful(BadRequest(views.html.home(webJarAssets, loginForm, formWithErrors)))
        },
        validData => {
          val encodedUserdata: User = validData.copy(email = validData.email.toLowerCase(),
            password = Helpers.passwordEncoder(validData.password), name = validData.name, designation = validData.designation)
          userService.validateEmail(encodedUserdata.email).flatMap(value => if (!value) {
            val isInserted: Future[Boolean] = userService.signUpUser(encodedUserdata)
            isInserted.map(value => if (value) {
              cacheService.setCache("id", validData.email)
              Redirect(routes.DashboardController.renderDashBoard())
            }
            else {
              Redirect(routes.AuthenticationController.loginPage()).flashing("SIGNUP.ERROR" -> Messages("signup.error"))
            })
          }
          else {

            Future.successful(Redirect(routes.AuthenticationController.loginPage).flashing("EMAIL.EXISTS" -> Messages("email.exists")))

          })
        }
      )
  }

  /**
    * Create an Action for signout option
    */
  def signOut: Action[AnyContent] = Action.async {
    cacheService.remove("id")
    Future.successful {
      Redirect(routes.AuthenticationController.loginPage).flashing("SUCCESS" -> Messages("logout.success"))

    }

  }
}


