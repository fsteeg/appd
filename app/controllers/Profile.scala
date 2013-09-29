package controllers

import play.api._
import play.api.mvc._

object Profile extends Controller {

  def index = Action {
    Ok(<h1>Your profile</h1>).as("text/html")
  }

}