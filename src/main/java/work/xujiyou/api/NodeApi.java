package work.xujiyou.api;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import work.xujiyou.entity.NodeEntity;
import work.xujiyou.utils.Csv;
import work.xujiyou.utils.Kubectl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * NodeApi class
 *
 * @author jiyouxu
 * @date 2020/2/16
 */
public class NodeApi {

    public static List<NodeEntity> findNodes(String configPath) {
        try {
            String result = Kubectl.exec("kubectl get Node  --kubeconfig=" + configPath);
            if (result != null) {
                CSVParser parser = Csv.getNodeEntities(result);
                List<NodeEntity> nodeEntityList = new ArrayList<>();
                for (CSVRecord csvRecord : parser) {
                    nodeEntityList.add(new NodeEntity(csvRecord.get("NAME"), csvRecord.get("STATUS")));
                }
                return nodeEntityList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
