package issac.study.cache.config;

import issac.study.cache.core.config.ConfigKey;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class FtpConfig {
    @ConfigKey("ftp.username")
    private String username = "administrator";
    @ConfigKey("ftp.password")
    private String password = "root";
    @ConfigKey("ftp.activeMode")
    private Boolean activeMode = true;
    @ConfigKey("ftp.host")
    private String host = "192.168.99.2";
    @ConfigKey("ftp.port")
    private Integer port = 21;
}
