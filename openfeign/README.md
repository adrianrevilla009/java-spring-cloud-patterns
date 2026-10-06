# openfeign

A declarative OpenFeign `InventoryClient` with a fallback, called from an Orders endpoint in the same app.

## Goal

Show an HTTP client declared as an interface, and how its fallback keeps the caller answering when the remote service is unreachable.

## Run it

```
mvn -q -B test
```

Expected: `FeignTest` and `FeignFallbackTest` pass. By hand, `mvn spring-boot:run` then `curl localhost:8080/orders/book/stock` returns `7`, because the app points the client at its own `/stock/{sku}` endpoint.

## What it proves

- `FeignTest` calls `/orders/book/stock` and gets `7` from the remote `/stock/{sku}` endpoint over HTTP.
- `FeignFallbackTest` points `lab.inventory-url` at `http://localhost:1`, where nothing listens, and still gets `-1` from `InventoryFallback`.
- `spring.cloud.openfeign.circuitbreaker.enabled=true` plus 500 ms connect and 1000 ms read timeouts are set in `application.properties`.

The test covers a refused connection only; a slow response hitting the read timeout was not tested.

## Trade-offs

- A magic value like `-1` in the fallback must be understood by every caller.
- Fallbacks hide outages from callers, so they need metrics or logs alongside.
- Feign is blocking; it suits servlet apps, not reactive ones.

## When not to use it

- When only one or two simple calls are made, where `RestClient` is less machinery.
- When the fallback would have to invent business data that cannot safely be guessed.
