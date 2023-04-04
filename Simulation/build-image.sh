set -x
REPO=geepsah
SERVICE=simulation
mvn spring-boot:build-image
docker tag ${SERVICE}:1.0-SNAPSHOT ${REPO}/${SERVICE}:latest
docker push ${REPO}/${SERVICE}:latest
