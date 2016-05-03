package apprenticeshipScorecard.frontend.controllers

import javax.inject.Inject

import apprenticeshipScorecard.frontend.services.ApiService
import play.api.mvc._

import scala.concurrent.ExecutionContext

class IndexController @Inject()(api: ApiService)(implicit ec: ExecutionContext) extends Controller {

  def show = Action.async { implicit request =>
    val f1 = api.subjects
    val f2 = api.regions

    for {
      subjects <- f1
      regions <- f2
    } yield Ok(views.html.index(subjects, regions))
  }

}
