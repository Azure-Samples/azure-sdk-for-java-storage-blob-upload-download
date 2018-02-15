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
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;

import com.microsoft.azure.storage.blob.BlobRange;
import com.microsoft.azure.storage.blob.BlockBlobURL;
import com.microsoft.azure.storage.blob.ContainerURL;
import com.microsoft.azure.storage.blob.ListBlobsOptions;
import com.microsoft.azure.storage.blob.PipelineOptions;
import com.microsoft.azure.storage.blob.ServiceURL;
import com.microsoft.azure.storage.blob.SharedKeyCredentials;
import com.microsoft.azure.storage.blob.StorageURL;
import com.microsoft.azure.storage.models.Blob;
import com.microsoft.azure.storage.models.ListBlobsResponse;
import com.microsoft.rest.v2.RestException;
import com.microsoft.rest.v2.util.FlowableUtil;

import io.reactivex.Flowable;

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
        // All APIs accept asynchronous ByteBuffers stored in a Flowable. 
        // Let's use PutBlob to upload the sample data
        try {
            // Read the file asynchronously into a Flowable
            AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(sourceFile.toPath());
            Flowable<ByteBuffer> stream = FlowableUtil.readFile(fileChannel);

            blob.putBlob(stream, sourceFile.length(), null, null, null)
            .doFinally(fileChannel::close)
            .subscribe(
                success -> System.out.println(">> Success : " + success.statusCode() + ", Etag: " + success.headers().eTag()),
                error -> System.out.println(">> Failed to upload the file"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void listBlobs(ContainerURL containerURL) {
        // Each ContainerURL.listBlobs call return up to maxResults (maxResults=10 passed into ListBlobOptions below).
        // To list all Blobs, we are creating a helper static method called listAllBlobs
        // This method keeps track of nextMarker, and repeats the listBlobs call until there is no more nextMarker
        // The result of listBlobs is appended into a Flowable
        ListBlobsOptions options = new ListBlobsOptions(null, null, null, 10);
        listAllBlobs(containerURL, null, options)
        .subscribe(
            res -> {
                System.out.println("Got some blobs:");
                for (Blob blob : res.blobs().blob()) {
                    System.out.println(">> Blob name: " + blob.name());
                }
            },
            err -> System.err.println("An error occurred: " + err.getMessage()),
            () -> System.out.println("Finished listing all blobs."));
    }

    private static Flowable<ListBlobsResponse> listAllBlobs(ContainerURL url, String marker, ListBlobsOptions options) {
        return url.listBlobs(marker, options) // calling ContainerURL.listBlobs to retrieve the next 100 blobs with the nextMarker
        .flatMapPublisher(res -> Flowable.just(res.body()).concatWith( // this will repeat until nextMarker is null
                res.body().nextMarker() != null ? listAllBlobs(url, res.body().nextMarker(), options) : Flowable.empty()));
    }

    static void deleteBlob(BlockBlobURL blobURL) {
        // Delete the blob
        blobURL.delete(null, null)
        .subscribe(
            response -> System.out.println(">> Blob deleted: " + blobURL),
            error -> System.out.println(">> An error encountered during deleteBlob: " + error.getMessage()));

    }

    static void getBlob(BlockBlobURL blobURL) {
        // Get the blob
        // Since the blob is small, we'll read the entire blob into memory asynchronously
        // com.microsoft.rest.v2.util.FlowableUtil is a static class that contains helpers to work with Flowable
        blobURL.getBlob(new BlobRange(0, 1000000000), null, false)
        .flatMap(res -> FlowableUtil.collectBytesInArray(res.body()))
        .subscribe(body -> {
            System.out.println(">> Blob contents: " + new String(body, StandardCharsets.UTF_8));;
        }, error -> {
            System.out.println(">> An error encountered during getBlob: " + error.getMessage());
        }); // getBlob calls the service only when you subscribe to it

    }

    public static void main(String[] args){
        ContainerURL containerURL;

        // Creating a sample file to use in the sample
        File sampleFile = createTempFile();

        try {
            // Retrieve the credentials and initialize SharedKeyCredentials    
            String accountName = System.getenv("AZURE_STORAGE_ACCOUNT");
            String accountKey = System.getenv("AZURE_STORAGE_ACCESS_KEY");

            // Create a ServiceURL to call the Blob service. We will also use this to construct the ContainerURL
            SharedKeyCredentials creds = new SharedKeyCredentials(accountName, accountKey);
            final ServiceURL serviceURL = new ServiceURL(new URL("https://" + accountName + ".blob.core.windows.net"), StorageURL.createPipeline(creds, new PipelineOptions()));

            // Let's create a container using a blocking call to Azure Storage
            containerURL = serviceURL.createContainerURL("bigdata");
            try {
                containerURL.create(null, null).blockingGet();
                System.out.println("Created bigdata container");
            } catch (RestException e) {
                if (e.response().statusCode() != 409) {
                    throw e;
                } else {
                    System.out.println("bigdata container already exists");
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
                        getBlob(blobURL);
                        break;
                    case "D":
                        System.out.println("Delete the blob: " + blobURL.toString() );
                        deleteBlob(blobURL);
                        break;
                    case "E":
                        System.out.println("Cleaning up the sample and exiting!");
                        containerURL.delete(null).blockingGet();
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
