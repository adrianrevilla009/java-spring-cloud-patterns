# config-server

Spring Cloud Config Server backed by a git repository, with a test that commits to a throwaway repo.

## Goal

Serve application configuration from git and show that a new commit is visible on the next request without restarting the server.

## Run it

```
mvn -q -B test
```

Expected: `ConfigServerTest` passes. To run it by hand, point it at a git repo containing `orders.properties` on branch `main`:

```
CONFIG_GIT_URI=file:///path/to/repo mvn spring-boot:run
curl localhost:8888/orders/default
```

## What it proves

- `GET /orders/default` returns the content of `orders.properties`, here `"orders.maxItems":"10"`.
- After a second commit setting it to 25, the same request returns `"orders.maxItems":"25"`, because `force-pull=true` is set in `application.properties`.
- The test builds its git repo with JGit in a temp directory, so no remote is needed.

The client side is not covered: there is no client app, and `/actuator/refresh` on a `@RefreshScope` bean was not run.

## Trade-offs

- Git gives history, review and rollback for configuration.
- It adds one more service to run, and git becomes a runtime dependency.
- Secrets in git need encryption or Vault.
- `force-pull=true` fetches on every request, which is simple but slower than caching.

## When not to use it

- On Kubernetes, where ConfigMaps and Secrets already cover most needs without an extra server.
- For a single service whose configuration rarely changes.
