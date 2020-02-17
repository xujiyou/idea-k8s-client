package work.xujiyou.utils;

import kotlin.text.Charsets;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Kubectl class
 *
 * @author jiyouxu
 * @date 2020/2/16
 */
public class Bash {

    public static String exec(String command) throws IOException {
        final CommandLine cmdLine = CommandLine.parse(command);
        return executor(cmdLine);
    }

    public static String execByAddArg(String command, String arg) throws IOException {
        final CommandLine cmdLine = CommandLine.parse(command);
        cmdLine.addArgument(arg, false);
        return executor(cmdLine);
    }

    @Nullable
    private static String executor(CommandLine cmdLine) throws IOException {
        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        executor.setStreamHandler(new PumpStreamHandler(byteArrayOutputStream, byteArrayOutputStream));
        int exitValue = executor.execute(cmdLine);
        if (exitValue == 0) {
            return byteArrayOutputStream.toString(Charsets.UTF_8);
        }
        return null;
    }
}
