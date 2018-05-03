package quickstart;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;

import com.microsoft.azure.storage.blob.BlobRange;
import com.microsoft.azure.storage.blob.BlockBlobURL;
import com.microsoft.azure.storage.blob.ContainerURL;
import com.microsoft.azure.storage.blob.ListBlobsOptions;
import com.microsoft.azure.storage.blob.ICredentials;
import com.microsoft.azure.storage.blob.PipelineOptions;
import com.microsoft.azure.storage.blob.ServiceURL;
import com.microsoft.azure.storage.blob.SharedKeyCredentials;
import com.microsoft.azure.storage.blob.StorageURL;
import com.microsoft.azure.storage.blob.TransferManager;
import com.microsoft.azure.storage.blob.models.Blob;
import com.microsoft.azure.storage.blob.models.ContainersListBlobFlatSegmentResponse;
import com.microsoft.rest.v2.RestException;
import com.microsoft.rest.v2.util.FlowableUtil;


import io.reactivex.Flowable;

import io.reactivex.*;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Function;


public class Quickstart {
    static File createTempFile() {
        // Here we are creating a temporary file to use for download and upload to Blob storage
        File sampleFile = null;
        try {
            sampleFile = File.createTempFile("sampleFile", ".txt");
            System.out.println(">> Creating a sample file at: " + sampleFile.toString());
            Writer output = new BufferedWriter(new FileWriter(sampleFile));
            output.write("Hello Azure!");
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sampleFile;
    }

    static void uploadFile(BlockBlobURL blob, File sourceFile) {
        try {
            // Read the file asynchronously into a Flowable
            FileChannel fileChannel = FileChannel.open(sourceFile.toPath());
            //Uploading file to the blobURL
            TransferManager.uploadFileToBlockBlob(fileChannel, blob,(int) sourceFile.length(), null).blockingGet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void listBlobs(ContainerURL containerURL) {
        // Each ContainerURL.listBlobs call return up to maxResults (maxResults=10 passed into ListBlobOptions below).
        // To list all Blobs, we are creating a helper static method called listAllBlobs
        ListBlobsOptions options = new ListBlobsOptions(null, null, 1);

        containerURL.listBlobsFlatSegment(null, options).flatMap(containersListBlobFlatSegmentResponse -> 
            listAllBlobs(containerURL, containersListBlobFlatSegmentResponse)            
            )

            /*
            This will synchronize all the above operations. This is strongly discouraged for use in production as
            it eliminates the benefits of asynchronous IO. We use it here to enable the sample to complete and
            demonstrate its effectiveness.
            */
            .blockingGet();
    }

    private static Single <ContainersListBlobFlatSegmentResponse> listAllBlobs(ContainerURL url, ContainersListBlobFlatSegmentResponse response) {                
        // Process the blobs returned in this result segment (if the segment is empty, blobs() will be null.
        if (response.body().blobs() != null) {
            for (Blob b : response.body().blobs().blob()) {
                String output = "Blob name: " + b.name();
                if (b.snapshot() != null) {
                    output += ", Snapshot: " + b.snapshot();
                }
                System.out.println(output);
            }
        }
        else {
            System.out.println("There are no blobs to list off.");
        }
    
        // If there is not another segment, return this response as the final response.
        if (response.body().nextMarker() == null) {
            return Single.just(response);
        } else {
            /*
            IMPORTANT: ListBlobsFlatSegment returns the start of the next segment; you MUST use this to get the next
            segment (after processing the current result segment
            */
            
            String nextMarker = response.body().nextMarker();

            /*
            The presence of the marker indicates that there are more blobs to list, so we make another call to
            listBlobsFlatSegment and pass the result through this helper function.
            */
            
            return url.listBlobsFlatSegment(nextMarker, new ListBlobsOptions(null, null,
                    1))
                    .flatMap(containersListBlobFlatSegmentResponse ->
                            listAllBlobs(url, containersListBlobFlatSegmentResponse));
        }
    }

    static void deleteBlob(BlockBlobURL blobURL) {
        // Delete the blob
        blobURL.delete(null, null)
        .subscribe(
            response -> System.out.println(">> Blob deleted: " + blobURL),
            error -> System.out.println(">> An error encountered during deleteBlob: " + error.getMessage()));

    }

    static void getBlob(BlockBlobURL blobURL, File sourceFile) {
        try {
            // Get the blob
            // Since the blob is small, we'll read the entire blob into memory asynchronously
            // com.microsoft.rest.v2.util.FlowableUtil is a static class that contains helpers to work with Flowable
            blobURL.download(new BlobRange(0, Long.MAX_VALUE), null, false)
                    .flatMapCompletable(response -> {
                        AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get(sourceFile.getPath()), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                        return FlowableUtil.writeFile(response.body(), channel);
                    })
                    .blockingAwait();
            System.out.println("The blob was downloaded to " + sourceFile.getAbsolutePath());            
        } catch (Exception ex){

            System.out.println(ex.toString());
        }
    }

    public static void main(String[] args){
        ContainerURL containerURL;

        // Creating a sample file to use in the sample
        File sampleFile = createTempFile();


        try {
            File downloadedFile = File.createTempFile("downloadedFile", ".txt");
            // Retrieve the credentials and initialize SharedKeyCredentials    
            String accountName = System.getenv("ACCOUNT_NAME");
            String accountKey = System.getenv("ACCOUNT_KEY");

            // Create a ServiceURL to call the Blob service. We will also use this to construct the ContainerURL
            SharedKeyCredentials creds = new SharedKeyCredentials(accountName, accountKey);
            final ServiceURL serviceURL = new ServiceURL(new URL("https://" + accountName + ".blob.core.windows.net"), StorageURL.createPipeline(creds, new PipelineOptions()));

            // Let's create a container using a blocking call to Azure Storage
            containerURL = serviceURL.createContainerURL("quickstart");
            try {
                containerURL.create(null, null).blockingGet();
                System.out.println("Created quickstart container");
            } catch (RestException e) {
                if (e.response().statusCode() != 409) {
                    throw e;
                } else {
                    System.out.println("quickstart container already exists");
                }
            }

            // Create a BlockBlobURL to run operations on Blobs
            final BlockBlobURL blobURL = containerURL.createBlockBlobURL("SampleBlob.txt");

            // Listening for commands from the console
            System.out.println("Enter a command");
            System.out.println("(P)utBlob | (L)istBlobs | (G)etBlob | (D)eleteBlobs | (E)xitSample");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {

                System.out.println("# Enter a command : ");
                String input = reader.readLine();

                switch(input){
                    case "P":
                        System.out.println("Uploading the sample file into the container: " + containerURL );
                        uploadFile(blobURL, sampleFile);
                        break;
                    case "L":
                        System.out.println("Listing blobs in the container: " + containerURL );
                        listBlobs(containerURL);
                        break;
                    case "G":
                        System.out.println("Get the blob: " + blobURL.toString() );
                        getBlob(blobURL, downloadedFile);
                        break;
                    case "D":
                        System.out.println("Delete the blob: " + blobURL.toString() );
                        deleteBlob(blobURL);
                        System.out.println();
                        break;
                    case "E":
                        System.out.println("Cleaning up the sample and exiting!");
                        containerURL.delete(null).blockingGet();
                        downloadedFile.delete();
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }

        } catch (InvalidKeyException e) {
            System.out.println("Invalid Storage account name/key provided");
        } catch (MalformedURLException e) {
            System.out.println("Invalid URI provided");
        } catch (RestException e){
            System.out.println("Service error returned: " + e.response().statusCode() );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}