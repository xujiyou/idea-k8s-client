package work.xujiyou;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializer;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.XCollection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
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

    @XCollection
    private List<String> serverConfigurations = new ArrayList<>();

    public static KubernetesConfiguration getInstance() {
        return ServiceManager.getService(KubernetesConfiguration.class);
    }

    public List<String> getServerConfigurations() {
        return serverConfigurations;
    }

    @Nullable
    @Override
    public KubernetesConfiguration getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull KubernetesConfiguration kubernetesConfiguration) {
        XmlSerializerUtil.copyBean(kubernetesConfiguration, this);
    }

    @Override
    public void noStateLoaded() {
        String userHome = System.getProperties().getProperty("user.home");
        File defaultFile = new File(userHome + "/.kube/config");
        if (defaultFile.exists()) {
            serverConfigurations.add(defaultFile.getPath());
        }
        XmlSerializer.serialize(this);
    }

    @Override
    public void initializeComponent() { }

    @Override
    public String toString() {
        return "KubernetesConfiguration{" +
                "serverConfigurations=" + serverConfigurations +
                '}';
    }
}
