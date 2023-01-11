package tech.xavi.wschatspringvue.configuration.spamfilter;

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
    private static boolean allowUrls_default;
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
                spamPoints_default = Integer.valueOf((String) properties.get("tech.xavi.wschat.spam-default.max-points"));
                secBetweenMessages_default = Integer.valueOf((String) properties.get("tech.xavi.wschat.spam-default.sec-between-msg"));
                allowUrls_default = properties.get("tech.xavi.wschat.spam-default.allow-urls").toString().equals("true");
                setLoadedDefault(true);
                log.info("SPAM FILTER PROPERTIES SUCCESSFULLY LOADED!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int getMaxMessageLength_default() {
        return maxMessageLength_default;
    }

    public static String[] getBannedWords_default() {
        return bannedWords_default;
    }

    public static int getSpamPoints_default() {
        return spamPoints_default;
    }

    public static int getSecBetweenMessages_default() {
        return secBetweenMessages_default;
    }

    public static boolean isAllowUrls_default() {
        return allowUrls_default;
    }

    public static boolean isLoadedDefault() {
        return loadedDefault;
    }

    public static void setLoadedDefault(boolean loadedDefault) {
        SpamFilterDefaultCfg.loadedDefault = loadedDefault;
    }
}
