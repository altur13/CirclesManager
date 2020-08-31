package org.nebiros.circlesmanager.console;

import java.util.Scanner;

public class ConsoleMain
{
    public static void main(String[] args)
    {
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
