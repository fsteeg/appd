package controllers

import play.api._
import play.api.mvc._
import scala.io.Source
import play.api.libs.json.Json
import models.User
import models.Users

object Projects extends Controller {

  def index(criterion: String) = Action { implicit request =>
    val user = Users.fromFile(Application.data("profile/fsteeg.json"))
    val jobs = models.Jobs.fromDirectory(Application.data("projects/"), user)
    render {
      case Accepts.Html() => Ok(views.html.projects(user, jobs, criterion))
      case Accepts.Json() => Ok(Json.arr(jobs.map(_.json)))
    }
  }
}