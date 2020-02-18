package work.xujiyou.view.model;

import com.intellij.ui.components.JBList;
import org.yaml.snakeyaml.Yaml;
import work.xujiyou.KubernetesConfiguration;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ConfigListModel class
 *
 * @author jiyouxu
 * @date 2020/2/18
 */
public class ConfigListModel implements ListModel<String> {

    private ConfigFiles configFiles;

    public ConfigListModel() {
        configFiles = new ConfigFiles();
        List<String> filePathList = new ArrayList<>(KubernetesConfiguration.getInstance().getServerConfigurations());
        configFiles.setFilePathList(filePathList);
    }

    public void addConfig(JBList<String> fileList, String configPath) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream(new File(configPath));
            Map<String, Object> yamlMap = yaml.load(inputStream);
            if (yamlMap != null && yamlMap.get("clusters") != null) {
                if (!KubernetesConfiguration.getInstance().getServerConfigurations().contains(configPath)) {
                    configFiles.getFilePathList().add(configPath);
                    fileList.updateUI();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void removeConfigFile(JBList<String> fileList, int selectedIndex) {
        configFiles.getFilePathList().remove(selectedIndex);
        fileList.updateUI();
    }

    public ConfigFiles getConfigFiles() {
        return configFiles;
    }

    @Override
    public int getSize() {
        return configFiles.getFilePathList().size();
    }

    @Override
    public String getElementAt(int index) {
        return configFiles.getFilePathList().get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
