import com.blueanvil.kotka.Kotka
import com.blueanvil.kotka.KotkaConfig
import com.sun.net.httpserver.HttpServer
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.time.Duration

fun main(args: Array<String>) {
//    val kafka = Kotka(kafkaServers = "", config = KotkaConfig(
//        partitionCount = 2,
//        replicationFactor = 1,
//        consumerProps = mapOf("max.poll.records" to "1").toProperties(),
//        producerProps = mapOf("batch.size" to "1").toProperties(),
//        pollTimeout = Duration.ofMillis(100)
//    ))
//
//    val team = System.getenv()["HOSTNAME"]!!.substringAfterLast("-")
//    kafka.consumer(topic = "movielog$team", threads = 2, messageClass = Message::class) { message ->
//        // YOUR CODE GOES HERE
//    }

    HttpServer.create(InetSocketAddress(8082), 0).apply {
        createContext("/recommend") { http ->
            http.responseHeaders.add("Content-type", "text/plain")
            http.sendResponseHeaders(200, 0)
            PrintWriter(http.responseBody).use { out ->
                val userId = http.requestURI.path.substringAfterLast("/")
                println("Received recommendation request for user $userId")
                // ==================
                // YOUR CODE GOES HERE

                val reccomendations = (0..20).toList().shuffled().joinToString(", ")

                // ==================

                out.println(reccomendations)
                println("Recommended watchlist for user $userId: $reccomendations")
            }
        }

        start()
    }
}

data class Message(val name: String, val age: Int)