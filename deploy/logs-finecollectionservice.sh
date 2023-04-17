set -x
kubectl logs -n dapr-traffic-control -lapp=finecollectionservice -f
