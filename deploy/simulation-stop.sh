set -x
kubectl scale --replicas=0 deployment.apps/simulation -n dapr-traffic-control

