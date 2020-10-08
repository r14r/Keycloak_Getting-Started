package net.mavroprovato.springcms.component;

import net.mavroprovato.springcms.entity.Category;
import net.mavroprovato.springcms.entity.Comment;
import net.mavroprovato.springcms.entity.Page;
import net.mavroprovato.springcms.entity.Parameter;
import net.mavroprovato.springcms.entity.Post;
import net.mavroprovato.springcms.entity.Tag;
import net.mavroprovato.springcms.service.ConfigurationParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Utilities for generating URLs.
 */
@Component
public class UrlUtils {

    /** The configuration parameter service */
    private final ConfigurationParameterService configurationParameterService;

    /** The current HTTP request */
    private HttpServletRequest request;

    /**
     * Create the URL utilities.
     *
     * @param configurationParameterService The configuration parameter service.
     */
    @Autowired
    public UrlUtils(ConfigurationParameterService configurationParameterService) {
        this.configurationParameterService = configurationParameterService;
    }

    /**
     * Set the current HTTP request.
     *
     * @param request The current HTTP request.
     */
    @Autowired(required = false)
    public void setMyServiceB(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Return the URL of the next page.
     *
     * @param urlPrefix The URL prefix.
     * @param page The page number.
     * @return The URL of the next page.
     */
    public String olderPage(String urlPrefix, org.springframework.data.domain.Page<?> page) {
        if (!urlPrefix.endsWith("/")) {
            urlPrefix += "/";
        }
        if (page.isLast()) {
            return urlPrefix;
        }

        String url = urlPrefix + "page/" + (page.getNumber() + 2);
        if (request.getQueryString() != null) {
            url += "?" + request.getQueryString();
        }

        return url;
    }

    /**
     * Return the URL of the previous page.
     *
     * @param urlPrefix The URL prefix.
     * @param page The page number.
     * @return The URL of the previous page.
     */
    public String newerPage(String urlPrefix, org.springframework.data.domain.Page<?> page) {
        if (!urlPrefix.endsWith("/")) {
            urlPrefix += "/";
        }
        if (page.isFirst()) {
            return urlPrefix;
        }
        if (page.getNumber() == 1) {
            return urlPrefix;
        }

        String url = urlPrefix + "page/" + page.getNumber();
        if (request.getQueryString() != null) {
            url += "?" + request.getQueryString();
        }

        return url;
    }

    /**
     * Get the URL for post list.
     *
     * @return The URL.
     */
    public String postList() {
        return postListDay(null, null, null, false);
    }

    /**
     * Get the URL for post list.
     *
     * @param absolute If the URL should be absolute or not.
     * @return The URL.
     */
    public String postList(boolean absolute) {
        return postListDay(null, null, null, absolute);
    }

    /**
     * Get the relative URL for post list published in a year.
     *
     * @param year The year.
     * @return The URL.
     */
    public String postListYear(Integer year) {
        return postListDay(year, null, null, false);
    }

    /**
     * Get the URL for post list published in a year.
     *
     * @param year The year.
     * @param absolute If the URL should be absolute or not.
     * @return The URL.
     */
    public String postListYear(Integer year, boolean absolute) {
        return postListDay(year, null, null, absolute);
    }

    /**
     * Get the relative URL for post list published in a month.
     *
     * @param year The year.
     * @param month The month.
     * @return The URL.
     */
    public String postListMonth(Integer year, Integer month) {
        return postListDay(year, month, null, false);
    }

    /**
     * Get the URL for post list published in a month.
     *
     * @param year The year.
     * @param month The month.
     * @param absolute If the URL should be absolute.
     * @return The URL.
     */
    public String postListMonth(Integer year, Integer month, boolean absolute) {
        return postListDay(year, month, null, absolute);
    }

    /**
     * Get the relative URL for post list published in a day.
     *
     * @param year The year.
     * @param month The month.
     * @param day The day.
     * @return The URL.
     */
    public String postListDay(Integer year, Integer month, int day) {
        return postListDay(year, month, day, false);
    }

    /**
     * Get the URL for post list published in a day.
     *
     * @param year The year.
     * @param month The month.
     * @param day The day.
     * @param absolute If the URL should be absolute.
     * @return The URL.
     */
    public String postListDay(Integer year, Integer month, Integer day, boolean absolute) {
        String path;
        if (day != null) {
            path = String.format("/%02d/%02d/%02d", year, month, day);
        } else if (month != null) {
            path = String.format("/%02d/%02d", year, month);
        } else if (year != null) {
            path = String.format("/%02d", year);
        } else {
            path = "/";
        }

        if (absolute) {
            path = getAbsoluteUrl(path);
        }

        return path;
    }

    /**
     * Get the relative URL for the post list by tag.
     *
     * @param tag The tag.
     * @return The URL.
     */
    public String postListByTag(Tag tag) {
        return postListByTag(tag, false);
    }

    /**
     * Get the URL for the post list by tag.
     *
     * @param tag The tag.
     * @param absolute If the URL should be absolute.
     * @return The URL.
     */
    public String postListByTag(Tag tag, boolean absolute) {
        String path;

        if (tag.getSlug() == null) {
            path = String.format("/tag/%d", tag.getId());
        } else {
            path = String.format("/tag/%s", tag.getSlug());
        }

        if (absolute) {
            path = getAbsoluteUrl(path);
        }

        return path;
    }

    /**
     * Get the URL for the post list by category.
     *
     * @param category The category.
     * @return The URL.
     */
    public String postListByCategory(Category category) {
        return postListByCategory(category, false);
    }

    /**
     * Get the URL for the post list by category.
     *
     * @param category The category.
     * @param absolute If the URL should be absolute.
     * @return The URL.
     */
    public String postListByCategory(Category category, boolean absolute) {
        String path;

        if (category.getSlug() == null) {
            path = String.format("/category/%d", category.getId());
        } else {
            path = String.format("/category/%s", category.getSlug());
        }

        if (absolute) {
            path = getAbsoluteUrl(path);
        }

        return path;
    }

    /**
     * Return the relative post URL.
     *
     * @param post The post.
     * @return The post URL.
     */
    public String post(Post post) {
        return post(post, false);
    }

    /**
     * Return the post URL.
     *
     * @param post The post.
     * @param absolute If the URL should be absolute.
     * @return The post URL.
     */
    public String post(Post post, boolean absolute) {
        String path;

        if (post.getSlug() == null) {
            path = String.format("/post/%d", post.getId());
        } else {
            path = String.format("/post/%s", post.getSlug());
        }

        if (absolute) {
            path = getAbsoluteUrl(path);
        }

        return path;
    }

    /**
     * Return the comment url.
     *
     * @param comment The comment.
     * @return The comment url.
     */
    public String comment(Comment comment) {
        if (comment.getPost().getSlug() == null) {
            return "/post/" + comment.getPost().getId() + "#comment-" + comment.getId();
        } else {
            return "/post/" + comment.getPost().getSlug() + "#comment-" + comment.getId();
        }
    }

    /**
     * Return the relative page URL.
     *
     * @param page The page.
     * @return The post URL.
     */
    public String page(Page page) {
        return page(page, false);
    }

    /**
     * Return the post URL.
     *
     * @param page The post.
     * @param absolute If the URL should be absolute.
     * @return The post URL.
     */
    public String page(Page page, boolean absolute) {
        String path;

        if (page.getSlug() == null) {
            path = String.format("/page/%d", page.getId());
        } else {
            path = String.format("/page/%s", page.getSlug());
        }

        if (absolute) {
            path = getAbsoluteUrl(path);
        }

        return path;
    }

    /**
     * Get the absolute URL from the path.
     *
     * @param path The path.
     * @return The absolute URL.
     */
    private String getAbsoluteUrl(String path) {
        String rootUrl = configurationParameterService.getString(Parameter.SITE_URL);
        if (rootUrl == null || rootUrl.isEmpty()) {
            rootUrl = request.getScheme() + "//" + request.getServerName() + ":" + request.getLocalPort();
        }

        return rootUrl + path;
    }
}
