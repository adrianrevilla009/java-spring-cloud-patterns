# java-spring-cloud-patterns

Eight small Spring Cloud examples, each in its own folder, showing how gateway routing, central config, service discovery, client-side load balancing, messaging and function packaging are wired around a tiny Orders domain.

## What is inside

| Folder | What it shows | Run |
| --- | --- | --- |
| [`gateway`](./gateway) | Spring Cloud Gateway route with path rewrite and request/response header filters | `mvn -q -B test` |
| [`config-server`](./config-server) | Config Server reading from a git repo and serving a new commit without a restart | `mvn -q -B test` |
| [`openfeign`](./openfeign) | Declarative OpenFeign client with a fallback when the remote service is down | `mvn -q -B test` |
| [`loadbalancer`](./loadbalancer) | `@LoadBalanced` client round-robining over two instances of a logical service id | `mvn -q -B test` |
| [`eureka`](./eureka) | Eureka registry server and an instance registered over its REST API | `mvn -q -B test` |
| [`kubernetes-discovery`](./kubernetes-discovery) | `DiscoveryClient` backed by the Kubernetes API, with the RBAC it needs | `mvn -q -B compile` |
| [`stream-kafka`](./stream-kafka) | A `Function` bound to Kafka topics through Spring Cloud Stream | `mvn -q -B test` |
| [`cloud-function`](./cloud-function) | A `Function` bean looked up by name from the Spring Cloud Function catalog | `mvn -q -B test` |

Each folder is a standalone Maven project. Versions are pinned: Spring Boot 3.3.5 and Spring Cloud 2023.0.3.

## Prerequisites

- Java 21 and Maven 3.8 or newer
- Docker, only to run a real Kafka broker for `stream-kafka`
- A Kubernetes cluster (kind or minikube), only to run `kubernetes-discovery` for real

## How to read it

Start with `gateway`, then `openfeign` and `loadbalancer`, which show the calling side of a service. `kubernetes-discovery` is the only folder that was not run against its real platform; its README says so.
