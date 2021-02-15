package io.github.jiashunx.app.miniblog;

import org.junit.Assert;

/**
 * @author jiashunx
 */
public class Test {

    @org.junit.Test
    public void test() {
        String requestUrl = "/2021/02/23/article-short-id";
        Assert.assertTrue(requestUrl.matches("^/\\d{4}/\\d{1,2}/\\d{1,2}/\\S+$"));
        requestUrl = "/tags/tags-name";
        Assert.assertTrue(requestUrl.matches("^/tags/\\S+$"));
        requestUrl = "/categories/category-name";
        Assert.assertTrue(requestUrl.matches("^/categories/\\S+$"));
        requestUrl = "/page/11";
        Assert.assertTrue(requestUrl.matches("^/page/\\d+$"));
    }

}
