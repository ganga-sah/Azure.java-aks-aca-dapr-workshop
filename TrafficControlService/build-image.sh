set -x
REPO=geepsah
SERVICE=traffic-control-service
mvn spring-boot:build-image
docker tag ${SERVICE}:1.0-SNAPSHOT ${REPO}/${SERVICE}:latest
docker push ${REPO}/${SERVICE}:latest
