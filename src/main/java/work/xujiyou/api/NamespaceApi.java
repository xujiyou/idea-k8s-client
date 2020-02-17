package work.xujiyou.api;

import com.google.common.collect.Lists;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import work.xujiyou.entity.ComponentStatusEntity;
import work.xujiyou.entity.KindEntity;
import work.xujiyou.entity.NamespaceEntity;
import work.xujiyou.entity.NodeEntity;
import work.xujiyou.utils.Csv;
import work.xujiyou.utils.Kubectl;

import java.io.IOException;
import java.util.*;

/**
 * NamespaceApi class
 *
 * @author jiyouxu
 * @date 2020/2/16
 */
public class NamespaceApi {

    public static List<NamespaceEntity> findNamespace(String configPath) {
        try {
            String result = Kubectl.exec("kubectl get Namespace --kubeconfig=" + configPath);
            if (result != null) {
                CSVParser parser = Csv.getNodeEntities(result);
                List<NamespaceEntity> namespaceEntityList = new ArrayList<>();
                for (CSVRecord csvRecord : parser) {
                    namespaceEntityList.add(new NamespaceEntity(csvRecord.get("NAME"), csvRecord.get("STATUS")));
                }
                return namespaceEntityList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> findNamespaceResourcesType(String configPath, String namespaces) {
        try {
            String command = "kubectl get all  --kubeconfig=" + configPath + " -n " + namespaces;
            String result = Kubectl.execByAddArg(command, "--output=jsonpath=\"{range .items[*]}{.kind}{'\\n'}{end}\"");
            if (result != null) {
                result = result.replaceAll("\"", "");
                List<String> list = Lists.newArrayList(result.split("\n"));
                Set<String> set = new HashSet<>(list);
                return new ArrayList<>(set);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<KindEntity> findNamespaceOneResourcesList(String configPath, String kind, String namespaces) {
        try {
            String command = "kubectl get " + kind + "  --kubeconfig=" + configPath + " -n " + namespaces;
            String result = Kubectl.exec(command);
            if (result != null) {
                CSVParser parser = Csv.getNodeEntities(result);
                List<KindEntity> kindEntityList = new ArrayList<>();
                for (CSVRecord csvRecord : parser) {
                    kindEntityList.add(new KindEntity(csvRecord.get("NAME")));
                }
                return kindEntityList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
