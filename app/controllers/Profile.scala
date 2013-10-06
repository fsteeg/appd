package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.io.Source
import java.io.File
import models.User
import models.Users

object Profile extends Controller {

  def index = Action {
    val user = Users.fromFile(Application.data("profile/fsteeg.json"))
    Ok(views.html.profile(user)).as("text/html; charset=utf-8")
  }

}