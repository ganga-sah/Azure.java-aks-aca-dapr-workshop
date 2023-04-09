set -x
kubectl create secret generic license-key -n traffic-control --from-file ./license-key
kubectl get secret -n traffic-control -o jsonpath="{.items[].data.license-key}" | base64 -d
