package ke.co.weetkit.collatzproblem.service

import ke.co.weetkit.collatzproblem.model.NumberEntity
import ke.co.weetkit.collatzproblem.repository.FactorRepository
import ke.co.weetkit.collatzproblem.repository.NumberRepository

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.springframework.beans.factory.annotation.Autowired

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFutureCallback

import java.text.DecimalFormat
import java.time.Duration
import java.time.Instant


@Service
class SyncService: ISyncService {
    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, String>

    @Autowired
    lateinit var numberRepository: NumberRepository

    @Autowired
    lateinit var factorRepository: FactorRepository

    val decimalFormat = DecimalFormat("#,###")

    val logger: Logger = LoggerFactory.getLogger(SyncService::class.java)
    var completedThreads = 0
    val programStart: Instant = Instant.now()
    val threadCount = Runtime.getRuntime().availableProcessors()
    @Async
    override fun sync(from: Long, to: Long) {
        logger.info("Started: {}-{}", decimalFormat.format(from), decimalFormat.format(to))
        for (i in from..to) {
//            logger.info("Processing: {}", decimalFormat.format(i))
            produce(decimalFormat.format(i))
            val factors = mutableListOf<Long>()
            var temp = i
            while (temp > 0) {
                if (temp == 1L) {
                    factors.add(temp)
                    break
                } else {
                    temp = nextFactor(temp)
                    factors.add(temp)
                }
            }
        }
        val instantFinish   = Instant.now()
        completedThreads    = completedThreads.plus(1)
        if (completedThreads == threadCount){
            val executionTime = Duration.between(programStart, instantFinish).toMillis()
            logger.info("Completed: {} Threads Time: {}",completedThreads, convertTime(executionTime))
        }
    }

    @KafkaListener(topics = ["collatz"], groupId = "collatz")
    override fun consume(message: String) {
        logger.info("Kafka consumer msg: {}", message)
    }

    override fun produce(message: String) {
        val future = kafkaTemplate.send("collatz", message)

        future.addCallback(object : ListenableFutureCallback<SendResult<String?, String?>?> {
            override fun onSuccess(result: SendResult<String?, String?>?) {
                logger.info("Sent message=[{}] with offset=[{}]", message, result!!.recordMetadata.offset().toString())
            }

            override fun onFailure(ex: Throwable) {
                logger.error("Unable to send message=[{}] due to : {}", message,ex.message)
            }
        })
    }

    fun processFactors(numberEntity: NumberEntity?, num: Long, index: Long): Long {
        return if (num==1L){
            num
        } else{
            processFactors(numberEntity, nextFactor(num), index = index+1)
        }
    }

    fun nextFactor(num: Long): Long {
        return if ((num%2) == 0L)
            num/2
        else
            num.times(3L).plus(1L)
    }

    fun convertTime(duration: Long): String {
        val hours: Long     = duration/3_600_000L
        val minutes: Long   = (duration%3_600_000L)/60_000L
        val seconds: Long   = (duration%3_600_000L)%60_000L/1_000L
        val millis: Long    = ((duration%3_600_000L)%60_000L)%1000L

        return if (hours == 0L && minutes == 0L && seconds == 0L) {
            "${millis}ms"
        } else if(hours == 0L && minutes == 0L){
            "${seconds}s:${millis}ms"
        }else if (hours == 0L) {
            "${minutes}m:${seconds}s:${millis}ms"
        } else {
            "${hours}h:${minutes}m:${seconds}s:${millis}ms"
        }
    }
}