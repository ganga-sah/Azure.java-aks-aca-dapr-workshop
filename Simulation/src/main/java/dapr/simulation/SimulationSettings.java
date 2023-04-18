package dapr.simulation;

import io.dapr.client.DaprClientBuilder;
import io.dapr.client.DaprPreviewClient;
import io.dapr.client.domain.ConfigurationItem;
import io.dapr.client.domain.SubscribeConfigurationResponse;
import io.dapr.client.domain.UnsubscribeConfigurationResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.nonNull;

@ConstructorBinding
@ConfigurationProperties("simulation")
public class SimulationSettings {
    private int numLanes;
    private final DelaySettings entryDelay;
    private final DelaySettings exitDelay;
    private static final String DAPR_CONFIGURATON_STORE = "configstore";
    private static String CONFIGURATION_ITEM = "simulation.num-lanes";
    private static String subscriptionId = null;
    public SimulationSettings(final int numLanes,
                              final DelaySettings entryDelay,
                              final DelaySettings exitDelay) {
        this.numLanes = numLanes;
        this.entryDelay = entryDelay;
        this.exitDelay = exitDelay;
        getDaprConfigurationUpdateForNumLanes();
    }

    public int getNumLanes() {
        return numLanes;
    }

    public DelaySettings getEntryDelay() {
        return entryDelay;
    }

    public DelaySettings getExitDelay() {
        return exitDelay;
    }

    @ConstructorBinding
    static class DelaySettings {
        private final int minimum;
        private final int maximum;

        public DelaySettings(final int minimum, final int maximum) {
            this.minimum = minimum;
            this.maximum = maximum;
        }

        public int getMinimum() {
            return minimum;
        }

        public int getMaximum() {
            return maximum;
        }
    }

    private void getDaprConfigurationUpdateForNumLanes() {
        // Get config items from the config store
        try (DaprPreviewClient client = (new DaprClientBuilder()).buildPreviewClient()) {
            ConfigurationItem item = client.getConfiguration(DAPR_CONFIGURATON_STORE, CONFIGURATION_ITEM).block();
            this.numLanes = Integer.parseInt(item.getValue());
            System.out.println("Configuration for " + CONFIGURATION_ITEM + ": {'value':'" + this.numLanes + "'}");
        } catch (Exception e) {
            System.err.println("Could not get config item, err:" + e.getMessage());
        }

        try (DaprPreviewClient client = (new DaprClientBuilder()).buildPreviewClient()) {
            // Subscribe for config changes
            Flux<SubscribeConfigurationResponse> subscription = client.subscribeConfiguration(DAPR_CONFIGURATON_STORE,
                    List.of(CONFIGURATION_ITEM).toArray(String[]::new));

            // Read config changes for 20 seconds
            subscription.subscribe((response) -> {
                // First ever response contains the subscription id
                if (response.getItems() == null || response.getItems().isEmpty()) {
                    subscriptionId = response.getSubscriptionId();
                    System.out.println("App subscribed to config changes with subscription id: " + subscriptionId);
                } else {
                    var items = response.getItems();
                    this.numLanes = Integer.parseInt(items.get(CONFIGURATION_ITEM).getValue());
                    System.out.println("Configuration update for " + CONFIGURATION_ITEM + ": {'value':'" + this.numLanes + "'}");
                }
            });
            System.out.println("Waiting for 20 seconds for Configuration update");
            Thread.sleep(20000);
            // Unsubscribe from config changes
            UnsubscribeConfigurationResponse unsubscribe = client
                    .unsubscribeConfiguration(subscriptionId, DAPR_CONFIGURATON_STORE).block();
            if (unsubscribe.getIsUnsubscribed()) {
                System.out.println("App unsubscribed to config changes");
            } else {
                System.out.println("Error unsubscribing to config updates, err:" + unsubscribe.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error reading config updates, err:" + e.getMessage());
        }
    }
}
