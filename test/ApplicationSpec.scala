import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json._
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Transaction Service" should {
    "create a root transaction" in new WithApplication {
      val request = route(FakeRequest(PUT, "/transactionservice/1", FakeHeaders(), Json.obj("amount" -> 1, "types" -> "A"))).get
      status(request) must equalTo(OK)
    }

    "create a child transaction" in new WithApplication {
      val request = route(FakeRequest(PUT, "/transactionservice/2", FakeHeaders(), Json.obj("amount" -> 1, "types" -> "A", "parentId" -> 1))).get
      status(request) must equalTo(OK)
    }

    "response a root transaction" in new WithApplication {
      val request = route(FakeRequest(GET, "/transactionservice/1")).get
      status(request) must equalTo(OK)
      val response = Json.parse(contentAsString(request))
      (response \ "amount").as[Double] must equalTo(1)
      (response \ "types").as[String]  must equalTo("A")
      (response \ "parentId").asOpt[Int]  must beNone
    }

    "response a child transaction" in new WithApplication {
      val request = route(FakeRequest(GET, "/transactionservice/2")).get
      status(request) must equalTo(OK)
      val response = Json.parse(contentAsString(request))
      (response \ "amount").as[Double] must equalTo(1)
      (response \ "types").as[String]  must equalTo("A")
      (response \ "parentId").as[Int]  must equalTo(1)
    }

    "response list of transaction of a type" in new WithApplication {
      val request = route(FakeRequest(GET, "/transactionservice/types/A")).get
      status(request) must equalTo(OK)
      val response = Json.parse(contentAsString(request))
      response.as[List[Long]] must equalTo(List(1,2))
    }

    "sum amount of a root transaction" in new WithApplication {
      val request = route(FakeRequest(GET, "/transactionservice/sum/1")).get
      status(request) must equalTo(OK)
      val response = Json.parse(contentAsString(request))
      (response \ "sum").as[Double] must equalTo(2)
    }

    "sum amount of a child transaction" in new WithApplication {
      val request = route(FakeRequest(GET, "/transactionservice/sum/2")).get
      status(request) must equalTo(OK)
      val response = Json.parse(contentAsString(request))
      (response \ "sum").as[Double] must equalTo(1)
    }
  }
}