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
            log.info("Try1: fineCalculatorLicenseKey="+fineCalculatorLicenseKey);
        } catch (Exception e) {
            // Retry
            try {
                log.error("Sleeping for 10 seconds");
                Thread.sleep(10000); // TODO: Optimize this in a retry sleep interval of say 5 seconds
            } catch (InterruptedException ie) {
            }
            try {
                final Map<String, String> licenseKeySecret = daprClient.getSecret("secretstore", "license-key").block();
                if (licenseKeySecret == null || licenseKeySecret.isEmpty()) {
                    throw new RuntimeException("'license-key' is not part of the secret store.");
                }
                this.fineCalculatorLicenseKey = licenseKeySecret.get("license-key");
                log.info("Try2: fineCalculatorLicenseKey="+fineCalculatorLicenseKey);
            } catch (Exception e2) {
                this.fineCalculatorLicenseKey = null;
                log.error("Error in licenseKeySecret:", e2);
            }
        }
        this.fineFines = new FineFines();
    }

    @Override
    public int calculateFine(final int excessSpeed) {
        return fineFines.calculateFine(this.fineCalculatorLicenseKey, excessSpeed);
    }
}
