# 1. Replace traffic-control by NAMESPACE in dapr-appconfig-zipkin.yaml's endpointAddress: "http://zipkin.traffic-control.svc.cluster.local:9411/api/v2/spans"

set -x
#kubectl apply $N -k ./base 
kubectl apply -k ./overlays/minikube
