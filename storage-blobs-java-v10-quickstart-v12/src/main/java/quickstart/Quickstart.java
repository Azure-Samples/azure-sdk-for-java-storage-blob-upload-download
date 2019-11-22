package quickstart;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;

public class Quickstart {
    static File createTempFile() throws IOException {

        // Here we are creating a temporary file to use for download and upload to Blob
        // storage
        File sampleFile = null;
        sampleFile = File.createTempFile("sampleFile", ".txt");
        System.out.println(">> Creating a sample file at: " + sampleFile.toString());
        Writer output = new BufferedWriter(new FileWriter(sampleFile));
        output.write("Hello Azure Storage blob quickstart.");
        output.close();

        return sampleFile;
    }

    public static void main(String[] args) throws IOException {

        // Creating a sample file to use in the sample
        File sampleFile = null;
        sampleFile = createTempFile();
        String downloadedFilePath = "downloadedFile.txt";

        // Retrieve the credentials and initialize SharedKeyCredentials
        String accountName = System.getenv("AZURE_STORAGE_ACCOUNT");
        String accountKey = System.getenv("AZURE_STORAGE_ACCESS_KEY");
        String endpoint = "https://" + accountName + ".blob.core.windows.net";
        String containerName = "mycontainer";
        String blobName = "myblob";

        // Create a SharedKeyCredential
        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, accountKey);
       
        // Create a blobServiceClient
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
            .endpoint(endpoint)
            .credential(credential)
            .buildClient();

        // Create a containerClient
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);

        // Create a container
        blobServiceClient.createBlobContainer(containerName);
        System.out.printf("Creating a container : %s %n", blobContainerClient.getBlobContainerUrl());

        // Create a BlobClient to run operations on Blobs
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);

        // Listening for commands from the console
        System.out.println("Enter a command");
        System.out.println("(U)Upload Blob | (L)List Blobs | (G)Get Blob | (D)Delete Blobs | (E)Exit");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {

            System.out.println("# Enter a command : ");
            String input = reader.readLine();

            switch (input) {

            // Upload a blob from a File
            case "U":
                System.out.println("Uploading the sample file into the container from a file: "
                        + blobContainerClient.getBlobContainerUrl());
                blobClient.uploadFromFile(sampleFile.toPath().toString());
                break;

            // List Blobs
            case "L":
                System.out.println("Listing blobs in the container: " + blobContainerClient.getBlobContainerUrl());
                blobContainerClient.listBlobs()
                        .forEach(
                            blobItem -> System.out.println("This is the blob name: " + blobItem.getName()));
                break;

            // Download a blob to local path
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
                File downloadFile = new File(downloadedFilePath);
                System.out.println("Cleaning up the sample and exiting.");
                blobContainerClient.delete();
                downloadFile.delete();
                System.exit(0);
                break;

            default:
                break;
            }
        }
    }
}
