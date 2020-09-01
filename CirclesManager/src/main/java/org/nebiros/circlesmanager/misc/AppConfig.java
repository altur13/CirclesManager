package org.nebiros.circlesmanager.misc;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class AppConfig
{
    private final Path configFolder;
    private static final String configFileName = "config";
    private static final String appNameFolder = "circles-manager";
    private String rootFolder = "";

    public AppConfig()
    {
        String userHome = System.getProperty("user.home");
        configFolder = Paths.get(userHome, ".config", appNameFolder);

        // create/check if config folder exists
        if (configFolder.toFile().mkdirs())
        {
            writeToConfigFile();
        }

        // create/check if config file exists
        File configFile = Paths.get(configFolder.toString(), configFileName).toFile();
        if (!(configFile.exists() && configFile.isFile()))
        {
            writeToConfigFile();
        }

        readConfigFile();
    }

    public void writeToConfigFile()
    {
        try {
            FileWriter fileWriter = new FileWriter(Paths.get(configFolder.toString(), configFileName).toString());
            fileWriter.write("root-folder=" + this.rootFolder + '\n');
            fileWriter.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void readConfigFile()
    {
        try {
            Scanner scanner = new Scanner(Paths.get(configFolder.toString(), configFileName).toFile());
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("=");
                if (parts.length < 2)
                    this.rootFolder = "";
                else
                    this.rootFolder = parts[1].trim();
            }
            scanner.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean isRootFolderSet()
    {
        return !(rootFolder.isEmpty());
    }

    public Path getRootFolder()
    {
        if (isRootFolderSet())
            return Paths.get(rootFolder);
        else
            return null;
    }

    public String getRootFolderString()
    {
        if (isRootFolderSet())
            return rootFolder;
        else
            return "";
    }

    // TODO: improve the logic of this method...
    public boolean setRootFolder(String folder)
    {
        File file = new File(folder);
        if (file.exists() && file.isDirectory())
        {
            this.rootFolder = folder;
            return true;
        }
        else if (!file.exists())
        {
            if (file.mkdirs())
            {
                this.rootFolder = folder;
                return true;
            }
            else
                return false;
        }

        return false;
    }
}
