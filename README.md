# Mini Redis

MiniRedis Ktor project running as an application inside [Docker](https://www.docker.com/).

## Running

To build and run this application with Docker, execute the following commands:

```bash
./gradlew installDist
docker build -t mini-redis .
docker run -p 8080:8080 mini-redis
```

Then, navigate to [http://localhost:8080/](http://localhost:8080/)