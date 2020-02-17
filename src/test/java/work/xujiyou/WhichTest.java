package work.xujiyou;

import com.intellij.openapi.util.text.StringUtil;
import work.xujiyou.utils.Bash;

import java.io.IOException;

/**
 * WhichTest class
 *
 * @author jiyouxu
 * @date 2020/2/17
 */
public class WhichTest {

    public static void main(String[] args) {
        try {
            String kubectlPath = Bash.exec("which kubectl");
            if (StringUtil.isNotEmpty(kubectlPath)) {
                kubectlPath = kubectlPath.replaceAll(System.getProperty("line.separator"), "");
                System.out.println("hehe:" + kubectlPath + ":hehe");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
