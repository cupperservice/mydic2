package DI

import javax.inject.Inject

import com.google.inject.{AbstractModule, Guice, Injector}

class InfrastructureModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[cupper.mydic2.models.WordRepo]).to(classOf[cupper.mydic2.dao.WordRepo])
    bind(classOf[cupper.mydic2.models.ExampleRepo]).to(classOf[cupper.mydic2.dao.ExampleRepo])
  }
}
