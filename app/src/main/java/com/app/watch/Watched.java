package com.app.watch;

/**
 * Created by Administrator on 2016/6/23.
 */
public interface Watched {
    public void addWatcher(Watcher watcher);

    public void removeWatcher(Watcher watcher);

    public void notifyWatchers(String str);
}
