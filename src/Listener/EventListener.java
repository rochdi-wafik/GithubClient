package Listener;


public interface EventListener {
    public void onStart();
    
    public void onProgress(float progress);
    public void onFileSize(long fileSize);
    public void onFileFethced(byte[] file);
    public void onFileSaved(String outputPath);

    public void onComplete();
    public void onFailer(String strMsg);
    public void onFailer(int errorCode);
}
