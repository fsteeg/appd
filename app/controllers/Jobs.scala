package controllers

import play.api._
import play.api.mvc._
import scala.io.Source
import play.api.libs.json.Json
import models.User
import models.Users

object Jobs extends Controller {

  def index = Action {
    val user = Users.fromFile("data/profile/fsteeg.json")
    val jobs = models.Jobs.fromDirectory("data/jobs/", user)
    Ok(views.html.jobs(user, jobs)).as("text/html; charset=utf-8")
  }
}