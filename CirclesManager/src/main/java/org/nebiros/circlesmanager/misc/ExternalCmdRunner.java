package org.nebiros.circlesmanager.misc;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.Map;

public class ExternalCmdRunner
{
    public static boolean isWineInstalled()
    {
        boolean retval = false;
        try
        {
            Process process = new ProcessBuilder("wine", "--version").start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            if (line.startsWith("wine-"))
                retval = true;
        }
        catch (Exception e)
        {
            retval = false;
        }

        return retval;
    }

    public static boolean isWinetricksInstalled()
    {
        boolean retval = false;
        try
        {
            Process process = new ProcessBuilder("winetricks", "--version").start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            if (line.contains("sha256sum"))
                retval = true;
        }
        catch (Exception e)
        {
            retval = false;
        }

        return retval;
    }

    private static ProcessBuilder setupProcessBuilder(AppConfig appConfig)
    {
        ProcessBuilder processBuilder = new ProcessBuilder();

        // Setup working directory
        processBuilder.directory(appConfig.getRootFolder().toFile());

        // Setup wine environment
        Map<String, String> environment = processBuilder.environment();
        environment.put("WINEPREFIX",
                Paths.get(appConfig.getRootFolderString(), "wine-prefix").toString());
        environment.put("WINEARCH", "win32");
        environment.put("WINEDEBUG", "-all");
        environment.put("vblank_mode", "0");
        environment.put("PULSE_LATENCY_MSEC", "40");
        environment.put("STAGING_AUDIO_DURATION", "10000");

        return processBuilder;
    }

    // TODO: how to do this?
    public static void setupWinePrefix(AppConfig appConfig)
    {
        try
        {
            ProcessBuilder processBuilder = setupProcessBuilder(appConfig);
            processBuilder.directory(appConfig.getRootFolder().toFile());

            /*
            System.out.print("    Prefix boot...");
            Process process = processBuilder.command("wineboot", "--init").start();
            process.waitFor();
            System.out.println("OK");

            System.out.print("    Uninstalling wine mono...");
            Process process = processBuilder
                    .command("wine", "uninstaller", "--remove", "'{E45D8920-A758-4088-B6C6-31DBB276992E}'")
                    .start();
            process.waitFor();
            System.out.println("OK");
             */
            System.out.print("    Installing .NET 4.0...");
            Process process = processBuilder.command("winetricks", "winxp").start();
            process.waitFor();
            process = processBuilder.command("wget",
                    "'http://download.microsoft.com/download/9/5/A/95A9616B-7A37-4AF6-BC36-D6EA96C8DAAE/dotNetFx40_Full_x86_x64.exe'")
                    .start();
            process.waitFor();
            process = processBuilder.command("wine", "~/dotNetFx40_Full_x86_x64.exe").start();
            process.waitFor();
            System.out.println("OK");

            /*
            Process process = processBuilder.command("winetricks", "dotnet462").start();
            process.waitFor();

            process = processBuilder.command("winetricks", "cjkfonts").start();
            process.waitFor();

            process = processBuilder.command("winetricks", "gdiplus").start();
            process.waitFor();
             */
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void installStableOsuClient(AppConfig appConfig, File installPath)
    {
        try
        {
            // Download official osu client
            System.out.println("Installing osu client...");

            System.out.print("    Downloading client executable...");
            URL clientURL = new URL("https://m1.ppy.sh/r/osu!install.exe");
            ReadableByteChannel readableByteChannel = Channels.newChannel(clientURL.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(
                    Paths.get(installPath.toString(), "osu-client.exe").toString());
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            System.out.println("OK");

            // Run the installer
            System.out.print("    Running client installer...");
            ProcessBuilder processBuilder = setupProcessBuilder(appConfig);
            processBuilder.directory(installPath);
            Process process = processBuilder.command("wine", "osu-client.exe").start();
            process.waitFor();

            System.out.println("OK");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
