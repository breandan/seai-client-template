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

val team = InetAddress.getLocalHost().hostName.substringAfterLast("-")
val teamTopic = "movielog$team"
val kafkaServer = "fall2020-comp598.cs.mcgill.ca:9092"

fun main(args: Array<String>)  {
//  Choose one of the following two clients:
//    attachToKafkaServerUsingKotkaClient()
//    attachToKafkaServerUsingDefaultClient()

    startRecommendationService()
}

private fun startRecommendationService() {
    val port = 8082
    println("Starting server at ${InetAddress.getLocalHost().hostName}:${port}")

    HttpServer.create(InetSocketAddress(port), 0).apply {
        createContext("/recommend") { http ->
            http.responseHeaders.add("Content-type", "text/plain")
            http.sendResponseHeaders(200, 0)
            PrintWriter(http.responseBody).use { out -> handleRequest(http, out) }
        }

        start()
    }
}

private fun handleRequest(http: HttpExchange, out: PrintWriter) {
    val userId = http.requestURI.path.substringAfterLast("/")
    println("Received recommendation request for user $userId")

//    val userJson = URL("http://fall2020-comp598.cs.mcgill.ca:8080/user/$userId").readText()
//                println("User $userId JSON: $userJson")

    // ==================
    // YOUR CODE GOES HERE

    val recommendations = (0..20).toList().joinToString(",")

    // ==================

    out.println(recommendations)
    println("Recommended watchlist for user $userId: $recommendations")
}

// Documentation: https://kafka.apache.org/documentation/streams/
fun attachToKafkaServerUsingDefaultClient() {
    val props = Properties()
    props[APPLICATION_ID_CONFIG] = "seai-application"
    props[BOOTSTRAP_SERVERS_CONFIG] = kafkaServer
    props[DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String().javaClass
    props[DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String().javaClass

    val builder = StreamsBuilder()
    val textLines = builder.stream<String, String>("movielog4")
    textLines.print(Printed.toSysOut())

    val streams = KafkaStreams(builder.build(), props)
    streams.start()
}

// Documentation: https://github.com/blueanvil/kotka
fun attachToKafkaServerUsingKotkaClient() {
    val kafka = Kotka(
        kafkaServers = kafkaServer, config = KotkaConfig(
            partitionCount = 2,
            replicationFactor = 1,
            consumerProps = mapOf("max.poll.records" to "1").toProperties(),
            producerProps = mapOf("batch.size" to "1").toProperties(),
            pollTimeout = Duration.ofMillis(100)
        )
    )

    kafka.consumer(topic = teamTopic, threads = 2, messageClass = Message::class) { message ->
        // YOUR CODE GOES HERE
    }
}

data class Message(val name: String, val age: Int)