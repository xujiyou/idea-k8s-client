package work.xujiyou.constant;

/**
 * ResourcesType class
 *
 * @author jiyouxu
 * @date 2020/2/14
 */
public enum ResourcesType {

    /**
     * 跟节点
     */
    ROOT,

    /**
     * 集群
     */
    CLUSTER,

    /**
     * 命名空间
     */
    NAMESPACES,

    NAMESPACES_INSTANCE,

    /**
     * 节点
     */
    NODE,

    /**
     * 节点实例
     */
    NODE_INSTANCE,

    /**
     * 组件状态
     */
    COMPONENT_STATUS,

    /**
     * 组件实例
     */
    COMPONENT_STATUS_INSTANCE,

    /**
     * pv
     */
    PV,


    PV_INSTANCE,

    /**
     * StorageClass
     */
    STORAGE_CLASS,

    /**
     * crd
     */
    CRD,

    CRD_INSTANCE,

    /**
     * 资源，如 Pod，Service，Deployment 等
     */
    KIND,

    /**
     * 一个实例，如一个Pod，一个 Service 等
     */
    ONE

}
