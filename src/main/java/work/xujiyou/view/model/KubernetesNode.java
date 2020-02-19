package work.xujiyou.view.model;

import com.alibaba.fastjson.annotation.JSONType;
import com.google.common.base.Objects;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.text.StringUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import work.xujiyou.api.*;
import work.xujiyou.constant.ResourcesType;
import work.xujiyou.entity.*;

import javax.swing.*;
import java.util.*;

/**
 * KubernetesNode class
 *
 * @author jiyouxu
 * @date 2020/2/14
 */
@Data
@JSONType
@NoArgsConstructor
public class KubernetesNode {

    private String uuid = UUID.randomUUID().toString();

    private boolean leaf;

    private String name;

    private Icon icon;

    private ResourcesType resourcesType;

    private String configPath;

    private List<KubernetesNode> children = new ArrayList<>();

    private Map<String, String> infoMap = new HashMap<>();

    public KubernetesNode(boolean leaf, String name, ResourcesType resourcesType, String configPath) {
        this.leaf = leaf;
        this.name = name;
        this.resourcesType = resourcesType;
        this.configPath = configPath;
    }

    public void findResources() {
        if (children.size() == 0) {
            //二级
            if (resourcesType == ResourcesType.CLUSTER) {
                this.children.add(new KubernetesNode(false, "Node", ResourcesType.NODE, this.configPath));
                this.children.add(new KubernetesNode(false, "ComponentStatus", ResourcesType.COMPONENT_STATUS, this.configPath));
                this.children.add(new KubernetesNode(false, "Namespace", ResourcesType.NAMESPACES, this.configPath));
                this.children.add(new KubernetesNode(false, "PV", ResourcesType.PV, this.configPath));
                this.children.add(new KubernetesNode(false, "StorageClass", ResourcesType.STORAGE_CLASS, this.configPath));
                this.children.add(new KubernetesNode(false, "CRD", ResourcesType.CRD, this.configPath));
            } else if (resourcesType == ResourcesType.NODE) {
                List<NodeEntity> nodeEntityList = NodeApi.findNodes(this.configPath);
                if (nodeEntityList != null) {
                    nodeEntityList.forEach(nodeEntity -> {
                        KubernetesNode kubernetesNode = new KubernetesNode(true, nodeEntity.getName(), ResourcesType.NODE_INSTANCE, this.configPath);
                        kubernetesNode.setIcon(AllIcons.Actions.Checked);
                        this.children.add(kubernetesNode);
                    });
                }
            } else if (resourcesType == ResourcesType.COMPONENT_STATUS) {
                List<ComponentStatusEntity> componentStatusEntityList = ComponentStatusApi.findComponentStatus(this.configPath);
                if (componentStatusEntityList != null) {
                    componentStatusEntityList.forEach(componentStatusEntity -> {
                        KubernetesNode kubernetesNode = new KubernetesNode(true, componentStatusEntity.getName(), ResourcesType.COMPONENT_STATUS_INSTANCE, this.configPath);
                        kubernetesNode.setIcon(AllIcons.Actions.Checked);
                        this.children.add(kubernetesNode);
                    });
                }
            } else if (resourcesType == ResourcesType.NAMESPACES) {
                List<NamespaceEntity> namespaceEntityList = NamespaceApi.findNamespace(this.configPath);
                if (namespaceEntityList != null) {
                    namespaceEntityList.forEach(namespaceEntity -> {
                        KubernetesNode kubernetesNode = new KubernetesNode(false, namespaceEntity.getName(), ResourcesType.NAMESPACES_INSTANCE, this.configPath);
                        this.children.add(kubernetesNode);
                    });
                }
            } else if (resourcesType == ResourcesType.PV) {
                List<PvEntity> pvEntityList = PvApi.findPv(this.configPath);
                if (pvEntityList != null) {
                    pvEntityList.forEach(pvEntity -> {
                        KubernetesNode kubernetesNode = new KubernetesNode(true, pvEntity.getName(), ResourcesType.PV_INSTANCE, this.configPath);
                        this.children.add(kubernetesNode);
                    });
                }
            } else if (resourcesType == ResourcesType.STORAGE_CLASS) {
                List<StorageClassEntity> storageClassEntityList = StorageClassApi.findStorageClass(this.configPath);
                if (storageClassEntityList != null) {
                    storageClassEntityList.forEach(storageClassEntity -> {
                        KubernetesNode kubernetesNode = new KubernetesNode(true, storageClassEntity.getName(), ResourcesType.NAMESPACES_INSTANCE, this.configPath);
                        this.children.add(kubernetesNode);
                    });
                }
            } else if (resourcesType == ResourcesType.CRD) {
                List<CrdEntity> crdEntityList = CrdApi.findCrd(this.configPath);
                if (crdEntityList != null) {
                    crdEntityList.forEach(crdEntity -> {
                        KubernetesNode kubernetesNode = new KubernetesNode(true, crdEntity.getKind(), ResourcesType.CRD_INSTANCE, this.configPath);
                        this.children.add(kubernetesNode);
                    });
                }
            }

            //三级
            if (resourcesType == ResourcesType.NAMESPACES_INSTANCE) {
                List<String> list = NamespaceApi.findNamespaceResourcesType(this.configPath, this.name);
                if (list != null && list.size() != 0) {
                    list.forEach(str -> {
                        if (StringUtil.isNotEmpty(str)) {
                            KubernetesNode kubernetesNode = new KubernetesNode(false, str, ResourcesType.KIND, this.configPath);
                            kubernetesNode.getInfoMap().put("namespaces", this.name);
                            this.children.add(kubernetesNode);
                        }
                    });
                }
            }

            //四级
            if (resourcesType == ResourcesType.KIND) {
                List<KindEntity> kindEntityList = NamespaceApi.findNamespaceOneResourcesList(this.configPath, this.name, this.infoMap.get("namespaces"));
                if (kindEntityList != null) {
                    kindEntityList.forEach(kindEntity -> {
                        KubernetesNode kubernetesNode = new KubernetesNode(true, kindEntity.getName(), ResourcesType.ONE, this.configPath);
                        this.children.add(kubernetesNode);
                    });
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KubernetesNode)) {
            return false;
        }
        KubernetesNode that = (KubernetesNode) o;
        return Objects.equal(getUuid(), that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUuid());
    }
}
