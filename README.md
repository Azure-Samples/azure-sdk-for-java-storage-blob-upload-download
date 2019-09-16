
# Quick Start with Azure Storage SDK V10 for Java

This QuickStart shows how to do some basic operations of Storage Blobs. 

> * Create a storage account using the Azure portal.
> * Create a container.
> * Upload a file to block blob.
> * List blobs.
> * Download a blob to file.
> * Delete a blob.
> * Delete the container.

# Folders introduction
Two folders are referred to different version of Azure SDK.
* storage-blobs-java-v10-quickstart-v3 referenced to following package in the Maven pom.xml file:
   <dependency>
     <groupId>com.microsoft.azure</groupId>
     <artifactId>azure-storage-blob</artifactId>
     <version>11.0.0</version>
   </dependency>
* storage-blobs-java-v10-quickstart-v4 referenced to following packages in the Maven pom.xml file:
   <dependency>
     <groupId>com.azure</groupId>
     <artifactId>azure-storage-blob</artifactId>
     <version>12.0.0-preview.3</version>
   </dependency>
    
   <dependency>
     <groupId>com.microsoft.azure</groupId>
     <artifactId>azure</artifactId>
     <version>1.22.0</version>
   </dependency>
  
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
