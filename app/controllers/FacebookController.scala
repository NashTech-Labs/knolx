package controllers

import javax.inject.Inject

import com.sun.corba.se.spi.ior.ObjectId
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import play.api.Play
import org.scribe.oauth.OAuthService
import org.scribe.model.Token
import play.api.mvc.Action
import org.scribe.builder.ServiceBuilder
import org.scribe.builder.api.FacebookApi
import services.{UserService, CacheService}
import utils.Constants._

import scala.concurrent.Future
import scala.text

//import models.Common
import play.api.Logger
import org.scribe.model.Verifier
import org.scribe.model.OAuthRequest
import org.scribe.model.Verb
import org.scribe.model.Response
import play.api.libs.json.Json
import models.{Login, User}
/**
  * Created by rahul on 1/8/16.
  */
class FacebookController @Inject()(cacheService: CacheService, webJarAssets: WebJarAssets, userService: UserService) extends Controller{

  val apiKey: String = Play.current.configuration.getString("facebook_api_id").get
  val apiSecret: String = Play.current.configuration.getString("facebook_api_secret").get
  val currentUserId = "userId"
  val protectedResourceUrl: String = "https://graph.facebook.com/me";
  val emptyToken: Token = null;

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



  def getOAuthService: OAuthService = {
    val service: OAuthService = new ServiceBuilder()
      .provider(classOf[FacebookApi])
      .apiKey(apiKey)
      .apiSecret(apiSecret)
      .scope("email")
      .callback("http://localhost:9000/facebook/callback")
      .build()
    service
  }

  def facebookLogin: Action[play.api.mvc.AnyContent] = Action {
    try {
      val authorizationUrl: String = getOAuthService.getAuthorizationUrl(emptyToken);
      Redirect(authorizationUrl)
    } catch {
      case ex => {
        Logger.error("Error During Login Through Facebook - " + ex)
        Ok(views.html.home(webJarAssets, loginForm, signUpForm))
      }
    }
  }


  def facebookCallback: Action[play.api.mvc.AnyContent] = Action { implicit request =>
    try {
      getVerifier(request.queryString) match {
        case None => Ok(views.html.home(webJarAssets, loginForm, signUpForm))
        case Some(code) =>
          val verifier: Verifier = new Verifier(code)
          val accessToken: Token = getOAuthService.getAccessToken(emptyToken, verifier)
          val oAuthRequest: OAuthRequest = new OAuthRequest(Verb.GET, protectedResourceUrl)
          getOAuthService.signRequest(accessToken, oAuthRequest)
          val response: Response = oAuthRequest.send
          response.getCode match {
            case 200 =>
              val responseBody = response.getBody
              val json = Json.parse(responseBody)
              val userEmailId = (json \ "email").asOpt[String]
              val userNetworkId = (json \ "id").asOpt[String]
              val userId = getOrCreateUserBySocialNetwork(userEmailId.get)
              val userSession = request.session + (currentUserId -> userId)
              Redirect(routes.DashboardController.renderDashBoard).withSession(userSession)
            case 400 =>
              Logger.error("Error 400-  During Login Through Facebook " + response.getBody)
              Ok(views.html.home(webJarAssets, loginForm, signUpForm))
            case _ =>
              Logger.error("Error " + response.getCode + " : During Login Through Facebook - " + response.getBody)
              Ok(views.html.home(webJarAssets, loginForm, signUpForm))
          }
      }

    } catch {
      case ex => {
        Logger.error("Error During Login Through Facebook - " + ex)
        Ok(views.html.home(webJarAssets, loginForm, signUpForm))
      }
    }
  }

  def getVerifier(queryString: Map[String, Seq[String]]): Option[String] = {
    val seq = queryString.get("code").getOrElse(Seq())
    seq.isEmpty match {
      case true => None
      case false => seq.headOption
    }
  }

  def getOrCreateUserBySocialNetwork(userName: String): String = {
    val userList = findUserViaSocialNetwork(userName)
   val userInfo = userList.map(userExist => if (!userExist) {
     val newUser = User(userName,"","",None,None)
     userService.signUpUser(newUser)
     userName
   } else {
     userName
   })
    userInfo.value.get.get
  }

  def findUserViaSocialNetwork(emailId: String): Future[Boolean] = {
    userService.validateEmail(emailId)
  }

}
