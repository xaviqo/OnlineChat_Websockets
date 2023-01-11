package tech.xavi.wschatspringvue.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import tech.xavi.wschatspringvue.Globals;

import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Configuration
public class SpamFilterDefaultCfg {

    private static int maxMessageLength_default;
    private static String[] bannedWords_default;
    private static int spamPoints_default;
    private static int secBetweenMessages_default;
    private static boolean loadedDefault;

    static {
        if (!isLoadedDefault()){
            try {
                log.info("LOADING SPAM FILTER PROPERTIES...");
                final Properties properties = new Properties();
                final InputStream is = SpamFilterDefaultCfg.class.getResourceAsStream(Globals.WS_SPAMFILT_PROPERTIES);
                properties.load(is);
                maxMessageLength_default = Integer.valueOf((String) properties.get("tech.xavi.wschat.spam-default.max-msg-length"));
                bannedWords_default = properties.get("tech.xavi.wschat.spam-default.banned-words").toString().split(",");
                spamPoints_default = Integer.valueOf((String) properties.get("tech.xavi.wschat.spam-default.start-points"));
                secBetweenMessages_default = Integer.valueOf((String) properties.get("tech.xavi.wschat.spam-default.sec-between-msg"));
                setLoadedDefault(true);
                log.info("SPAM FILTER PROPERTIES SUCCESSFULLY LOADED!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isLoadedDefault() {
        return loadedDefault;
    }

    public static void setLoadedDefault(boolean loadedDefault) {
        SpamFilterDefaultCfg.loadedDefault = loadedDefault;
    }
}
