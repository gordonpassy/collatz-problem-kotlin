package ke.co.weetkit.collatzproblem.repository

import ke.co.weetkit.collatzproblem.model.FactorEntity
import ke.co.weetkit.collatzproblem.model.NumberEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface FactorRepository: JpaRepository<FactorEntity, UUID> {
    fun countAllByNumberEntity(numberEntity: NumberEntity):Long
}