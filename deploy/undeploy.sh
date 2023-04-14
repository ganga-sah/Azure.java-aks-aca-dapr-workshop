set -x
#N="-n traffic-control"
#kubectl delete $N -k ./base 
kubectl delete -k ./overlays/minikube
