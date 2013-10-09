package controllers

import play.api._
import play.api.mvc._
import com.typesafe.config.Config

object Application extends Controller {

  def index = Action {
    Ok(views.html.index(
      Notes.notesAsRss(data("notes/")).toString,
      Notes.notesAsRss(data("posts/")).toString))
  }

  def data(kind: String): String = {
    val default = "test/data/"
    (Play.maybeApplication match {
      case Some(application) => application.configuration.getString("appd.data").getOrElse(default)
      case None => default
    }) + kind
  }
}