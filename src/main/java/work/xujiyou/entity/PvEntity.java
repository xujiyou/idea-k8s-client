package work.xujiyou.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * PvEntity class
 *
 * @author jiyouxu
 * @date 2020/2/16
 */
@Data
@AllArgsConstructor
public class PvEntity {

    private String name;

    private String capacity;

    private String accessModes;

    private String reclaimPolicy;

    private String status;

    private String claim;

    private String storageClass;
}
