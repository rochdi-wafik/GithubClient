package thread;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import Listener.OnClientListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class StorageThread extends Thread{
    private final String HEADER_ACCEPT = "application/vnd.github.v3.raw";
    private final String URL_FORMAT = "https://api.github.com/repos/%s/%s/contents/%s";
    private final OkHttpClient httpClient;

    private OnClientListener onClientListener;
    private String accessToken;
    private String owner; // owner means the username used in github account
    private String repository;
    private String filePath; // example: assets/server.json
    private String pathToSave;


    /**
     * @param owner => The github username
     * @param repository => if private, then accessToken is necessery
     * @param filePath => example: assets/servers.json
     * @param accessToken => (optional) to access private repository 
     * @param pathToSave => (optional) if you want to save the file in storage
     * @param onClientListener => to get the operation events
     */
    public StorageThread(String owner, String repository, String filePath, String accessToken, String pathToSave, OnClientListener onClientListener){
        this.owner = owner;
        this.repository = repository;
        this.filePath = filePath;
        this.accessToken = accessToken;
        this.pathToSave = pathToSave;
        this.onClientListener = onClientListener;
        this.httpClient = new OkHttpClient();
    }

    @Override
    public void run() {
        if(onClientListener!=null) onClientListener.onStart();

        // Create the file url
        String url = String.format(URL_FORMAT, owner, repository, filePath);

        /**
         * Create HTTP Request
         */
        Request.Builder builder = new Request.Builder().url(url).header("Accept", HEADER_ACCEPT);
        if(this.accessToken!=null){
            builder.header("Authorization", "token " + accessToken);
        }
        Request httpRequest = builder.build();

        /**
         * Get HTTP Response
         */
        try {
            Response response = httpClient.newCall(httpRequest).execute();

            /**
             * On Failure
             */
            if(!response.isSuccessful()){
                onClientListener.onFailer(response.message());
                onClientListener.onFailer(response.code());
                return;
            }

            /**
             * On Success
             */

            ResponseBody responseBody = response.body();

            if(responseBody==null){
                onClientListener.onFailer("Null response!");
                return;
            }

            /**
             * Read the file
             */

            if(pathToSave!=null){
                readFile(response, responseBody, pathToSave);
            }else{
                readFile(response, responseBody, null);
            }

            // Close the body stream
            responseBody.close();
        } catch (IOException e) {
            onClientListener.onFailer(e.getMessage());
        }
        
    }


    /**
     * Read The File
     * @param response
     * @param responseBody
     * @param saveInPath if !=null, save the fetched file in the given path
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void readFile(Response response, ResponseBody responseBody, String saveInPath) throws FileNotFoundException, IOException{

        try (InputStream inputStream = responseBody.byteStream();) 
        {
            byte[] buffer = new byte[1024 * 4];
            long byteFetched = 0;
            long fileLength = response.body().contentLength();
            
            onClientListener.onFileSize(fileLength);
            

            /**
             * Save File
             */
            if(saveInPath!=null){
                OutputStream outputStream = new FileOutputStream(saveInPath);
                while (true) {
                    int currentRead = inputStream.read(buffer);
                    if (currentRead == -1) {
                        break;
                    }
                    // Write to storage
                    outputStream.write(buffer, 0, currentRead);
                    
                    // Calculate Progress
                    byteFetched += currentRead;
                    float progress = (float) byteFetched / fileLength;
                    onClientListener.onProgress(progress);
                    
                }

                onClientListener.onFileSaved(filePath);

                outputStream.flush();
                outputStream.close();
            }
            /**
             * Return Output Stream File
             */
            else{
                ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();

                while (true) {
                    int currentRead = inputStream.read(buffer);
                    if (currentRead == -1) {
                        break;
                    }
                   
                    // Write to byte object
                    byteOutput.write(buffer, 0, currentRead);
    
                    // Calculate Progress
                    byteFetched += currentRead;
                    float progress = (float) byteFetched / fileLength;
                    onClientListener.onProgress(progress);  
                }

                // Final output file bytes
                byte[] fileBytes = byteOutput.toByteArray();
                onClientListener.onFileFethced(fileBytes);
            }

            // Operation Completed
            onClientListener.onComplete();
        }
    }
    
}
