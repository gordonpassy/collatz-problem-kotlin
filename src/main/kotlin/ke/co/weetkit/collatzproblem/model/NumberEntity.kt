package ke.co.weetkit.collatzproblem.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "numbers")
class NumberEntity(
    @OneToMany(mappedBy = "numberEntity", fetch = FetchType.LAZY)
    var factors: List<FactorEntity>? = null,

    @Column(name = "number", unique = true, nullable = false)
    var number: Long,

    @Column(name = "hops", nullable = true)
    var hops: Long? = null
):Base()