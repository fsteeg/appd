package models

import play.api.libs.json.JsValue
import play.api.libs.json.Json

case class User(json: JsValue) {

  class NoId(s: String) extends RuntimeException(s)
  class NoPriorities(s: String) extends RuntimeException(s)
  class NoSkills(s: String) extends RuntimeException(s)
  class NoAttributes(s: String) extends RuntimeException(s)

  lazy val id = (json \ "id").
    asOpt[String].getOrElse(throw new NoId(json.toString))
  lazy val priorities = (json \ "priorities").
    asOpt[List[Map[String, String]]].
    getOrElse(throw new NoPriorities(json.toString))
  lazy val skills = (json \ "skills").
    asOpt[List[Map[String, String]]].
    getOrElse(throw new NoSkills(json.toString))
  lazy val attributes = (json \ "attributes").
    asOpt[Map[String, String]].
    getOrElse(throw new NoAttributes(json.toString))
}