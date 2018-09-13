# Quick Start with Azure Storage SDK V10 for Java
## How to run this project

### Prerequisites

If you don't have an Azure subscription, create a [free account](https://azure.microsoft.com/free/?WT.mc_id=A261C142F) before you begin.

### Create a storage account using the Azure portal

First, create a new general-purpose storage account to use for this tutorial. 

*  Go to the [Azure portal](https://portal.azure.com) and log in using your Azure account. 
*  On the Hub menu, select **New** > **Storage** > **Storage account - blob, file, table, queue**. 
*  Enter a name for your storage account. The name must be between 3 and 24 characters in length and may contain numbers and lowercase letters only. It must also be unique.
*  Set `Deployment model` to **Resource manager**.
*  Set `Account kind` to **General purpose**.
*  Set `Performance` to **Standard**. 
*  Set `Replication` to **Locally Redundant storage (LRS)**.
*  Set `Storage service encryption` to **Disabled**.
*  Set `Secure transfer required` to **Disabled**.
*  Select your subscription. 
*  For `resource group`, create a new one and give it a unique name. 
*  Select the `Location` to use for your storage account.
*  Check **Pin to dashboard** and click **Create** to create your storage account. 

After your storage account is created, it is pinned to the dashboard. Click on it to open it. Under SETTINGS, click **Access keys**. Select a key and copy the **key1** to the clipboard, then paste it into Notepad for later use.

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

## Resources
* [Azure Storage SDK v10 for Java](https://github.com/azure/azure-storage-java/tree/vNext)
* [API Reference](https://docs.microsoft.com/en-us/java/api/storage/client?view=azure-java-preview)

# Contributing

This project welcomes contributions and suggestions.  Most contributions require you to agree to a
Contributor License Agreement (CLA) declaring that you have the right to, and actually do, grant us
the rights to use your contribution. For details, visit https://cla.microsoft.com.

When you submit a pull request, a CLA-bot will automatically determine whether you need to provide
a CLA and decorate the PR appropriately (e.g., label, comment). Simply follow the instructions
provided by the bot. You will only need to do this once across all repos using our CLA.

This project has adopted the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/).
For more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or
contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments.

