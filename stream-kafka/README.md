# stream-kafka

A `Function<String,String>` bound to Kafka topics with Spring Cloud Stream, a test on the in-memory binder, and a single-node Kafka compose file.

## Goal

Write order processing as a plain function and let Spring Cloud Stream connect it to the `orders` input topic and the `orders-confirmed` output topic.

## Run it

```
mvn -q -B test
```

Expected: `StreamTest` passes. The test uses the in-memory test binder, so it needs no Docker.

Not run end to end: the real broker path was not started here. To try it, run `docker compose up -d`, then `mvn spring-boot:run`, then produce a message to `orders` and read `orders-confirmed`. Cleanup: `docker compose down -v`.

## What it proves

- `processOrder` in `StreamApp.java` is bound to `orders` (group `order-processor`) and `orders-confirmed` by names in `application.properties`.
- In `StreamTest`, sending `book` to `orders` yields `CONFIRMED:BOOK` on `orders-confirmed`.
- `compose.yaml` runs `apache/kafka:3.8.0` as one KRaft node on port 9092.

This covers the function and binding names only, not partitions, rebalancing or offsets.

## Trade-offs

- Business code stays broker-agnostic, at the price of learning the `<function>-in-0` naming convention.
- There is less direct control than with the Kafka client API.
- The test binder behaves differently from Kafka in ordering and delivery guarantees.

## When not to use it

- When you need Kafka-specific features such as transactions or Kafka Streams topologies; use `spring-kafka` directly.
- When a service only ever publishes or consumes one topic with simple code.
