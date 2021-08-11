package ke.co.weetkit.collatzproblem.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "factors")
class FactorEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "number_id", nullable = false, referencedColumnName = "id")
    var numberEntity: NumberEntity,

    @Column(name = "factor")
    var factor: Long,

    @Column(name = "factor_index")
    var factorIndex: Long
): Base()