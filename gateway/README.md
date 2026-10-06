# gateway

Spring Cloud Gateway with one Java DSL route, plus a stand-in orders endpoint inside the same app.

## Goal

Show a gateway route matched by path, with a path rewrite and header filters on the way in and out. The backend is an endpoint in the same app (`/internal/orders`), so no second process is needed.

## Run it

```
mvn -q -B test
```

Expected: `GatewayTest` passes (the build prints no failures). To try it by hand, run `mvn spring-boot:run` and `curl -i localhost:8080/api/orders`: the response is `[{"id":1,"sku":"book"}]` with an `X-Lab-Route: orders` header.

## What it proves

- `GatewayApp.routes()` matches `/api/orders` and rewrites it to `/internal/orders`, then proxies to `lab.orders-uri` from `application.properties`.
- The route adds `X-Lab-Caller: gateway` to the request and `X-Lab-Route: orders` to the response.
- `GatewayTest` starts the gateway on port 18080 and asserts status 200, the response header and that the body contains `book`.

## Trade-offs

- Java DSL routes are type-safe and testable, but changing one needs a rebuild; property-based routes can come from Config Server instead.
- The gateway is reactive (WebFlux), so blocking filters hurt throughput.
- The test uses a fixed port, so two copies cannot run at the same time.

## When not to use it

- When a single service is exposed directly and no routing is needed.
- When a service mesh or cloud load balancer already does routing and auth at the edge.
