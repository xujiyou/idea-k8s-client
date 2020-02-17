package work.xujiyou.api;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import work.xujiyou.KubernetesConfiguration;
import work.xujiyou.entity.StorageClassEntity;
import work.xujiyou.utils.Csv;
import work.xujiyou.utils.Bash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * StorageClassApi class
 *
 * @author jiyouxu
 * @date 2020/2/16
 */
public class StorageClassApi {

    public static List<StorageClassEntity> findStorageClass(String configPath) {
        String kubectlPath = KubernetesConfiguration.getInstance().getKubectlPath();
        try {
            String result = Bash.exec(kubectlPath + " get StorageClass --kubeconfig=" + configPath);
            if (result != null) {
                CSVParser parser = Csv.getNodeEntities(result);
                List<StorageClassEntity> storageClassEntityList = new ArrayList<>();
                for (CSVRecord csvRecord : parser) {
                    storageClassEntityList.add(new StorageClassEntity(csvRecord.get("NAME"), csvRecord.get("PROVISIONER")));
                }
                return storageClassEntityList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
