---
page_type: sample
languages: 
- java
products:
- azure
- azure-storage
description: "This sample shows you how to upload and download blobs from Azure Blob Storage with Java."
urlFragment: upload-download-blobs-java
---

# How to upload and download blobs from Azure Blob Storage with Java

### SDK Versions
In this sample, you will find the following folders:
* **[azure-sdk-for-java-storage-blob-upload-download-v11][azure-sdk-blob-v11-folder]** - references [Storage Blob SDK v11.0.0][SDK-v11]
* **[azure-sdk-for-java-storage-blob-upload-download-v12][azure-sdk-blob-v12-folder]** - references [Storage Blob SDK v12.0.0][SDK-v12]
### Prerequisites

If you don't have an Azure subscription, create a [free account][free account] before you begin.

### Create a Storage Account using the Azure portal

Step 1 : Create a new general-purpose storage account to use for this tutorial.

*  Go to the [Azure Portal][Azure Portal] and log in using your Azure account.
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
export AZURE_STORAGE_ACCOUNT="<YourAccountName>"
export AZURE_STORAGE_ACCESS_KEY="<YourAccountKey>"
```

Windows
```
setx AZURE_STORAGE_ACCOUNT "<YourAccountName>"
setx AZURE_STORAGE_ACCESS_KEY "<YourAccountKey>"
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

### Resources

* [Azure Storage Blob SDK v11 for Java][SDK-v11]
* [Azure Storage Blob SDK v12 for Java][SDK-v12]
* [API Reference][API Reference]

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

<!-- LINKS -->
[azure-sdk-blob-v11-folder]: https://github.com/Azure-Samples/azure-sdk-for-java-storage-blob-upload-download/tree/master/azure-sdk-for-java-storage-blob-upload-download-v11
[azure-sdk-blob-v12-folder]: https://github.com/Azure-Samples/azure-sdk-for-java-storage-blob-upload-download/tree/master/azure-sdk-for-java-storage-blob-upload-download-v12
[free account]: https://azure.microsoft.com/free/?WT.mc_id=A261C142F
[Azure Portal]: https://portal.azure.com
[SDK-v11]: https://search.maven.org/artifact/com.microsoft.azure/azure-storage-blob/11.0.0/jar
[SDK-v12]: https://search.maven.org/artifact/com.azure/azure-storage-blob/12.0.0/jar
[API Reference]: http://azure.github.io/azure-sdk-for-java/