set -x
kubectl scale --replicas=0 deployment.apps/simulation -n traffic-control

