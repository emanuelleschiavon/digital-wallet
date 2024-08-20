### Building

```
./gradlew clean build
```


### Running

First, run the dependency database with command:
```
docker-compose up -d postgres
```
After this, run the application:
```
./gradlew bootRun
```
