package work.xujiyou.view.action.explorer;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;
import work.xujiyou.view.KubernetesExplorerPanel;

/**
 * RefreshServerAction class
 *
 * @author jiyouxu
 * @date 2020/2/12
 */
public class RefreshServerAction extends AnAction implements DumbAware {

    private static final String REFRESH_TEXT = "Refresh This Server";

    private final KubernetesExplorerPanel kubernetesExplorerPanel;

    public RefreshServerAction(KubernetesExplorerPanel kubernetesExplorerPanel) {
        super(REFRESH_TEXT);
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
