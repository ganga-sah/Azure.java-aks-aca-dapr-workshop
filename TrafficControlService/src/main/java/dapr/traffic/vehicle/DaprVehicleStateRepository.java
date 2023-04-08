package dapr.traffic.vehicle;

import dapr.traffic.TrafficController;
import io.dapr.client.DaprClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class DaprVehicleStateRepository implements VehicleStateRepository {

    private static final Logger log = LoggerFactory.getLogger(TrafficController.class);
    private static final String DAPR_STORE_NAME = "statestore";

    private final DaprClient daprClient;

    public DaprVehicleStateRepository(final DaprClient daprClient) {
        this.daprClient = daprClient;
    }

    @Override
    public VehicleState saveVehicleState(VehicleState vehicleState) {
        daprClient.saveState(DAPR_STORE_NAME, vehicleState.licenseNumber(), vehicleState).block();
        Mono<io.dapr.client.domain.State<VehicleState>> result = daprClient.getState(DAPR_STORE_NAME, vehicleState.licenseNumber(), VehicleState.class);
        log.info("result", result);
        return result.block().getValue();
    }

    @Override
    public Optional<VehicleState> getVehicleState(String licenseNumber) {
    	Mono<io.dapr.client.domain.State<VehicleState>> result = daprClient.getState(DAPR_STORE_NAME, licenseNumber, VehicleState.class);
        log.info("result", result);
        return Optional.ofNullable(result.block().getValue());
    }
}
