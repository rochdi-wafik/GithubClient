package Listener;

public abstract class OnClientListener implements EventListener{
   
    @Override public void onStart(){}
    
    @Override public void onProgress(byte data){}
    @Override public void onFileSize(long fileSize){}
    @Override public void onFileFethced(byte[] file){}
    @Override public void onFileSaved(String outputPath){}

    @Override public void onComplete(){}
    @Override public void onFailer(String stringError){}
    @Override public void onFailer(int errorCode){}
}
