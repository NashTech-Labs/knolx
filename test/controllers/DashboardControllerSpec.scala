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
import services.{CacheService, UserService}
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
  val cacheService = mock[CacheService]

  val dashBoardController = new DashboardController(cacheService, webJarAssets, userService)
  "should render the dashboard in case renderDashBoard url is hit and user doesNot logout" in new WithApplication() {
    when(cacheService.getCache).thenReturn(Some("johndeo@gmail.com"))

    when(userService.getNameByEmail("johndeo@gmail.com")) thenReturn Future.successful(Some("john"))
    val results = call(dashBoardController.renderDashBoard, FakeRequest(GET, "/"))
    status(results) must equalTo(OK)
    contentAsString(results).contains("knolx | DashBoard")
  }
  "should not render the dashboard in case renderDashBoard url is hit and user doesNot logout" in new WithApplication() {
    when(cacheService.getCache).thenReturn(None)
    when(userService.getNameByEmail("johndeo@gmail.com")) thenReturn Future.successful(None)
    val results = call(dashBoardController.renderDashBoard, FakeRequest(GET, "/"))
    status(results) must equalTo(SEE_OTHER)
    contentAsString(results).contains("knolx")
  }


}
