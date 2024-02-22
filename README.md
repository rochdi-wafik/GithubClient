# Gethub Client
A simple Github api client written in java.

## Current APIs
- Until now, there is the following apis:

### Github Storage
This Api lets you fetch remote files from a github repository.

- `Features:`
- Support Access Token:
Although you can fetch files directly without authentication, Its very useful to uses an Access Token for two reasons:
* With Access Token: 
1. You can fetch files from private repository via Access Token 
2. You can do 5K request per hour if you using Access Token
* Without Access Token
1. You can only fetch files from public repositories.
2. You can only create 50 request per hour

- Progress Listener:
You can get the realtime progress of the downlading operation using a Listener

- `How To Use:`
```java
public class App{

    public static final String TOKEN="__YOUR__ACCESS__TOKEN__";
    public static final String OWNER="__YOUR__NAME__";
    public static final String REPO="__YOUR__REPO__";
    public static final String FILE_PATH="assets/servers.json";

    public static void main(String[] args) throws Exception {

        new GithubClient.Builder(OWNER, REPO, FILE_PATH)
            .setAccessToken(TOKEN)
            .saveToPath("output_file.json")
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
```

## Dependencies 
- `OkHttp` is used as an http client.

## Authors
- Rochdi Wafik [http://fb.com/rochdi.wafik](https://facebook.com/rochdi.wafik)
