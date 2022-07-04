package org.gepron1x.resourcepacks;

import com.google.common.base.Preconditions;
import org.bukkit.plugin.java.JavaPlugin;
import org.takes.Take;
import org.takes.facets.fallback.*;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.FtBasic;
import org.takes.misc.Opt;
import org.takes.rs.RsHtml;
import org.takes.tk.TkFiles;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicBoolean;

public final class PacksServerPlugin extends JavaPlugin {
    
    private final AtomicBoolean exit = new AtomicBoolean(false);


    @Override
    public void onEnable() {
        saveDefaultConfig();

        int port = getConfig().getInt("port");

        File files = new File(getDataFolder(), "files");
        if(files.mkdir()) {
            getLogger().info("created a files successfully.");
        }


        Fallback fallback = (req) -> new Opt.Single<>(
                new RsHtml(
                        MessageFormat.format("<h1> {0} </h1> <p>{1}</p>",
                                req.code(),
                                req.throwable().getMessage())
                )
        );

        Take take = new TkFallback(
                new TkFork(new FkRegex("/.+", new TkFiles(files))),
                fallback
        );

        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try {
                new FtBasic(take, port).start(this.exit::get);
            } catch (IOException e) {
               getSLF4JLogger().error("Error happened while starting a webserver.", e);
            }
        });
        getLogger().info("started web server successfully.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Shutting down web server...");
        exit.set(true);
    }
}
