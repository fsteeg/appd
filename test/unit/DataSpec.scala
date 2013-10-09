package unit

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json
import play.api.libs.json.Json._
import play.api.libs.json.JsValue
import models.Job
import models.User

@RunWith(classOf[JUnitRunner])
class DataSpec extends Specification {

  "User data" should {

    "be accessible via thin user objects over a JSON data structure" in {
      val user = User(userJson)
      user.id mustEqual "fsteeg"
      user.attributes mustEqual Map(
        "name" -> "Fabian",
        "mail" -> "fsteeg@gmail.com")
      user.priorities mustEqual List(
        Map("label" -> "location", "comment" -> "close by, reachable by bicycle"),
        Map("label" -> "activity", "comment" -> "what you actually do (programming) and don't do (meetings)"),
        Map("label" -> "perspective", "comment" -> "is there a sustainable perspective"),
        Map("label" -> "conditions", "comment" -> "hours, offices, etc."))
      user.skills mustEqual List(
        Map("label" -> "Java", "comment" -> "10 years"),
        Map("label" -> "Scala", "comment" -> "5 years"))
    }

    "throw exceptions when trying to access unexpected or missing data" in {
      val user = User(Json.toJson(Map[String, JsValue]()))
      user.id must throwA[user.NoId]
      user.skills must throwA[user.NoSkills]
      user.priorities must throwA[user.NoPriorities]
      user.attributes must throwA[user.NoAttributes]
    }
  }

  val userJson = Json.toJson(Map(
    "id" -> toJson("fsteeg"),
    "attributes" -> toJson(Map(
      "name" -> toJson("Fabian"),
      "mail" -> toJson("fsteeg@gmail.com"))),
    "priorities" -> toJson(List(
      Map("label" -> "location", "comment" -> "close by, reachable by bicycle"),
      Map("label" -> "activity", "comment" -> "what you actually do (programming) and don't do (meetings)"),
      Map("label" -> "perspective", "comment" -> "is there a sustainable perspective"),
      Map("label" -> "conditions", "comment" -> "hours, offices, etc."))),
    "skills" -> toJson(List(
      Map("label" -> "Java", "comment" -> "10 years"),
      Map("label" -> "Scala", "comment" -> "5 years")))))

  val jobJson = Map(
    "id" -> toJson("hbz"),
    "attributes" -> toJson(Map("name" -> toJson("hbz"), "user" -> toJson("fsteeg"))),
    "links" -> toJson(List(Map("label" -> "homepage", "url" -> "http://www.hbz-nrw.de"))),
    "location" -> toJson(Map("lon" -> 6.935512, "lat" -> 50.934060)),
    "priorities" -> toJson(Map(
      "location" -> toJson(Map("score" -> toJson("9"), "comment" -> toJson("Köln"))),
      "activity" -> toJson(Map("score" -> toJson("9"), "comment" -> toJson("developer position"))),
      "perspective" -> toJson(Map("score" -> toJson("9"), "comment" -> toJson("service and development agency in the public sector"))),
      "conditions" -> toJson(Map("score" -> toJson("8"), "comment" -> toJson("quiet, good (but not great) office equipment"))))),
    "skills" -> toJson(Map(
      "Java" -> toJson(Map("score" -> toJson("9"), "comment" -> toJson("Java main language"))),
      "Scala" -> toJson(Map("score" -> toJson("6"), "comment" -> toJson("Play with Java, but Scala in templates"))))))

  "Job data" should {

    "be accessible via thin job objects over a JSON data structure" in {
      val user = User(userJson)
      val job = Job(toJson(jobJson), user)
      job.id mustEqual "hbz"
      job.attributes mustEqual Map("name" -> "hbz", "user" -> "fsteeg")
      job.priorities.data mustEqual Map(
        "location" -> Map("score" -> "9", "comment" -> "Köln"),
        "activity" -> Map("score" -> "9", "comment" -> "developer position"),
        "perspective" -> Map("score" -> "9", "comment" -> "service and development agency in the public sector"),
        "conditions" -> Map("score" -> "8", "comment" -> "quiet, good (but not great) office equipment"))
      job.skills.data mustEqual Map(
        "Java" -> Map("score" -> "9", "comment" -> "Java main language"),
        "Scala" -> Map("score" -> "6", "comment" -> "Play with Java, but Scala in templates"))
      job.links mustEqual List(Map("label" -> "homepage", "url" -> "http://www.hbz-nrw.de"))
      job.location mustEqual Map("lon" -> 6.935512, "lat" -> 50.934060)
    }

    "throw exceptions when trying to access unexpected or missing data" in {
      val user = User(userJson)
      val job = Job(Json.toJson(Map[String, JsValue]()), user)
      job.id must throwA[job.NoId]
      job.skills must throwA[job.NoSkills]
      job.priorities must throwA[job.NoPriorities]
      job.attributes must throwA[job.NoAttributes]
      job.links must throwA[job.NoLinks]
      job.location must throwA[job.NoLocation]
    }

    "implicitly rate itself for its users, by skills and by priorities" in {
      val user = User(userJson)
      val job = Job(toJson(jobJson), user)
      job.score(user.priorities, job.priorities.data) === "9998"
      job.score(user.skills, job.skills.data) === "96"
    }
  }
}
