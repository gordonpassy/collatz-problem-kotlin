package ke.co.weetkit.collatzproblem.config

//import ke.co.weetkit.collatzproblem.repository.NumberRepository
import ke.co.weetkit.collatzproblem.service.ISyncService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import kotlin.concurrent.thread

@Component
class ApplicationStartup: ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    lateinit var syncService: ISyncService

//    @Autowired
//    lateinit var numberRepository: NumberRepository

    val logger: Logger = LoggerFactory.getLogger(ApplicationStartup::class.java)

    override fun onApplicationEvent(p0: ApplicationReadyEvent) {
        val from = 1L
        val to = 1_000_000L
//        val syncedNumbers = numberRepository.count()
//        if (syncedNumbers == to){
//            logger.info("All numbers synced.{}", syncedNumbers)
//            return
//        }
        val threads     = Runtime.getRuntime().availableProcessors()
        val base: Long  = to/threads
        for (i in 1..threads) {
            syncService.sync(
                from = if (i == 1) from else i.minus(1).times(base).plus(1),
                to = base*i
            )
        }
    }
}