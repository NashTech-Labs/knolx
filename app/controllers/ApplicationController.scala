package controllers

import play.api.mvc.{Action, Controller}
import play.api.routing.JavaScriptReverseRouter

/**
  * Created by rahul on 3/8/16.
  */
class ApplicationController extends Controller {

  def jsRoutes = Action { implicit request =>
    println("\n\ninside jsroutes")
    Ok(JavaScriptReverseRouter("jsRoutes")(
      routes.javascript.AuthenticationController.signIn
    )
    )
      .as("text/javascript")
  }

}
