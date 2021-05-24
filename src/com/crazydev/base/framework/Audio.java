package com.crazydev.base.framework;

public interface Audio {
    public Music newMusic(String filename);

    public Sound newSound(String filename);
    
    public void volumeUp();
    
    public void volumeDown();
    
    public void volumeMute();
}