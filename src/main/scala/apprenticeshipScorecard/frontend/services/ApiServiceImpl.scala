package apprenticeshipScorecard.frontend.services

import javax.inject.Inject

import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

case class Subject(subject_tier_2_code: BigDecimal, subject_tier_2_title: String)

object Subject {
  implicit val f = Json.format[Subject]
}

trait ApiService {
  def subjects(implicit ec: ExecutionContext): Future[Seq[Subject]]

  def regions(implicit ec: ExecutionContext): Future[Seq[String]]
}

case class BodyParams(q: Option[String] = None, extract: Option[Seq[String]] = None, max_results: Option[Int] = None, page_size: Option[Int] = None)

object BodyParams {
  implicit val f = Json.format[BodyParams]
}

case class SearchResults[T: Reads](results: Seq[T], result_count: Int, page_number: Int, page_size: Int)

object SearchResults {

  implicit def reads[T: Reads]: Reads[SearchResults[T]] =
    ((__ \ "results").read[Seq[T]] ~
      (__ \ "page_number").read[Int] ~
      (__ \ "page_size").read[Int] ~
      (__ \ "result_count").read[Int]) (SearchResults.apply[T] _)
}

case class Region(region: String)

object Region {
  implicit val reads= Json.reads[Region]
}

class ApiServiceImpl @Inject()(ws: WSClient) extends ApiService {

  override def regions(implicit ec: ExecutionContext): Future[Seq[String]] = {

    val bodyParams = BodyParams(extract = Some(Seq("region")))

    val body = Json.toJson(bodyParams)
    Logger.info(Json.prettyPrint(body))
    ws.url("http://localhost:9004/providers").post(body).map { response =>
      response.status match {
        case 200 => response.json.validate[SearchResults[Region]] match {
          case JsSuccess(sr, _) => sr.results.map(_.region)
          case JsError(errs) =>
            Logger.warn(errs.toString)
            throw new Exception(errs.toString)
        }
        case _ =>
          Logger.warn(response.body)
          throw new Exception(response.body)
      }
    }
  }

  override def subjects(implicit ec: ExecutionContext): Future[Seq[Subject]] = {

    val bodyParams = BodyParams(extract = Some(Seq("subject_tier_2_code", "subject_tier_2_title")))

    val body = Json.toJson(bodyParams)
    Logger.info(Json.prettyPrint(body))
    ws.url("http://localhost:9004/apprenticeships").post(body).map { response =>
      response.status match {
        case 200 => response.json.validate[SearchResults[Subject]] match {
          case JsSuccess(sr, _) => sr.results
          case JsError(errs) =>
            Logger.warn(errs.toString)
            throw new Exception(errs.toString)
        }
        case _ =>
          Logger.warn(response.body)
          throw new Exception(response.body)
      }
    }
  }
}
