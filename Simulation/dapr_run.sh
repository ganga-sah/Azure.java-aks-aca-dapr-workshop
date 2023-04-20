set -x
dapr run --app-id simulationservice --app-port 8080 --dapr-http-port 3603 --dapr-grpc-port 60003 --resources-path ../dapr/components mvn spring-boot:run
#dapr run --app-id simulationservice --app-port 8080 --resources-path ../dapr/components mvn spring-boot:run

