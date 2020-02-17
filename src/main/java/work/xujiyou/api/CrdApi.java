package work.xujiyou.api;

import com.google.protobuf.TextFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import work.xujiyou.entity.CrdEntity;
import work.xujiyou.entity.PvEntity;
import work.xujiyou.utils.Csv;
import work.xujiyou.utils.Kubectl;

import java.io.IOException;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

/**
 * CrdApi class
 *
 * @author jiyouxu
 * @date 2020/2/17
 */
public class CrdApi {

    public static List<CrdEntity> findCrd(String configPath) {
        try {
            String command = "kubectl get crd  --kubeconfig=" + configPath;
            String result = Kubectl.execByAddArg(command, "--output=jsonpath=\"{range .items[*]}{.metadata.name}{','}{.spec.names.kind}{'\\n'}{end}\"");
            if (result != null) {
                result = "NAME,KIND\n" + result;
                result = result.replaceAll("\"", "");
                System.out.println(result);
                CSVParser parser = Csv.getNodeEntities(result);
                List<CrdEntity> crdEntityList = new ArrayList<>();
                for (CSVRecord csvRecord : parser) {
                    crdEntityList.add(new CrdEntity(csvRecord.get("NAME"), csvRecord.get("KIND")));
                }
                return crdEntityList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
