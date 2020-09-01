package org.nebiros.circlesmanager.console;

import org.nebiros.circlesmanager.misc.AppConfig;

import java.util.Scanner;

public class ConsoleMain
{
    public static void main(String[] args)
    {
        System.out.println("Reading config file...");
        AppConfig config = new AppConfig();

        // Initial Main Folder Setup
        if (!config.isRootFolderSet())
        {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Main Folder not set!!!");
            do {
                System.out.print("Enter Main Folder: ");
                if (!config.setRootFolder(scanner.nextLine().trim()))
                {
                    System.out.println("Invalid Folder!!");
                } else
                {
                    config.writeToConfigFile();
                    break;
                }
            } while(true);
        }

        System.out.println("Main Folder: " + config.getRootFolderString());

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
                default:
                    System.out.println("Unknown command " + cmd_parts[0]);
            }
        }
    }
}
