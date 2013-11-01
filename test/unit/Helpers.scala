package unit

import org.specs2.mutable.Specification

import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.test.Helpers.defaultAwaitTimeout

object Helpers extends Specification {
  def beAcceptedBy(action: Action[AnyContent]) = (ct: String) =>
    contentType(action.apply(FakeRequest().
      withHeaders(("Accept", ct)))) must beSome.which(_ == ct)

  def returnValidJson() = (action: Action[AnyContent]) =>
    contentAsJson(action.apply(FakeRequest().
      withHeaders(("Accept", "application/json")))) must not beNull
}