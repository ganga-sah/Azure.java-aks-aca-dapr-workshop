# 1. Replace traffic-control by NAMESPACE in dapr-appconfig-zipkin.yaml's endpointAddress: "http://zipkin.traffic-control.svc.cluster.local:9411/api/v2/spans"
# 2. Run license-key.sh with NAMESPACE

set -x
#N="-n traffic-control"
kubectl apply $N -k . 
