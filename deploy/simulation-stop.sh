set -x
#N="-n traffic-control"
kubectl scale --replicas=0 deployment.apps/simulation $N

