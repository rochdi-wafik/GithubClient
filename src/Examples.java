import Listener.OnClientListener;
import api.GithubStorage;

public class Examples{

    public static final String TOKEN="__YOUR__ACCESS__TOKEN__";
    public static final String OWNER="__YOUR__NAME__";
    public static final String REPO="__YOUR__REPO__";
    public static final String FILE_PATH="assets/servers.json";

    public static void main(String[] args) throws Exception {

        /**
         * Github Storage
         * ------------------------------------------------------
         * - Fetch file from repo and save it in storage
         */
        new GithubStorage.Builder(OWNER, REPO, FILE_PATH)
            .setAccessToken(TOKEN)
            .saveToPath("__output__servers.m4a")
            .addListener(new OnClientListener() {
                @Override
                public void onStart() {
                    System.out.println("Starting..");
                }
                @Override
                public void onProgress(float progress) {
                    System.out.println("Download progress: "+progress);
                }
                @Override
                public void onComplete() {
                    System.out.println("Download Completed.");
                }
                @Override
                public void onFailer(String strMsg) {
                    System.out.println("Failed! "+strMsg);
                }
            })
            .build();
        
    }
}
