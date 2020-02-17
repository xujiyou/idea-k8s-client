package work.xujiyou.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Csv class
 *
 * @author jiyouxu
 * @date 2020/2/16
 */
public class Csv {

    public static CSVParser getNodeEntities(String result) throws IOException {
        String csvResult = result.replaceAll(" +",",");
        return CSVParser.parse(csvResult, CSVFormat.DEFAULT.withFirstRecordAsHeader());
    }
}
