package org.nebiros.circlesmanager.misc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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
}
