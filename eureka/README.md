# eureka

A standalone Eureka registry server and a test that registers an instance over its REST API.

## Goal

Show a Netflix Eureka service registry running as a Spring Boot app, and how an instance appears in it once registered.

## Run it

```
mvn -q -B test
```

Expected: `EurekaTest` passes. An `ERROR ... Cannot clean connections` line from the Eureka client may print while the context shuts down; it did here and the test still passed. By hand, `mvn spring-boot:run` and open `http://localhost:8761`.

## What it proves

- `EurekaApp` with `@EnableEurekaServer` starts on port 8761 (18761 in the test) and does not register with itself or fetch a registry (`application.properties`).
- `EurekaTest` POSTs an instance named `DEMO` to `/eureka/apps/DEMO`, then reads it back with `"instanceId":"demo-1"` and `"status":"UP"`.
- Self-preservation is switched off, so the registry evicts dead instances normally in a small lab.

No real client app registered through the Eureka client library, and no peer-aware cluster was run.

## Trade-offs

- A registry is another component to run and make highly available.
- Clients cache the registry, so views are eventually consistent.
- Turning self-preservation off is fine for a lab but risky in production during network partitions.

## When not to use it

- On Kubernetes, where Services and `kubernetes-discovery` already give you discovery.
- For a handful of fixed services that a static list can cover.
