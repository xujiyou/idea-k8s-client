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
public class ConfigListModel implements ListModel<File> {

    private ConfigFiles configFiles;

    public ConfigListModel() {
        configFiles = new ConfigFiles();
        List<File> fileList = new ArrayList<>();
        KubernetesConfiguration.getInstance().getServerConfigurations().forEach(path -> {
            fileList.add(new File(path));
        });
        configFiles.setFileList(fileList);
    }

    public void addConfig(JBList<File> fileList, String configPath) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream(new File(configPath));
            Map<String, Object> yamlMap = yaml.load(inputStream);
            if (yamlMap != null && yamlMap.get("clusters") != null) {
                if (!KubernetesConfiguration.getInstance().getServerConfigurations().contains(configPath)) {
                    configFiles.getFileList().add(new File(configPath));
                    KubernetesConfiguration.getInstance().getServerConfigurations().add(configPath);
                    fileList.updateUI();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void removeConfigFile(JBList<File> fileList, int selectedIndex) {
        configFiles.getFileList().remove(selectedIndex);
        KubernetesConfiguration.getInstance().getServerConfigurations().remove(selectedIndex);
        fileList.updateUI();
    }

    @Override
    public int getSize() {
        return configFiles.getFileList().size();
    }

    @Override
    public File getElementAt(int index) {
        return configFiles.getFileList().get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
