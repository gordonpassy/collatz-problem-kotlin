package ke.co.weetkit.collatzproblem.model

import com.fasterxml.jackson.annotation.JsonFormat

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type

import java.time.LocalDateTime
import java.util.UUID

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.Version

@MappedSuperclass
abstract class Base (
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type="uuid-char")
    @Column(name="id", columnDefinition = "VARCHAR(255)", insertable = false, updatable = false, nullable = false)
    var id: UUID = UUID.randomUUID(),

    @Version
    var version: Long = 0,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Column(nullable = false, columnDefinition = "datetime default CURRENT_TIMESTAMP")
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Column(nullable = false, columnDefinition = "datetime default CURRENT_TIMESTAMP")
    var updatedAt: LocalDateTime = LocalDateTime.now()
)