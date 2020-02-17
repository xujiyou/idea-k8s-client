package work.xujiyou.api;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import work.xujiyou.entity.ComponentStatusEntity;
import work.xujiyou.utils.Csv;
import work.xujiyou.utils.Kubectl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ComponentStatusApi class
 *
 * @author jiyouxu
 * @date 2020/2/16
 */
public class ComponentStatusApi {

    public static List<ComponentStatusEntity> findComponentStatus(String configPath) {
        try {
            String result = Kubectl.exec("kubectl get ComponentStatus --kubeconfig=" + configPath);
            if (result != null) {
                CSVParser parser = Csv.getNodeEntities(result);
                List<ComponentStatusEntity> nodeEntityList = new ArrayList<>();
                for (CSVRecord csvRecord : parser) {
                    nodeEntityList.add(new ComponentStatusEntity(csvRecord.get("NAME"), csvRecord.get("STATUS")));
                }
                return nodeEntityList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
