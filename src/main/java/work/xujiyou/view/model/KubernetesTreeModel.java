package work.xujiyou.view.model;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.yaml.snakeyaml.Yaml;
import work.xujiyou.KubernetesConfiguration;
import work.xujiyou.constant.ResourcesType;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * KubernetesTreeModel class
 *
 * @author jiyouxu
 * @date 2020/2/13
 */
public class KubernetesTreeModel implements TreeModel {

    private KubernetesNode rootNode = new KubernetesNode(false, "root", ResourcesType.ROOT, "");

    public KubernetesTreeModel() {
        List<KubernetesNode> allClusterNodeList = new ArrayList<>();
        List<String> serverConfigurationList = KubernetesConfiguration.getInstance().getServerConfigurations();
        serverConfigurationList.forEach((configPath) -> {
            if (configPath == null) {
                return;
            }
            try {
                Yaml yaml = new Yaml();
                InputStream inputStream = new FileInputStream(new File(configPath));
                Map<String, Object> yamlMap = yaml.load(inputStream);
                JSONObject clusterConfigJson = new JSONObject(yamlMap);
                JSONArray clusterArray = clusterConfigJson.getJSONArray("clusters");
                clusterArray.toJavaList(Map.class).forEach((cluster) -> {
                    KubernetesNode clusterNode = new KubernetesNode();
                    clusterNode.setLeaf(false);
                    clusterNode.setName((cluster.get("name").toString()));
                    clusterNode.setResourcesType(ResourcesType.CLUSTER);
                    clusterNode.setConfigPath(configPath);
                    clusterNode.setChildren(new ArrayList<>());
                    allClusterNodeList.add(clusterNode);
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        rootNode.setChildren(allClusterNodeList);
    }

    @Override
    public KubernetesNode getRoot() {
        return rootNode;
    }

    @Override
    public KubernetesNode getChild(Object parent, int index) {
        KubernetesNode kubernetesNode = (KubernetesNode) parent;
        return kubernetesNode.getChildren().get(index);
    }

    @Override
    public int getChildCount(Object parent) {
        KubernetesNode kubernetesNode = (KubernetesNode) parent;
        return kubernetesNode.getChildren().size();
    }

    /**
     * 判断当前节点是否有子节点
     * @param node 当前阶段
     * @return 是否可展开
     */
    @Override
    public boolean isLeaf(Object node) {
        KubernetesNode kubernetesNode = (KubernetesNode) node;
        return kubernetesNode.isLeaf();
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {

    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        KubernetesNode parentKubernetesNode = (KubernetesNode) parent;
        KubernetesNode childKubernetesNode = (KubernetesNode) child;
        return parentKubernetesNode.getChildren().indexOf(childKubernetesNode);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {

    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {

    }
}

