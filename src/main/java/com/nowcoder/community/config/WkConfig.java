package com.nowcoder.community.config;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

@Configuration
public class WkConfig {

    private static final Logger logger = LoggerFactory.getLogger(WkConfig.class);

    @Value("${wk.image.storage}")
    private String wkImageStorage;

    // @PostConstruct:表示初始化。
    // 类带有@Configuration注解，Spring会认为这是一个配置类，服务启动后,会先实例化这个类。
    // 因为@PostConstruct注解，会自动调用init方法初始化。即服务启动后init方法会调用一次。
    @PostConstruct
    public void init() {
        // 创建wk图片目录
        File file = new File(wkImageStorage);
        if (!file.exists()) {
            file.mkdir();
            logger.info("创建wk图片目录：" + wkImageStorage);
        }
    }
}
