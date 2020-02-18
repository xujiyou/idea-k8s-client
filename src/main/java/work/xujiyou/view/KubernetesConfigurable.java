package work.xujiyou.view;

import com.intellij.ide.ui.UINumericRange;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.ui.ComboBox;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * KubernetesConfigurable class
 *
 * @author jiyouxu
 * @date 2020/2/18
 */
public class KubernetesConfigurable extends BaseConfigurable implements SearchableConfigurable {

    public static final String PLUGIN_SETTINGS_NAME = "K8s-Client Plugin";

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return PLUGIN_SETTINGS_NAME;
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "preferences.kubernetesOptions";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return new JLabel("Hello world");
    }

    @Override
    public boolean isModified(@NotNull JTextField textField, @NotNull String value) {
        return false;
    }

    @Override
    public boolean isModified(@NotNull JTextField textField, int value, @NotNull UINumericRange range) {
        return false;
    }

    @Override
    public boolean isModified(@NotNull JToggleButton toggleButton, boolean value) {
        return false;
    }

    @Override
    public <T> boolean isModified(@NotNull ComboBox<T> comboBox, T value) {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }

    @Override
    public void reset() { }

    @Override
    public void disposeUIResources() {

    }

    @NotNull
    @Override
    public String getId() {
        return "preferences.kubernetesOptions";
    }

    @Nullable
    @Override
    public Runnable enableSearch(String option) {
        return null;
    }
}
