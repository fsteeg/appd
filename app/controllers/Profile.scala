package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.io.Source
import java.io.File
import models.User

object Profile extends Controller {

  def index = Action {
    implicit val codec = scala.io.Codec.UTF8
    val jsonSource = Source.fromFile("data/profile/fsteeg.json")
    val jsonValue = Json.parse(jsonSource.mkString)
    val user = User(jsonValue)
    val jobs = jobsFrom("data/profile/jobs/", user)
    Ok(views.html.profile(user, jobs)).as("text/html; charset=utf-8")
  }

  def jobsFrom(location: String, user: User) = {
    for (file <- new File(location).listFiles())
      yield models.Job(Json.parse(Source.fromFile(file).mkString), user)
  }

}