package work.xujiyou.view;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * KubernetesToolWindowFactory class
 *
 * @author jiyouxu
 * @date 2020/2/12
 */
public class KubernetesToolWindowFactory implements ToolWindowFactory {

    public static final KubernetesExplorerPanel KUBERNETES_EXPLORER_PANEL = new KubernetesExplorerPanel();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(KUBERNETES_EXPLORER_PANEL, null, false);
        toolWindow.getContentManager().addContent(content);
    }

    @Override
    public void init(ToolWindow window) {
        System.out.println("init");
    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    public boolean isDoNotActivateOnStart() {
        return false;
    }
}
