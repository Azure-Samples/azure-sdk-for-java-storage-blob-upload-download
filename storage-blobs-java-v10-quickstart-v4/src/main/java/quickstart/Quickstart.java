package quickstart;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.BlockBlobClient;
import com.azure.storage.blob.ContainerClient;
import com.azure.storage.blob.ContainerClientBuilder;
import com.azure.storage.common.credentials.SharedKeyCredential;

import com.microsoft.azure.management.resources.fluentcore.utils.SdkContext;

public class Quickstart {
    static File createTempFile() throws IOException {

        // Here we are creating a temporary file to use for download and upload to Blob storage
        File sampleFile = null;
        sampleFile = File.createTempFile("sampleFile", ".txt");
        System.out.println(">> Creating a sample file at: " + sampleFile.toString());
        Writer output = new BufferedWriter(new FileWriter(sampleFile));
        output.write("Hello Azure Storage blob quickstart!");
        output.close();

        return sampleFile;
    }

    public static void main(String[] args) throws IOException {
        ContainerClient containerClient;
        String endpoint = "<-Primary Blob Service Endpoint->";
        String containerName = SdkContext.randomResourceName("quickstart",20);
        String blobName = "<-upload to file->";
        String downloadFileName = SdkContext.randomResourceName("downloadFile", 25);
        // Creating a sample file to use in the sample
        File sampleFile = null;

        sampleFile = createTempFile();

        String downloadedFilePath = "<-download file path using downloadFileName (this file must not be exist)->";

        // Retrieve the credentials and initialize SharedKeyCredentials
        String accountName = System.getenv("AZURE_STORAGE_ACCOUNT");
        String accountKey = System.getenv("AZURE_STORAGE_ACCESS_KEY");

        System.out.print("\n AZURE_STORAGE_ACCOUNT : " + accountName);
        System.out.print("\n AZURE_STORAGE_ACCESS_KEY : " + accountKey);
        System.out.println();

        // Create a SharedKeyCredential 
        SharedKeyCredential credential = new SharedKeyCredential(accountName, accountKey);
       
        // Create a blobServiceClient
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
        .endpoint(endpoint)
        .credential(credential)
        .buildClient();

        // Create a containerClient
        containerClient = new ContainerClientBuilder()
        .endpoint(endpoint)
        .credential(credential)
        .containerName(containerName)
        .buildClient();

        // Create a container
        blobServiceClient.createContainer(containerName);
        System.out.print("Creating a container : " + containerClient.getContainerUrl());
        System.out.println();

        // Create a BlobClient to run operations on Blobs
        BlobClient blobClient = new BlobClientBuilder()
        .endpoint(endpoint)
        .credential(credential)
        .containerName(containerName)
        .blobName(blobName)
        .buildBlobClient();
 
        // Listening for commands from the console
        System.out.println("Enter a command");
        System.out.println("(P)utBlob-upload | (L)istBlobs | (G)etBlob-download | (D)eleteBlobs | (E)xitSample");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {

            System.out.println("# Enter a command : ");
            String input = reader.readLine();

            switch (input) {

            // Upload a blob from a File
            case "P":
                System.out.println("Uploading the sample file into the container from a file: " + containerClient.getContainerUrl());
                BlockBlobClient blockBlobClient = containerClient.getBlockBlobClient(blobName);
                blockBlobClient.uploadFromFile(sampleFile.toPath().toString());
                break;

            // List Blobs
            case "L":
                System.out.println("Listing blobs in the container: " + containerClient.getContainerUrl());
                containerClient.listBlobsFlat()
                        .forEach(blobItem -> System.out.println("This is the blob name: " + blobItem.name()));
                break;

            // Download a blob to local path   ?
            case "G":
                System.out.println("Get(Download) the blob: " + blobClient.getBlobUrl());
                blobClient.downloadToFile(downloadedFilePath);
                break;

            // Delete a blob
            case "D":
                System.out.println("Delete the blob: " + blobClient.getBlobUrl());
                blobClient.delete();
                System.out.println();
                break;

            // Exit 
            case "E":
                File downloadFile =new File(downloadedFilePath);
                System.out.println("Cleaning up the sample and exiting!");
                containerClient.delete();
                downloadFile.delete();
                System.exit(0);
                break;

            default:
                break;
            }
        }
    }
}
