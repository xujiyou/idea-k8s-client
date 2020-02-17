package work.xujiyou.entity;

import lombok.Data;

/**
 * ApiResources class
 *
 * @author jiyouxu
 * @date 2020/2/15
 */
@Data
public class ApiResourcesEntity {

    private String name;

    private String shortNames;

    private String apiGroup;

    private boolean namespaces;

    private String kind;

    private String verbs;
}
