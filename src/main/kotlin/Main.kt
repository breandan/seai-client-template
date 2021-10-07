import com.blueanvil.kotka.Kotka
import com.blueanvil.kotka.KotkaConfig
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig.*
import org.apache.kafka.streams.kstream.Printed
import java.io.PrintWriter
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.URL
import java.time.Duration
import java.util.*

val whoami = InetAddress.getLocalHost().hostName
val team = whoami.substringAfterLast("-")
val teamTopic = "movielog$team"
val kafkaServer = "fall2021-comp598.cs.mcgill.ca:9092"
val userService = "http://fall2021-comp598.cs.mcgill.ca:8080/user/"
val movieService = "http://fall2021-comp598.cs.mcgill.ca:8080/movie/"
val port = 8082

fun main(args: Array<String>) {
//  Choose one of the following two clients:
//  val client = attachToKafkaServerUsingKotkaClient()
    val stream = attachToKafkaServerUsingDefaultClient()
    val server = startRecommendationService()
}

internal fun startRecommendationService(): HttpServer =
    HttpServer.create(InetSocketAddress(port), 0).apply {
        createContext("/recommend") { http ->
            http.responseHeaders.add("Content-type", "text/plain")
            http.sendResponseHeaders(200, 0)
            PrintWriter(http.responseBody).use { out -> handleRequest(http, out) }
        }

        start().also { println("Starting recommendation service at $whoami:$port") }
    }

private fun getUserData(userId: Int) = URL(userService + userId).readText()
private fun getMovieData(userId: Int) = URL(movieService + userId).readText()

private fun handleRequest(http: HttpExchange, out: PrintWriter) {
    val userId = http.requestURI.path.substringAfterLast("/").toInt()
    println("Received recommendation request for user $userId")

//    val userJson = getUserData(userId)
//    println("User $userId JSON: $userJson")

    // ==================
    // YOUR CODE GOES HERE

    val recommendations = (1..20).toList().joinToString(",")

    // ==================

    out.println(recommendations)
    println("Recommended watchlist for user $userId: $recommendations")
}

// Documentation: https://kafka.apache.org/documentation/streams/
fun attachToKafkaServerUsingDefaultClient(): KafkaStreams {
    val props = Properties()
    props[APPLICATION_ID_CONFIG] = "seai-application"
    props[BOOTSTRAP_SERVERS_CONFIG] = kafkaServer
    props[DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String().javaClass
    props[DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String().javaClass

    val builder = StreamsBuilder()
    val textLines = builder.stream<String, String>(teamTopic)
    textLines.print(Printed.toSysOut())

    return KafkaStreams(builder.build(), props).also { it.start() }
}

// Documentation: https://github.com/blueanvil/kotka
fun attachToKafkaServerUsingKotkaClient(): Kotka =
    Kotka(
        kafkaServers = kafkaServer, config = KotkaConfig(
            partitionCount = 2,
            replicationFactor = 1,
            consumerProps = mapOf("max.poll.records" to "1").toProperties(),
            producerProps = mapOf("batch.size" to "1").toProperties(),
            pollTimeout = Duration.ofMillis(100)
        )
    ).apply {
        consumer(
            topic = teamTopic,
            threads = 2,
            messageClass = Message::class
        ) { message ->
            // YOUR CODE GOES HERE
        }
    }

data class Message(val name: String, val age: Int)