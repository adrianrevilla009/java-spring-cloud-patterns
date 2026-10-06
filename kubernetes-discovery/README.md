# kubernetes-discovery

Spring Cloud Kubernetes discovery app exposing `/services`, plus the RBAC manifest its pod needs.

## Goal

Let a pod find other Services through the Kubernetes API using `DiscoveryClient`, instead of running a registry such as Eureka.

## Run it

```
mvn -q -B compile
```

Expected: the build finishes with no errors. Not run end to end: no cluster was available, so the app was never started against one and the endpoints below were not called.

On a cluster (kind or minikube): build and load an image, run `kubectl apply -f k8s/rbac.yaml`, run a pod with `serviceAccountName: k8s-discovery`, then `curl <pod>:8080/services`. Cleanup: `kubectl delete -f k8s/rbac.yaml` and delete the pod.

## What it proves

- `K8sDiscoveryApp` serves `/services` (all service names) and `/services/{name}` (`host:port` of each instance) from `DiscoveryClient`.
- `k8s/rbac.yaml` defines a ServiceAccount, a Role with get/list/watch on services, endpoints, pods and endpointslices, and a RoleBinding.
- `application.properties` limits lookups to the pod's own namespace and turns off Kubernetes config and secrets.

Only the compile step is verified; runtime behaviour is untested.

## Trade-offs

- No extra registry to operate; Kubernetes stays the source of truth.
- The app is tied to the Kubernetes API and needs RBAC.
- Lookups through the API are heavier than a DNS name.

## When not to use it

- When a plain Service DNS name such as `http://orders:8080` is enough, which is the common case.
- When the app also runs outside Kubernetes.
