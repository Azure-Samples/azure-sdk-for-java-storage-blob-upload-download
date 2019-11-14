# Quickstart with Azure Storage Blobs SDK V10 for Java

### Prerequisites

If you don't have an Azure subscription, create a [free account](https://azure.microsoft.com/free/?WT.mc_id=A261C142F) before you begin.

### Create a Storage Account using the Azure portal

Step 1 : Create a new general-purpose storage account to use for this tutorial.

*  Go to the [Azure Portal](https://portal.azure.com) and log in using your Azure account.
*  Select **New** > **Storage** > **Storage account**.
*  Select your Subscription.
*  For `Resource group`, create a new one and give it a unique name.
*  Enter a name for your storage account.
*  Select the `Location` to use for your Storage Account.
*  Set `Account kind` to **StorageV2(general purpose v2)**.
*  Set `Performance` to **Standard**.
*  Set `Replication` to **Locally-redundant storage (LRS)**.
*  Set `Secure transfer required` to **Disabled**.
*  Check **Review + create** and click **Create** to create your Storage Account.

Step 2 : Copy and save keys.

 * After your Storage Account is created. Click on it to open it. Select **Settings** > **Access keys** > **key1/key2**, Select a key and copy the **Key** to the clipboard, then paste it into text editor for later use.

### Set credentials in environment variables 

Linux
```
export AZURE_STORAGE_ACCOUNT="<youraccountname>"
export AZURE_STORAGE_ACCESS_KEY="<youraccountkey>"
```

Windows
```
setx AZURE_STORAGE_ACCOUNT "<youracountname>"
setx AZURE_STORAGE_ACCESS_KEY "<youraccountkey>"
```

At this point, you can run this application using maven: `mvn compile exec:java`. It creates its own file to upload and download, and then cleans up after itself by deleting everything at the end.

```
mvn compile exec:java
```

### This Quickstart shows how to do the following operations of Storage Blobs. 

> * Create a Storage Account using the Azure portal.
> * Create a container.
> * Upload a file to block blob.
> * List blobs.
> * Download a blob to file.
> * Delete a blob.
> * Delete the container.

### SDK Versions
* To use the latest Azure SDK version [storage-blobs-java-v10-quickstart-v4](./storage-blobs-java-v10-quickstart-v4) please add the following dependency to the Maven pom.xml file:
```xml
   <dependency>
     <groupId>com.azure</groupId>
     <artifactId>azure-storage-common</artifactId>
     <version>12.0.0</version>
   </dependency>

    <dependency>
     <groupId>com.azure</groupId>
     <artifactId>azure-storage-blob</artifactId>
     <version>12.0.0</version>
   </dependency>
```
* For the previous Azure SDK version [storage-blobs-java-v10-quickstart-v3](./storage-blobs-java-v10-quickstart-v3) please add the following dependency to the Maven pom please add the following dependencies to the Maven pom.xml file:
```xml
   <dependency>
     <groupId>com.microsoft.azure</groupId>
     <artifactId>azure-storage-blob</artifactId>
     <version>11.0.0</version>
   </dependency>
```

### Resources
* [Azure Storage SDK v10 for Java](https://github.com/azure/azure-storage-java)
* [API Reference](https://docs.microsoft.com/en-us/java/api/overview/azure/storage/blob?view=azure-java-preview)

### Contributing

This project welcomes contributions and suggestions.  Most contributions require you to agree to a
Contributor License Agreement (CLA) declaring that you have the right to, and actually do, grant us
the rights to use your contribution. For details, visit https://cla.microsoft.com.

When you submit a pull request, a CLA-bot will automatically determine whether you need to provide
a CLA and decorate the PR appropriately (e.g., label, comment). Simply follow the instructions
provided by the bot. You will only need to do this once across all repos using our CLA.

This project has adopted the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/).
For more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or
contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments.
