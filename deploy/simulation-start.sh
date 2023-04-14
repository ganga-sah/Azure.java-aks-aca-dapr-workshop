set -x
kubectl scale --replicas=1 deployment.apps/simulation $N

