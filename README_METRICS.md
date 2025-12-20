Prometheus & Actuator setup

What I changed:
- Enabled Micrometer + Prometheus registry via pom.xml (dependencies already present).
- Exposed Actuator endpoints in `application.yml` (include: info, health, metrics, prometheus).
- Allowed unauthenticated access to `/actuator/**` in `SecurityConfig` so Prometheus can scrape the metrics.
- Added `MetricsConfig` bean that configures a common tag `application: football-team`.

How to test locally:
1. Start the application: `mvn -DskipTests spring-boot:run` from `backend` folder.
2. Visit http://localhost:8080/actuator/prometheus — you should see plain-text Prometheus metrics.
3. Add a Prometheus job to your prometheus.yml to scrape `http://host.docker.internal:8080/actuator/prometheus` or `http://localhost:8080/actuator/prometheus`.

Security note:
- Opening `/actuator/**` may expose internal information. For production, prefer to secure Actuator and limit access to the Prometheus IP or use basic auth for the scrape job.

