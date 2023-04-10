set -x
#N="-n traffic-control"
#kubectl create secret generic license-key $N --from-file ./license-key
kubectl create secret generic license-key $N --from-literal=license-key=HX783-5PN1G-CRJ4A-K2L7V
kubectl get secret $N -o jsonpath="{.items[].data.license-key}" | base64 -d
