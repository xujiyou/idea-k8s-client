package work.xujiyou.view.action.explorer;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import work.xujiyou.view.KubernetesConfigurable;

/**
 * OpenPluginSettingsAction class
 *
 * @author jiyouxu
 * @date 2020/2/17
 */

public class OpenPluginSettingsAction extends AnAction implements DumbAware {

    public OpenPluginSettingsAction() {
        super("Kubernetes Client General Settings", "Edit the k8s-client settings for the current project", AllIcons.General.Settings);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        ShowSettingsUtil.getInstance().showSettingsDialog(null, KubernetesConfigurable.PLUGIN_SETTINGS_NAME);
    }
}
