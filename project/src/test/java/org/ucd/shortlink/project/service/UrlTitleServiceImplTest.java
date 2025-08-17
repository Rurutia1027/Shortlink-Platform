package org.ucd.shortlink.project.service;

import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UrlTitleServiceImplTest {

    private UrlTitleService urlTitleService;

    @BeforeEach
    void setup() {
        urlTitleService = new UrlTitleServiceImpl();
    }

    @Test
    public void testGetTitleByUrl() {
        String testUrl = "https://baidu.com";
        String retContent = urlTitleService.getTitleByUrl(testUrl);
        Assertions.assertNotNull(retContent);
        Assertions.assertTrue(StrUtil.isNotBlank(retContent));
    }
}