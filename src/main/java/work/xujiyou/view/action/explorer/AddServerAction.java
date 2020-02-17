package work.xujiyou.view.action.explorer;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;
import work.xujiyou.view.KubernetesExplorerPanel;

/**
 * AddServerAction class
 *
 * @author jiyouxu
 * @date 2020/2/12
 */
public class AddServerAction extends AnAction implements DumbAware {

    private final KubernetesExplorerPanel kubernetesExplorerPanel;

    public AddServerAction(KubernetesExplorerPanel kubernetesExplorerPanel) {
        super("Add Server", "Add a Kubernetes server configuration", AllIcons.General.Add);
        this.kubernetesExplorerPanel = kubernetesExplorerPanel;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

    }

    @Override
    public boolean isDumbAware() {
        return false;
    }
}
