package com.greencat.antimony.common;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.greencat.Antimony;
import com.greencat.antimony.core.via.loader.BackwardsLoader;
import com.greencat.antimony.core.via.loader.RewindLoader;
import com.greencat.antimony.core.via.platform.Injector;
import com.greencat.antimony.core.via.platform.Platform;
import com.greencat.antimony.core.via.platform.ProviderLoader;
import com.greencat.antimony.utils.JLoggerToLog4j;
import com.viaversion.viaversion.ViaManagerImpl;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import io.netty.channel.EventLoop;
import io.netty.channel.local.LocalEventLoopGroup;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

public class Via {
    private static Via instance;
    public final static int SHARED_VERSION = 47;

    public static void init(){
        instance = new Via();
    }

    public static Via getInstance() {
        return instance;
    }

    private final Logger jLogger = new JLoggerToLog4j(LogManager.getLogger("Antimony"));
    private final CompletableFuture<Void> initFuture = new CompletableFuture<>();

    private ExecutorService asyncExecutor;
    private EventLoop eventLoop;

    private File file;
    private int version;
    private String lastServer;
    public void start() {
        ThreadFactory factory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("antimony_via").build();
        asyncExecutor = Executors.newFixedThreadPool(8, factory);

        eventLoop = new LocalEventLoopGroup(1, factory).next();
        eventLoop.submit(initFuture::join);

        setVersion(SHARED_VERSION);
        this.file = new File("Antimony/via");
        this.file.mkdir();

        com.viaversion.viaversion.api.Via.init(
                ViaManagerImpl.builder()
                        .injector(new Injector())
                        .loader(new ProviderLoader())
                        .platform(new Platform(file))
                        .build()
        );

        MappingDataLoader.enableMappingsCache();
        ((ViaManagerImpl) com.viaversion.viaversion.api.Via.getManager()).init();

        new BackwardsLoader(file);
        new RewindLoader(file);

        initFuture.complete(null);
    }
    public Logger getjLogger() {
        return jLogger;
    }

    public CompletableFuture<Void> getInitFuture() {
        return initFuture;
    }

    public ExecutorService getAsyncExecutor() {
        return asyncExecutor;
    }

    public EventLoop getEventLoop() {
        return eventLoop;
    }

    public File getFile() {
        return file;
    }

    public String getLastServer() {
        return lastServer;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setLastServer(String lastServer) {
        this.lastServer = lastServer;
    }
}
