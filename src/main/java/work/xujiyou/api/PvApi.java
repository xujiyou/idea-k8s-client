package work.xujiyou.api;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import work.xujiyou.KubernetesConfiguration;
import work.xujiyou.entity.PvEntity;
import work.xujiyou.utils.Csv;
import work.xujiyou.utils.Bash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * PvApi class
 *
 * @author jiyouxu
 * @date 2020/2/16
 */
public class PvApi {

    public static List<PvEntity> findPv(String configPath) {
        String kubectlPath = KubernetesConfiguration.getInstance().getKubectlPath();
        try {
            String result = Bash.exec(kubectlPath + " get pv --kubeconfig=" + configPath);
            if (result != null) {
                result = result.replaceAll("ACCESS MODES", "ACCESS_MODES");
                result = result.replaceAll("RECLAIM POLICY", "RECLAIM_POLICY");
                CSVParser parser = Csv.getNodeEntities(result);
                List<PvEntity> storageClassEntityList = new ArrayList<>();
                for (CSVRecord csvRecord : parser) {
                    storageClassEntityList.add(new PvEntity(csvRecord.get("NAME"), csvRecord.get("CAPACITY"),
                            csvRecord.get("ACCESS_MODES"), csvRecord.get("RECLAIM_POLICY"), csvRecord.get("STATUS"),
                            csvRecord.get("CLAIM"), csvRecord.get("STORAGECLASS")));
                }
                return storageClassEntityList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
