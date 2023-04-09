set -x
kubectl create secret generic license-key --from-file ./license-key
kubectl get secret -n dapr-traffic-control -o jsonpath="{.items[].data.license-key}" | base64 -d
