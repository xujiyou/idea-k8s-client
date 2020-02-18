package work.xujiyou.view.model;

import com.alibaba.fastjson.annotation.JSONType;
import com.google.common.base.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * ConfigFile class
 *
 * @author jiyouxu
 * @date 2020/2/18
 */
@Data
@NoArgsConstructor
public class ConfigFiles {

    private String uuid = UUID.randomUUID().toString();

    private List<File> fileList;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigFiles)) {
            return false;
        }
        ConfigFiles that = (ConfigFiles) o;
        return Objects.equal(getUuid(), that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUuid());
    }
}
