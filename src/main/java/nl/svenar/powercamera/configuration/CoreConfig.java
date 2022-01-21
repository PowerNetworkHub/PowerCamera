package nl.svenar.powercamera.configuration;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.handlers.BaseConfig;

public class CoreConfig extends BaseConfig {
    
    public CoreConfig(PowerCamera plugin, String name) {
        super(plugin, name);
    }

    // public final String getCameraFormat() {
    //     return getConfig().getString("camera_format").toLowerCase();
    // }

    // public final String getLanguage() {
    //     return getConfig().getString("options.language").toLowerCase();
    // }

    // public boolean doUpdateCheck() {
    //     return getConfig().getBoolean("check-for-updates");
    // }

    // public boolean pluginhookEnabled(String option) {
    //     return getConfig().getBoolean("pluginhooks." + option);
    // }

    // public boolean playerTaggingEnabled() {
    //     return getConfig().getBoolean("options.player-tagging.enabled");
    // }

    // public boolean playerTaggingSoundEnabled() {
    //     return getConfig().getBoolean("options.player-tagging.play-sound.enabled");
    // }

    // public int playerTaggingSoundInterval() {
    //     return getConfig().getInt("options.player-tagging.play-sound.interval");
    // }
}
