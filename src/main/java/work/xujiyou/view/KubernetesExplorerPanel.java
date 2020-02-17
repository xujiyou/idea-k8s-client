package work.xujiyou.view;

import com.intellij.ide.CommonActionsManager;
import com.intellij.ide.TreeExpander;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import work.xujiyou.KubernetesConfiguration;
import work.xujiyou.utils.GuiUtils;
import work.xujiyou.view.action.explorer.AddServerAction;
import work.xujiyou.view.action.explorer.RefreshServerAction;
import work.xujiyou.view.model.KubernetesNode;
import work.xujiyou.view.model.KubernetesTreeModel;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.net.URL;
import java.util.List;

/**
 * KubernetesExplorerPanel class
 *
 * @author jiyouxu
 * @date 2020/2/12
 */
public class KubernetesExplorerPanel extends JPanel implements Disposable {

    private JPanel rootPanel;
    private JPanel toolBarPanel;
    private JPanel containerpanel;

    private final Tree kubernetesTree;

    private final KubernetesTreeModel kubernetesTreeModel = new KubernetesTreeModel();

    public KubernetesExplorerPanel() {
        this.kubernetesTree = createTree();
        buildGui();
        installActions();
    }

    private void buildGui() {

        rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());

        toolBarPanel = new JPanel();
        toolBarPanel.setLayout(new BorderLayout());

        containerpanel = new JPanel();
        containerpanel.setLayout(new BorderLayout());
        containerpanel.add(new JBScrollPane(kubernetesTree), BorderLayout.CENTER);

        rootPanel.add(toolBarPanel, BorderLayout.NORTH);
        rootPanel.add(containerpanel, BorderLayout.CENTER);



        setLayout(new BorderLayout());
        add(rootPanel, BorderLayout.CENTER);
    }

    private void installActions() {
        final TreeExpander treeExpander = new TreeExpander() {
            @Override
            public void expandAll() {
                kubernetesTree.expandRow(0);
            }

            @Override
            public boolean canExpand() {
                if (getServerConfigurations().isEmpty()) {
                    return false;
                }
                return !kubernetesTree.isExpanded(0);
            }

            @Override
            public void collapseAll() {
                kubernetesTree.collapseRow(0);
            }

            @Override
            public boolean canCollapse() {
                if (getServerConfigurations().isEmpty()) {
                    return false;
                }
                return !kubernetesTree.isCollapsed(0);
            }
        };

        CommonActionsManager actionsManager = CommonActionsManager.getInstance();

        final AnAction expandAllAction = actionsManager.createExpandAllAction(treeExpander, rootPanel);
        final AnAction collapseAllAction = actionsManager.createCollapseAllAction(treeExpander, rootPanel);

        Disposer.register(this, () -> {
            collapseAllAction.unregisterCustomShortcutSet(rootPanel);
            expandAllAction.unregisterCustomShortcutSet(rootPanel);
        });

        DefaultActionGroup actionGroup = new DefaultActionGroup("KubernetesExplorerGroup", false);
        RefreshServerAction refreshServerAction = new RefreshServerAction(this);
        AddServerAction addServerAction = new AddServerAction(this);
        if (ApplicationManager.getApplication() != null) {
            actionGroup.add(addServerAction);
            actionGroup.addSeparator();
            actionGroup.add(refreshServerAction);
            actionGroup.add(expandAllAction);
            actionGroup.add(collapseAllAction);
        }

        GuiUtils.installActionGroupInToolBar(actionGroup, toolBarPanel, ActionManager.getInstance(), "KubernetesExplorerActions", true);
    }

    private List<String> getServerConfigurations() {
        return KubernetesConfiguration.getInstance().getServerConfigurations();
    }

    private Tree createTree() {
        Tree tree = new Tree(kubernetesTreeModel) {

            private final URL pluginSettingsUrl = GuiUtils.class.getResource("/general/add.png");
            private final JLabel emptyLabel = new JLabel(
                    String.format("<html><center>No Kubernetes config available<br><br>You may use <img src=\"%s\"> to add config</center></html>", pluginSettingsUrl)
            );

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (!getServerConfigurations().isEmpty()) {
                    return;
                }

                emptyLabel.setFont(getFont());
                emptyLabel.setBackground(getBackground());
                emptyLabel.setForeground(getForeground());
                Rectangle bounds = getBounds();
                Dimension size = emptyLabel.getPreferredSize();
                emptyLabel.setBounds(0, 0, size.width, size.height);

                int x = (bounds.width - size.width) / 2;
                Graphics g2 = g.create(bounds.x + x, bounds.y + 20, bounds.width, bounds.height);
                try {
                    emptyLabel.paint(g2);
                } finally {
                    g2.dispose();
                }
            }
        };

        tree.getEmptyText().clear();
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setName("kubernetesTree");
        tree.setRootVisible(false);
        tree.setCellRenderer((tree1, value, selected, expanded, leaf, row, hasFocus) -> {
            KubernetesNode kubernetesNode = (KubernetesNode) value;
            JLabel nameLabel = new JLabel(kubernetesNode.getName());
            if (kubernetesNode.getIcon() == null) {
                return nameLabel;
            } else {
                JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                jPanel.add(new JLabel(kubernetesNode.getIcon()));
                jPanel.add(nameLabel);
                return jPanel;
            }
        });


        tree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                tree.setPaintBusy(true);
                ApplicationManager.getApplication().executeOnPooledThread(() -> {
                    KubernetesNode kubernetesNode = (KubernetesNode) event.getPath().getLastPathComponent();
                    kubernetesNode.findResources();
                    tree.setPaintBusy(false);
                    tree.updateUI();
                });
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                System.out.println("treeCollapsed");
            }
        });

        tree.addTreeSelectionListener(event -> {
            KubernetesNode kubernetesNode = (KubernetesNode) event.getPath().getLastPathComponent();

            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem menuItem = new JMenuItem(kubernetesNode.getName());
            popupMenu.add(menuItem);

            tree.setComponentPopupMenu(popupMenu);
        });

        return tree;
    }

    public Tree getTree() {
        return kubernetesTree;
    }

    @Override
    public void dispose() {

    }
}
