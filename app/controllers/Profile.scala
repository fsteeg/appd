package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.io.Source
import java.io.File
import models.User
import models.Users

object Profile extends Controller {

  def index = Action { implicit request =>
    val user = Users.fromFile(Application.data("profile/fsteeg.json"))
    render {
      case Accepts.Html() => Ok(views.html.profile(user))
      case Accepts.Json() => Ok(user.json)
    }
  }

}