package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.io.Source

object Profile extends Controller {

  def index = Action {
    implicit val codec = scala.io.Codec.UTF8
    val jsonSource = Source.fromFile("data/profile/fsteeg.json")
    val jsonValue = Json.parse(jsonSource.mkString)
    val profile = models.Profile(jsonValue)
    Ok(views.html.profile(profile)).as("text/html; charset=utf-8")
  }

}