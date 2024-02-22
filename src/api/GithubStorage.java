package api;
import Listener.OnClientListener;
import thread.StorageThread;

public class GithubStorage extends Thread{

    /**
     * Use GithubClient.Builder to build/initialize the client
     * @param owner => The github username
     * @param repository => if private, then accessToken is necessery
     * @param filePath => example: assets/servers.json
     * @param accessToken => (optional) to access private repository 
     * @param pathToSave => (optional) if you want to save the file in storage
     * @param onClientListener => to get the operation events
     */
    private GithubStorage(String owner, String repository, String filePath, String accessToken, String pathToSave, OnClientListener onClientListener){
        StorageThread clientThread = new StorageThread(owner, repository, filePath, accessToken, pathToSave, onClientListener);
        clientThread.start();
    }

    /**
     * Client Builder
     */
    public static class  Builder {
        private  OnClientListener onClientListener;
        private  String accessToken;
        private  String owner; // owner means the username used in github account
        private  String repository;
        private  String filePath; // example: assets/server.json
        private  String pathToSave;

        /**
         * Set the necessery fialds
         * @param owner
         * @param repository
         * @param filePath
         */
        public Builder(String owner, String repository, String filePath){
            this.owner = owner;
            this.repository = repository;
            this.filePath = filePath;
        }

        /**
         * Set Access Token
         * - It's required if you want to fetch from a private repository
         * - It can be also used with public repositiry
         * - With AccessToken you can get 5K request per hour
         * @param accessToken
         * @return Builder
         */
        public GithubStorage.Builder setAccessToken(String accessToken){
            this.accessToken = accessToken;
            return this;
        } 
    
        /**
         * Save the fetched file in your local storage
         * @param fullPath
         * @return Builder
         */
        public GithubStorage.Builder saveToPath(String fullPath){
            this.pathToSave = fullPath;
            return this;
        }
    
        /**
         * Add Listener to get the events
         * @param onClientListener
         * @return Builder
         */
        public GithubStorage.Builder addListener(OnClientListener onClientListener){
            this.onClientListener = onClientListener;
            return this;
        }
    
        public GithubStorage build(){
            return new GithubStorage(owner, repository, filePath, accessToken, pathToSave, onClientListener);
        }
    
        
    }
    
}
