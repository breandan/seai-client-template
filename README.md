# Client Template for SEAI Project

## Instructions

1. Make sure you are connected to the [CS VPN](https://www.cs.mcgill.ca/docs/remote/dynamic/) (e.g. `ssh -D 9000 [username]@ubuntu.cs.mcgill.ca`).
2. Check to see that you can access the Movie API service, [`http://fall2020-comp598.cs.mcgill.ca:8080`](http://fall2020-comp598.cs.mcgill.ca:8080). (You should see "Movie API Service".)
3. Try out some REST requests and replies (`/user/[USER_ID]`, `/movie/[MOVIE_ID]`)
4. Fork and clone this template. This fork will reside on your team's shared repository.
5. Clone your fork onto your local development environment.
6. Log onto your team's VM with the team credentials (i.e. `ssh team[TEAM_NUMBER]@fall2020-comp598-[TEAM_NUMBER].cs.mcgill.ca`). This requires you to have first sent us your public key.
7. Clone your fork onto the remote development environment.
8. Run `./gradlew run` from inside the project directory.
9. Check that your service is running by visiting [http://fall2020-comp598-[TEAM_NUMBER].cs.mcgill.ca:8082/recommend/[USER_ID]](http://fall2020-comp598-[TEAM_NUMBER].cs.mcgill.ca:8082/recommend/[USER_ID]). This should return a comma-separated list of movie recommendations.
10. Check the Kafka log to see that the replies are being received.
11. Open the parent project using your favorite IDE.
12. Update the code in [Main.kt](/src/main/kotlin/Main.kt).
13. Deploy the changes to your VM, and repeat steps 6-11.

## Kafka

There are many different [Kafka clients](https://docs.confluent.io/current/clients/index.html) you can use to read and write to Kafka. You are free to use any libraries you wish. We have included two in this template:

* [Apache Kafka Streams](https://kafka.apache.org/documentation/streams/) (the official client)
* [Kotka](https://github.com/blueanvil/kotka/) (a lightweight Kotlin client)

To read from Kafka, you will need to connect to the Kafka server at `fall2020-comp598.cs.mcgill.ca:9092` and stream from the topic `movielog[TEAM_NUMBER]`. The code for doing so is included in [`Main.kt`](/src/main/kotlin/Main.kt).

## REST API

You will need to access the user and movie services to access the corresponding features. There are many libraries for doing so, I recommend using [OkHttp](https://github.com/square/okhttp) if you are using the Kotlin template - the dependency is included, but you will need to implement the JSON data bindings yourself.

### User service (example)

http://fall2020-comp598.cs.mcgill.ca:8080/user/12

`{"user_id":12,"age":27,"occupation":"college/grad student","gender":"M"}`

### Movie service (example)

http://fall2020-comp598.cs.mcgill.ca:8080/movie/12

Reply: `{"id":"dracula+dead+and+loving+it+1995","tmdb_id":12110,"imdb_id":"tt0112896","title":"Dracula: Dead and Loving It","original_title":"Dracula: Dead and Loving It","adult":"False","belongs_to_collection":{},"budget":"0","genres":[{"id":35,"name":"Comedy"},{"id":27,"name":"Horror"}],"homepage":"null","original_language":"en","overview":"When a lawyer shows up at the vampire's doorstep, he falls prey to his charms and joins him in his search for fresh blood. Enter Dr. van Helsing, who may be the only one able to vanquish the count.","popularity":"5.430331","poster_path":"/xve4cgfYItnOhtzLYoTwTVy5FGr.jpg","production_companies":[{"name":"Columbia Pictures","id":5},{"name":"Castle Rock Entertainment","id":97},{"name":"Enigma Pictures","id":6368}],"production_countries":[{"iso_3166_1":"FR","name":"France"},{"iso_3166_1":"US","name":"United States of America"}],"release_date":"1995-12-22","revenue":"0","runtime":88,"spoken_languages":[{"iso_639_1":"en","name":"English"},{"iso_639_1":"de","name":"Deutsch"}],"status":"Released","vote_average":"5.7","vote_count":"210"}`
