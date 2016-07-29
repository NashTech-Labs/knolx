package controllers

import org.mockito.Mockito._
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}
import org.specs2.mutable.Specification
import play.api.cache.CacheApi
import play.api.test.{FakeRequest, WithApplication}
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._
import services.UserService
import org.mockito.Mockito._
import org.specs2.mock.Mockito
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by deepti on 26/7/16.
  */
class DashboardControllerSpec extends Specification with Mockito {

  val userService = mock[UserService]
  val webJarAssets = mock[WebJarAssets]
  val cache = mock[CacheApi]

  /*val authenticationController = new AuthenticationController(cache, webJarAssets, userService)
  "should render the dashbaord in case dashboard url is hit and user doesNot logout" in new WithApplication() {
    when(cache.get[String]("id")).thenReturn(Some("johndeo@gmail.com"))
    val results = call(authenticationController.homePage, FakeRequest(GET, "/"))
    status(results) must equalTo(200)
    redirectLocation(results) must beSome("/")
  }

  "should not render the dashbaord in case dashboard url is hit and user logout" in new WithApplication() {
    when(cache.get[String]("id")).thenReturn(None)
    val results = call(authenticationController.homePage, FakeRequest(GET, "/"))
    status(results) must equalTo(200)
    redirectLocation(results) must beSome("/")
  }*/
}
