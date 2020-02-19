package work.xujiyou.view;

import com.alibaba.fastjson.JSONObject;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import work.xujiyou.KubernetesConfiguration;
import work.xujiyou.utils.Bash;
import work.xujiyou.view.model.ConfigListModel;
import work.xujiyou.view.model.KubernetesTreeModel;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

/**
 * KubernetesConfigurable class
 *
 * @author jiyouxu
 * @date 2020/2/18
 */
public class SettingConfigurable extends BaseConfigurable implements SearchableConfigurable {

    public static final String PLUGIN_SETTINGS_NAME = "K8s-Client Plugin";

    private JPanel mainPanel;

    private LabeledComponent<TextFieldWithBrowseButton> shellPathField;

    private JBTextArea versionLabel;

    private JBList<String> fileList;


    public SettingConfigurable() {
        mainPanel = new JPanel(new BorderLayout());
        versionLabel = new JBTextArea();
        fileList = new JBList<>();
    }

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
        JPanel kubernetesShellOptionsPanel = new JPanel();
        kubernetesShellOptionsPanel.setLayout(new BorderLayout());
        JLabel label  = new JLabel("Path to Kubectl Shell:");
        label.setFont(new Font(null, Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        kubernetesShellOptionsPanel.add(label, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        shellPathField = createShellPathField();
        testKubectl();
        inputPanel.add(shellPathField);
        inputPanel.add(createTestButton());
        inputPanel.add(createDownloadButton());

        kubernetesShellOptionsPanel.add(inputPanel, BorderLayout.CENTER);

        versionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        versionLabel.setEditable(false);
        versionLabel.setBackground(mainPanel.getBackground());
        versionLabel.setBorder(JBUI.Borders.empty(4, 0));
        kubernetesShellOptionsPanel.add(versionLabel, BorderLayout.SOUTH);

        mainPanel.add(kubernetesShellOptionsPanel, BorderLayout.NORTH);

        JPanel fileListPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Kubernetes config file:");
        title.setBorder(JBUI.Borders.empty(10, 0, 6, 0));
        title.setFont(new Font(null, Font.BOLD, 12));
        fileListPanel.add(title, BorderLayout.NORTH);
        fileList.setEmptyText("No default profile");
        fileList.setModel(new ConfigListModel());
        fileListPanel.add(ToolbarDecorator.createDecorator(fileList)
                .setAddAction(anActionButton -> {
                    addConfigFile();
                }).setRemoveAction(anActionButton -> {
                    removeConfigFile();
                }).disableUpDownActions().createPanel(), BorderLayout.CENTER);

        mainPanel.add(fileListPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private void removeConfigFile() {
        ((ConfigListModel) fileList.getModel()).removeConfigFile(fileList, fileList.getSelectedIndex());
        setModified(true);
    }

    private void addConfigFile() {
        VirtualFile[] virtualFiles = FileChooser.chooseFiles(new FileChooserDescriptor(true, false, false, false, false, true),
                null, null);
        for (VirtualFile virtualFile : virtualFiles) {
            if ("PLAIN_TEXT".equals(virtualFile.getFileType().getName())) {
                ((ConfigListModel) fileList.getModel()).addConfig(fileList, virtualFile.getPath());
                setModified(true);
            }
        }
    }

    private LabeledComponent<TextFieldWithBrowseButton> createShellPathField() {
        LabeledComponent<TextFieldWithBrowseButton> shellPathField = new LabeledComponent<>();
        TextFieldWithBrowseButton component = new TextFieldWithBrowseButton();
        component.getChildComponent().setName("shellPathField");
        shellPathField.setComponent(component);
        shellPathField.getComponent().addBrowseFolderListener("Kubectl Shell Configuration", "", null,
                new FileChooserDescriptor(true, false, false, false, false, false));

        shellPathField.getComponent().setText(KubernetesConfiguration.getInstance().getKubectlPath());
        shellPathField.addPropertyChangeListener(event -> {
            setModified(true);
        });
        return shellPathField;
    }

    private JButton createTestButton() {
        JButton testButton = new JButton("Test");
        testButton.addActionListener(actionEvent -> {
            testKubectl();
        });
        return testButton;
    }

    private JButton createDownloadButton() {
        JButton downloadButton = new JButton("Download");
        downloadButton.addActionListener(actionEvent -> {
            try {
                String url = "https://github.com/kubernetes/kubectl/releases";
                java.net.URI uri = java.net.URI.create(url);
                java.awt.Desktop dp = java.awt.Desktop.getDesktop();
                if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                    dp.browse(uri);
                }
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
        });
        return downloadButton;
    }

    private void testKubectl() {
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                String newPath = shellPathField.getComponent().getText();
                String jsonResult = Bash.exec(newPath + " version -o json");
                if (jsonResult == null) {
                    Messages.showErrorDialog(mainPanel, "It's not kubectl", "Error During Kubectl Shell Path Checking...");
                    return;
                }
                JSONObject jsonObject = JSONObject.parseObject(jsonResult);
                JSONObject clientObject = jsonObject.getJSONObject("clientVersion");
                JSONObject serverObject = jsonObject.getJSONObject("serverVersion");
                StringBuilder versionBuilder = new StringBuilder();
                versionBuilder.append("client: ");
                getVersion(clientObject, versionBuilder);


                if (serverObject != null) {
                    versionBuilder.append(System.getProperty("line.separator"));
                    versionBuilder.append("server: ");
                    getVersion(serverObject, versionBuilder);
                }

                versionLabel.setText(versionBuilder.toString());
            } catch (RuntimeException e) {
                e.printStackTrace();
                Messages.showErrorDialog(mainPanel, "It is not kubectl", "Error During Kubectl Shell Path Checking...");
            } catch (IOException e) {
                e.printStackTrace();
                Messages.showErrorDialog(mainPanel, e.getMessage(), "Error During Kubectl Shell Path Checking...");
            }
        });
    }

    private void getVersion(JSONObject clientObject, StringBuilder versionBuilder) {
        versionBuilder.append("version=");
        versionBuilder.append(clientObject.getString("major")).append(".")
                .append(clientObject.getString("minor")).append(",");
        versionBuilder.append("gitVersion=");
        versionBuilder.append(clientObject.getString("gitVersion")).append(",");
        versionBuilder.append("goVersion=");
        versionBuilder.append(clientObject.getString("goVersion")).append(",");
        versionBuilder.append("platform=");
        versionBuilder.append(clientObject.getString("platform"));
    }

    @Override
    public boolean isModified() {
        String existingShellPath = KubernetesConfiguration.getInstance().getKubectlPath();
        String shellPath = shellPathField.getComponent().getText();
        return !StringUtils.equals(existingShellPath, shellPath)
                || !KubernetesConfiguration.getInstance().getServerConfigurations()
                    .equals(((ConfigListModel) fileList.getModel()).getConfigFiles().getFilePathList());
    }

    @Override
    public void apply() {
        if (isModified()) {
            KubernetesConfiguration.getInstance().updateConfig(
                shellPathField.getComponent().getText(),
                ((ConfigListModel) fileList.getModel()).getConfigFiles().getFilePathList()
            );
            KubernetesToolWindowFactory.KUBERNETES_EXPLORER_PANEL.getTree().setModel(new KubernetesTreeModel());
        }
    }

    @Override
    public void reset() {
        ConfigListModel configListModel = ((ConfigListModel) fileList.getModel());
        configListModel.getConfigFiles()
                .setFilePathList(new ArrayList<>(KubernetesConfiguration.getInstance().getServerConfigurations()));
        fileList.updateUI();
        shellPathField.getComponent().setText(KubernetesConfiguration.getInstance().getKubectlPath());
    }

    @Override
    public void cancel() {
        reset();
        super.cancel();
    }

    @Override
    public void disposeUIResources() {
        mainPanel = null;
        shellPathField = null;
        versionLabel = null;
        fileList = null;
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
