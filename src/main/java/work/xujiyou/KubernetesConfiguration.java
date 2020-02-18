package work.xujiyou;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.XmlSerializer;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.XCollection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import work.xujiyou.utils.Bash;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * KubernetesConfiguration class
 *
 * @author jiyouxu
 * @date 2020/2/13
 */
@State(
    name = "KubernetesConfiguration",
    storages = {
        @Storage("k8s-client.xml")
    }
)
public class KubernetesConfiguration implements PersistentStateComponent<KubernetesConfiguration> {

    @Attribute
    private String kubectlPath;

    @XCollection
    private List<String> serverConfigurations = new ArrayList<>();

    private static KubernetesConfiguration kubernetesConfiguration;

    public static KubernetesConfiguration getInstance() {
        if (kubernetesConfiguration == null) {
            kubernetesConfiguration = ServiceManager.getService(KubernetesConfiguration.class);
        }
        return kubernetesConfiguration;
    }

    public List<String> getServerConfigurations() {
        return serverConfigurations;
    }

    public String getKubectlPath() {
        return kubectlPath;
    }

    public boolean isCompleteConfig() {
        return kubectlPath != null && serverConfigurations.size() != 0;
    }

    public void updateConfig(String kubectlPath, List<String> fileList) {
        this.kubectlPath = kubectlPath;
        this.serverConfigurations = new ArrayList<>(fileList);
    }

    @Nullable
    @Override
    public KubernetesConfiguration getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull KubernetesConfiguration kubernetesConfiguration) {
        XmlSerializerUtil.copyBean(kubernetesConfiguration, this);
        XmlSerializer.serialize(this);
    }

    @Override
    public void noStateLoaded() {
        String userHome = System.getProperties().getProperty("user.home");
        File defaultFile = new File(userHome + "/.kube/config");
        if (defaultFile.exists()) {
            serverConfigurations.add(defaultFile.getPath());
        }
        try {
            String kubectlPath = Bash.exec("which kubectl");
            if (StringUtil.isNotEmpty(kubectlPath)) {
                this.kubectlPath = kubectlPath.replaceAll(System.getProperty("line.separator"), "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        XmlSerializer.serialize(this);
    }

    @Override
    public void initializeComponent() { }

    @Override
    public String toString() {
        return "KubernetesConfiguration{" +
                "kubectlPath='" + kubectlPath + '\'' +
                ", serverConfigurations=" + serverConfigurations +
                '}';
    }
}
