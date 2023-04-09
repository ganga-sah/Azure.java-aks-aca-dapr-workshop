package dapr.fines.fines;

import java.util.Map;

import finefines.FineFines;
import io.dapr.client.DaprClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaprFineCalculator implements FineCalculator {
    private static final Logger log = LoggerFactory.getLogger(DaprFineCalculator.class);
    private /*final*/ String fineCalculatorLicenseKey; //TODO: restore final
    private final FineFines fineFines;

    public DaprFineCalculator(final DaprClient daprClient) {
        if (daprClient == null) {
            throw new IllegalArgumentException("daprClient");
        }
        try {
            final Map<String, String> licenseKeySecret = daprClient.getSecret("secretstore", "license-key").block();
            if (licenseKeySecret == null || licenseKeySecret.isEmpty()) {
                throw new RuntimeException("'license-key' is not part of the secret store.");
            }
            this.fineCalculatorLicenseKey = licenseKeySecret.get("license-key");
        } catch (Exception e) {
            this.fineCalculatorLicenseKey = null;
            log.error("Error in licenseKeySecret:", e);
        }
        this.fineFines = new FineFines();
    }

    @Override
    public int calculateFine(final int excessSpeed) {
        return fineFines.calculateFine(this.fineCalculatorLicenseKey, excessSpeed);
    }
}
