---
title: Reference a secret in components
parent: Use Azure Keyvault as a secret store
grand_parent: Bonus Assignments
has_children: false
nav_order: 3
layout: default
has_toc: true
---

# Reference a secret in components

{: .no_toc }

<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
- TOC
{:toc}
</details>

Previously, you have created an Azure Key Vault and added the Dapr component. Now, you will use the secret in the application. This bonus assignment is about using Azure Key Vault as a [secret store](https://docs.dapr.io/operations/components/setup-secret-store/) to store the connection string of the Azure Service Bus.

{: .important-title }
> Pre-requisite
>
> If the setup of the Azure Key Vault is not done yet, please follow the instructions in [Part 1 - Setup Azure Key Vault as a secret store]({{ site.baseurl }}{% link modules/09-bonus-assignments/03-secret-store/1-azure-key-vault-secret-store-setup.md %}).
>
> The `Assignment 3 - Setup Azure Service Bus` is also a pre-requisite for this assignment. If not done yet, please follow the instructions in [Assignment 3 - Setup Azure Service Bus]({{ site.baseurl }}{% link modules/03-assignment-3-azure-pub-sub/1-azure-service-bus.md %}).


## Step 1: Create a secret in the Azure Key Vault for the connetion string

Azure Service Bus' connection string will be store as a string/literal secret:

1. Open a terminal window.
   
1. Create a secret in the Azure Key Vault for Azure Service Bus' connection string:
    ```bash
    az keyvault secret set --vault-name $KEY_VAULT --name azSericeBusconnectionString --value "<connection-string>"
    ```
    Replace `<connection-string>` with the connection string of the Azure Service Bus created in assignement 3.

## Step 2: Use the secret in the application `FineCollectionService`

1. Open the file `dapr/components/azure-servicebus-pubsub.yaml` (created in assignment 3) in your code editor, and inspect it.

1. **Replace** value:

    ```yaml
    value: "Endpoint=sb://{ServiceBusNamespace}.servicebus.windows.net/;SharedAccessKeyName={PolicyName};SharedAccessKey={Key};EntityPath={ServiceBus}"
    ```
    with:

    ```yaml
    secretKeyRef:
      name: azSericeBusconnectionString
      key: azSericeBusconnectionString
    ```
    When the secret is a string/literal, the `key` is the same as the `name` of the secret, see [How-To: Reference secrets in components](https://docs.dapr.io/operations/components/component-secrets/).

1. **Add** the following lines before `scopes:`:
    
    ```yaml
    auth:
      secretStore: secretstore
    ```
    This tells Dapr to use the secret store component `secretstore` to retrieve the secret.


## Step 3: Test the application

You're going to start all the services now. 

1. Make sure no services from previous tests are running (close the command-shell windows).

1. Open the terminal window and make sure the current folder is `VehicleRegistrationService`.

1. Enter the following command to run the VehicleRegistrationService with a Dapr sidecar:

   ```bash
   mvn spring-boot:run
   ```

1. Open a **new** terminal window and change the current folder to `FineCollectionService`.

1. Enter the following command to run the FineCollectionService with a Dapr sidecar:
   
    * Ensure you have run `dapr init` command prior to running the below command

    ```bash
    dapr run --app-id finecollectionservice --app-port 6001 --dapr-http-port 3601 --dapr-grpc-port 60001 --components-path ../dapr/components mvn spring-boot:run
    ```

1. Open a **new** terminal window and change the current folder to `TrafficControlService`.

1. Enter the following command to run the TrafficControlService with a Dapr sidecar:

   ```bash
   dapr run --app-id trafficcontrolservice --app-port 6000 --dapr-http-port 3600 --dapr-grpc-port 60000 --components-path ../dapr/components mvn spring-boot:run
   ```

1. Open a **new** terminal window and change the current folder to `Simulation`.

1. Start the simulation:

   ```bash
   mvn spring-boot:run
   ```

You should see the same logs as **Assignment 3** with Azure Service Bus. Obviously, the behavior of the application is exactly the same as before.

{: .important-title }
> Cleanup
>
> When the workshop is done, please follow the [cleanup instructions]({{ site.baseurl }}{% link modules/10-cleanup/index.md %}) to delete the resources created in this workshop.
> 

<span class="fs-3">
[< Secret Store setup]({{ site.baseurl }}{% link modules/09-bonus-assignments/03-secret-store/1-azure-key-vault-secret-store-setup.md %}){: .btn .mt-7 }
</span>