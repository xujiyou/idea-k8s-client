package work.xujiyou.api;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1APIResourceList;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import kotlin.text.Charsets;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * ResourcesApi class
 *
 * @author jiyouxu
 * @date 2020/2/14
 */
public class ResourcesApi {

    public static void findResource(String configPath) {
//        String cmdStr = "kubectl api-resources --namespaced=false -o wide";
//        final CommandLine cmdLine = CommandLine.parse(cmdStr);
//        DefaultExecutor executor = new DefaultExecutor();
//        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            executor.setStreamHandler(new PumpStreamHandler(baos, baos));
//            int exitValue = executor.execute(cmdLine);
//            if (exitValue == 0) {
//                String result = baos.toString(Charsets.UTF_8);
//                System.out.println(result.replaceAll(" +",","));
////                CSVFormat csvFormat = CSVFormat.DEFAULT;
////                CSVParser parser = CSVParser.parse(baos.toInputStream(), Charsets.UTF_8, csvFormat);
////                for (CSVRecord csvRecord : parser) {
////                    System.out.println(csvRecord.get(0));
////                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}


