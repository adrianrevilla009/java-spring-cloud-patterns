# cloud-function

One `priceOrder` function bean and a test that finds it through the Spring Cloud Function catalog.

## Goal

Write business logic once as a `java.util.function.Function` and look it up by name, independent of how it is later exposed over HTTP, Stream or a serverless adapter.

## Run it

```
mvn -q -B test
```

Expected: `FunctionTest` passes with no failures printed.

Not run end to end: no HTTP, Stream or serverless adapter is included, so the function was only invoked through the catalog.

## What it proves

- `FunctionApp.priceOrder()` returns the SKU plus ten times its length.
- `FunctionTest` calls `catalog.lookup(Function.class, "priceOrder")` and `apply("book")` gives `book:40`.
- The function has no web or messaging code in it, so a different adapter dependency could expose the same bean.

## Trade-offs

- The logic is portable and easy to unit test, with one programming model across runtimes.
- It adds an abstraction layer, and each cloud needs its own packaging (shaded jar, handler settings).
- JVM cold starts are a cost on serverless platforms.

## When not to use it

- For a long-running web app that will never run as a function, where a plain `@RestController` is clearer.
- When the target platform has a native SDK you want to use directly.
