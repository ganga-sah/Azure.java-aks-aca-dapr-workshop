set -x
REPO=geepsah
SERVICE=vehicle-registration-service
mvn spring-boot:build-image
docker tag ${SERVICE}:1.0-SNAPSHOT ${REPO}/${SERVICE}:latest
docker push ${REPO}/${SERVICE}:latest
