package controllers

import play.api._
import play.api.mvc._
import Notes._

object Posts extends Controller {
  def index = Notes.content(Application.data("posts/"))
}