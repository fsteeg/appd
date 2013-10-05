package models

import play.api.libs.json.JsValue

case class Criterion(
  data: Map[String, Map[String, String]],
  score: () => String)

case class Job(json: JsValue, user: User) {

  class NoId(s: String) extends RuntimeException(s)
  class NoPriorities(s: String) extends RuntimeException(s)
  class NoSkills(s: String) extends RuntimeException(s)
  class NoAttributes(s: String) extends RuntimeException(s)
  class NoLocation(s: String) extends RuntimeException(s)
  class NoLinks(s: String) extends RuntimeException(s)

  lazy val id = (json \ "id").
    asOpt[String].
    getOrElse(throw new NoId(json.toString))
  lazy val priorities: Criterion = Criterion(
    (json \ "priorities").
      asOpt[Map[String, Map[String, String]]].
      getOrElse(throw new NoPriorities(json.toString)),
    () => score(user.priorities, priorities.data))
  lazy val skills: Criterion = Criterion(
    (json \ "skills").
      asOpt[Map[String, Map[String, String]]].
      getOrElse(throw new NoSkills(json.toString)),
    () => score(user.skills, skills.data))
  lazy val attributes = (json \ "attributes").
    asOpt[Map[String, String]].
    getOrElse(throw new NoAttributes(json.toString))
  lazy val location = (json \ "location").
    asOpt[Map[String, Double]].
    getOrElse(throw new NoLocation(json.toString))
  lazy val links = (json \ "links").
    asOpt[List[Map[String, String]]].
    getOrElse(throw new NoLinks(json.toString))

  def score(
    userData: List[Map[String, String]],
    jobData: Map[String, Map[String, String]]): String = {
    val res = for (map: Map[String, String] <- userData) yield {
      val id = map.get("label").get
      val score = jobData.getOrElse(id, Map("score" -> 0)).get("score").get
      score
    }
    res.mkString
  }
}