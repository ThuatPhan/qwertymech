Build all service jars with Maven (requires JDK 21):

```bash
mvn clean install -DskipTests
```

Each service's Docker image packages the jar from its `target/` folder, so this step must run before `docker-compose build`.