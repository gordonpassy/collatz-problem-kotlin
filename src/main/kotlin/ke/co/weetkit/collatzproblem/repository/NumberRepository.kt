package ke.co.weetkit.collatzproblem.repository

import ke.co.weetkit.collatzproblem.model.NumberEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NumberRepository: JpaRepository<NumberEntity, UUID> {
}