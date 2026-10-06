# loadbalancer

A `@LoadBalanced` `RestClient` that calls the logical service id `orders`, resolved from a static instance list.

## Goal

Show client-side load balancing: the caller uses `http://orders` and Spring Cloud LoadBalancer picks an instance, with no registry involved.

## Run it

```
mvn -q -B test
```

Expected: `LbTest` passes. By hand, start two copies on ports 8081 and 8082 (`--server.port=8081`, `--server.port=8082`) and a third on 8080, then `curl localhost:8080/call` repeatedly: the answer alternates between `8081` and `8082`.

## What it proves

- `application.properties` lists two instances of `orders` using the simple discovery client.
- `LbTest` starts two apps on ports 18091 and 18092, calls `/call` on the first four times, and sees both `18091` and `18092` returned by `/whoami`.
- `LbApp` builds its client with `baseUrl("http://orders")`; the host is never a real DNS name.

## Trade-offs

- A static list is trivial to run but does not follow instances coming and going; use Eureka or Kubernetes discovery for that.
- Round robin ignores load and instance health.
- Each caller balances on its own, so there is no central view of traffic.

## When not to use it

- When a server-side load balancer or Kubernetes Service already spreads traffic.
- When there is only one instance of the target.
