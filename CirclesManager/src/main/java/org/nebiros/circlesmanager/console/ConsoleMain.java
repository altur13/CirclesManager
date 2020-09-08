package org.nebiros.circlesmanager.console;

import org.nebiros.circlesmanager.misc.AppConfig;
import org.nebiros.circlesmanager.misc.ExternalCmdRunner;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class ConsoleMain
{
    public static void main(String[] args)
    {
        System.out.println("Reading config file...");
        AppConfig config = new AppConfig();

        Scanner scanner = new Scanner(System.in);
        // Initial Main Folder Setup
        if (!config.isRootFolderSet())
        {
            System.out.println("Main Folder not set!!!");
            do {
                System.out.print("Enter Main Folder: ");
                if (!config.setRootFolder(scanner.nextLine().trim()))
                {
                    System.out.println("Invalid Folder!!");
                } else
                {
                    config.writeToConfigFile();

                    System.out.println("Creating folder structure...");
                    Paths.get(config.getRootFolderString(), "repositories").toFile().mkdirs();

                    break;
                }
            } while(true);
        }
        System.out.println("Main Folder: " + config.getRootFolderString());

        // Checking if wine is installed
        System.out.print("Checking for wine presence...");
        if (!ExternalCmdRunner.isWineInstalled())
        {
            System.out.println("\n    Error: wine not found! Please install wine/wine staging first.");
            return;
        }
        else
        {
            System.out.println("OK");
        }

        // Checking if winetricks is installed
        System.out.print("Checking for winetricks presence...");
        if (!ExternalCmdRunner.isWinetricksInstalled())
        {
            System.out.println("\n    Error: winetricks not found! Please install it using the package manager.");
            return;
        }
        else
        {
            System.out.println("OK");
        }

        // Setting up wine prefix for osu
        // TODO: create the prefix ourselves
        File winePrefix = Paths.get(config.getRootFolderString(), "wine-prefix").toFile();
        if (!winePrefix.exists())
        {
            System.out.print("  Enter path to existing wine prefix: ");
            String prefixPath = scanner.nextLine();
            // create symbolic link
            try
            {
                Files.createSymbolicLink(Paths.get(config.getRootFolderString(), "wine-prefix"),
                        Paths.get(prefixPath));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        // Install the most recent version of osu
        File osuInstallFolder = Paths.get(config.getRootFolderString(), "osu-install").toFile();
        if (!osuInstallFolder.exists())
        {
            osuInstallFolder.mkdirs();
            ExternalCmdRunner.installStableOsuClient(config, osuInstallFolder);
        }

        repl();
    }

    public static void repl()
    {
        Scanner scanner = new Scanner(System.in);
        while (true)
        {
            System.out.print("> ");
            String cmd = scanner.nextLine();
            String[] cmd_parts = cmd.split(" ");
            String cmd_norm = cmd_parts[0].toLowerCase();

            switch (cmd_norm)
            {
                case "exit":
                case "q":
                    return;
                case "finish-install":
                    System.out.println("Finishing osu installation....");
                    break;
                default:
                    System.out.println("Unknown command " + cmd_parts[0]);
            }
        }
    }
}
