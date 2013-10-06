package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.io.Source
import java.io.File
import models.User
import models.Users
import models.Jobs

object Profile extends Controller {

  def index = Action {
    val user = Users.fromFile("data/profile/fsteeg.json")
    val jobs = Jobs.fromDirectory("data/profile/jobs/", user)
    Ok(views.html.profile(user, jobs)).as("text/html; charset=utf-8")
  }

}