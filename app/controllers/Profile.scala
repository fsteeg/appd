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
    Ok(views.html.profile(jsonValue)).as("text/html; charset=utf-8")
  }

}