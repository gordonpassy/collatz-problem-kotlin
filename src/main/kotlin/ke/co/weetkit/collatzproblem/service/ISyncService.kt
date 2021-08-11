package ke.co.weetkit.collatzproblem.service

interface ISyncService {
    fun sync(from: Long, to: Long)
    fun consume(message: String)
    fun produce(message: String)
}