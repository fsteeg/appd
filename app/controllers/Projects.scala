package controllers

import play.api._
import play.api.mvc._
import scala.io.Source
import play.api.libs.json.Json
import models.User
import models.Users

object Projects extends Controller {

  def index(criterion: String) = Action {
    val user = Users.fromFile(Application.data("profile/fsteeg.json"))
    val jobs = models.Jobs.fromDirectory(Application.data("projects/"), user)
    Ok(views.html.projects(user, jobs, criterion)).as("text/html; charset=utf-8")
  }
}