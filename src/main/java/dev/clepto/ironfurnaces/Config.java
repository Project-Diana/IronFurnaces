package dev.clepto.ironfurnaces;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static int IronFurnaceCookTime;
    public static int GoldFurnaceCookTime;
    public static int DiamondFurnaceCookTime;
    public static int ObsidianFurnaceCookTime;
    public static int CrystalFurnaceCookTime;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        String category = "properties";
        IronFurnaceCookTime = configuration
            .getInt("IronFurnaceCookTime", category, 100, 1, 9999, "How long it takes the iron furnace to cook items.");
        GoldFurnaceCookTime = configuration
            .getInt("GoldFurnaceCookTime", category, 50, 1, 9999, "How long it takes the gold furnace to cook items.");
        DiamondFurnaceCookTime = configuration.getInt(
            "DiamondFurnaceCookTime",
            category,
            25,
            1,
            9999,
            "How long it takes the diamond furnace to cook items.");
        ObsidianFurnaceCookTime = configuration.getInt(
            "ObsidianFurnaceCookTime",
            category,
            25,
            1,
            9999,
            "How long it takes the obsidian furnace to cook items.");
        CrystalFurnaceCookTime = configuration.getInt(
            "CrystalFurnaceCookTime",
            category,
            10,
            1,
            9999,
            "How long it takes the crystal furnace to cook items.");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
