import apprenticeshipScorecard.frontend.services._
import com.google.inject.AbstractModule

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[ApiService]).to(classOf[ApiServiceImpl])
  }

}