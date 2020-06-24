package cupper.mydic2

import cupper.mydic2.model.DictionaryComponent
import cupper.mydic2.model.RepositoryComponent
import cupper.mydic2.repository.Repository
import cupper.mydic2.repository.http.RepositoryImpl

object ComponentRegistry extends DictionaryComponent with RepositoryComponent {
  override val repository: Repository = new RepositoryImpl
  override val dictionary: Dictionary = new Dictionary
}
