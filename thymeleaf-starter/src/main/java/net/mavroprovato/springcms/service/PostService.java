package net.mavroprovato.springcms.service;

import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.atom.Link;
import net.mavroprovato.springcms.component.UrlUtils;
import net.mavroprovato.springcms.entity.Category;
import net.mavroprovato.springcms.entity.Comment;
import net.mavroprovato.springcms.entity.ContentStatus;
import net.mavroprovato.springcms.entity.Parameter;
import net.mavroprovato.springcms.entity.Post;
import net.mavroprovato.springcms.entity.Tag;
import net.mavroprovato.springcms.exception.ResourceNotFoundException;
import net.mavroprovato.springcms.repository.CategoryRepository;
import net.mavroprovato.springcms.repository.CommentRepository;
import net.mavroprovato.springcms.repository.PageRepository;
import net.mavroprovato.springcms.repository.PostRepository;
import net.mavroprovato.springcms.repository.TagRepository;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The post service.
 */
@Service
@Transactional
public class PostService {

    /** The post repository */
    private final PostRepository postRepository;

    /** The page repository */
    private final PageRepository pageRepository;

    /** The tag repository */
    private final TagRepository tagRepository;

    /** The category repository */
    private final CategoryRepository categoryRepository;

    /** The comment repository */
    private final CommentRepository commentRepository;

    /** The configuration parameter service */
    private final ConfigurationParameterService configurationParameterService;

    /** The URL utilities */
    private final UrlUtils urlUtils;

    /** The entity manager */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Create the post service.
     *
     * @param postRepository The post repository.
     * @param pageRepository The page repository
     * @param tagRepository The tag repository
     * @param categoryRepository The category repository.
     * @param commentRepository The comment repository.
     * @param configurationParameterService The configuration parameter service.
     * @param urlUtils The URL utilities.
     */
    @Autowired
    public PostService(PostRepository postRepository, PageRepository pageRepository, TagRepository tagRepository,
                       CategoryRepository categoryRepository, CommentRepository commentRepository,
                       ConfigurationParameterService configurationParameterService, UrlUtils urlUtils) {
        this.postRepository = postRepository;
        this.pageRepository = pageRepository;
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
        this.configurationParameterService = configurationParameterService;
        this.urlUtils = urlUtils;
    }

    /**
     * Get a post page, ordered by publication date.
     *
     * @param page The page number.
     * @return The content items.
     */
    public Map<String, ?> list(int page) {
        return listImpl(null, null, null, page);
    }

    /**
     * Get a page of posts published in a year, ordered by publication date.
     *
     * @param year The year.
     * @param page The page number.
     * @return The posts.
     */
    public Map<String, ?> list(int year, int page) {
        return listImpl(year, null, null, page);
    }

    /**
     * Get a page of posts published in a month, ordered by publication date.
     *
     * @param year The year.
     * @param month The month number (1 for January, 12 for December)
     * @param page The page number.
     * @return The posts.
     */
    public Map<String, ?> list(int year, int month, int page) {
        return listImpl(year, month, null, page);
    }

    /**
     * Get a page of posts published in a day, ordered by publication date.
     *
     * @param year The year.
     * @param month The month number (1 for January, 12 for December)
     * @param day The day number.
     * @param page The page number.
     * @return The posts.
     */
    public Map<String, ?> list(int year, int month, int day, int page) {
        return listImpl(year, month, day, page);
    }

    /**
     * Get a page of posts, ordered by publication date.
     *
     * @param year The year.
     * @param month The month number (1 for January, 12 for December)
     * @param day The day number.
     * @param page The page number.
     * @return The posts.
     */
    private Map<String, ?> listImpl(Integer year, Integer month, Integer day, int page) {
        // Calculate the start/end publication date to use for the content query, and the url prefix for the pagination
        // links.
        OffsetDateTime startDateTime = null;
        OffsetDateTime endDateTime = null;
        String urlPrefix = urlUtils.postList();
        if (year != null && month != null && day != null) {
            LocalDate date = LocalDate.of(year, month, day);
            startDateTime = OffsetDateTime.of(date, LocalTime.MIN, ZoneOffset.UTC);
            endDateTime = OffsetDateTime.of(date, LocalTime.MAX, ZoneOffset.UTC);
            urlPrefix = urlUtils.postListDay(year, month, day);
        } else if (year != null && month != null) {
            LocalDate startDate = LocalDate.of(year, month, 1);
            startDateTime = OffsetDateTime.of(startDate, LocalTime.MIN, ZoneOffset.UTC);
            LocalDate endDate = startDate.withDayOfMonth(startDate.getMonth().length(startDate.isLeapYear()));
            endDateTime = OffsetDateTime.of(endDate, LocalTime.MAX, ZoneOffset.UTC);
            urlPrefix = urlUtils.postListMonth(year, month);
        } else if (year != null) {
            LocalDate startDate = LocalDate.of(year, Month.JANUARY.getValue(), 1);
            startDateTime = OffsetDateTime.of(startDate, LocalTime.MIN, ZoneOffset.UTC);
            LocalDate endDate = LocalDate.of(year, Month.DECEMBER.getValue(), 31);
            endDateTime = OffsetDateTime.of(endDate, LocalTime.MIN, ZoneOffset.UTC);
            urlPrefix = urlUtils.postListYear(year);
        }

        // Run the query
        int postsPerPage = configurationParameterService.getInteger(Parameter.POSTS_PER_PAGE);
        PageRequest pageRequest = PageRequest.of(page - 1, postsPerPage, Sort.Direction.DESC, "publishedAt");
        Page<Post> posts = startDateTime == null ?
                postRepository.findByStatus(ContentStatus.PUBLISHED, pageRequest) :
                postRepository.findByStatusAndPublishedAtBetween(
                        ContentStatus.PUBLISHED, startDateTime, endDateTime, pageRequest);

        return getListModel(posts, urlPrefix);
    }

    /**
     * Get a page of posts under a specific tag, specified by its id.
     *
     * @param id The tag identifier.
     * @param page The page.
     * @return The posts.
     */
    public Map<String, ?> listByTagId(int id, int page) {
        return tagRepository.findById(id)
                .map(t -> getTagListModel(t, page))
                .orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Get a page of posts under a specific tag, specified by its slug.
     *
     * @param slug The tag slug.
     * @param page The page.
     * @return The posts.
     */
    public Map<String, ?> listByTagSlug(String slug, int page) {
        return tagRepository.findBySlug(slug)
                .map(t -> getTagListModel(t, page))
                .orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Return the model for the list posts by tag.
     *
     * @param tag The tag.
     * @param page The page number.
     * @return The model.
     */
    private Map<String, ?> getTagListModel(Tag tag, int page) {
        int postsPerPage = configurationParameterService.getInteger(Parameter.POSTS_PER_PAGE);
        PageRequest pageRequest = PageRequest.of(page - 1, postsPerPage, Sort.Direction.DESC, "publishedAt");
        Page<Post> posts = postRepository.findByStatusAndTags(ContentStatus.PUBLISHED, tag, pageRequest);

        return getListModel(posts, urlUtils.postListByTag(tag));
    }

    /**
     * Get a page of posts categorized with a specific category, specified by its id.
     *
     * @param id The category identifier.
     * @param page The page.
     * @return The posts.
     */
    public Map<String, ?> listByCategoryId(int id, int page) {
        return categoryRepository.findById(id)
                .map(c -> getCategoryListModel(c, page))
                .orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Get a page of posts categorized with a specific category, specified by its slug.
     *
     * @param slug The category slug.
     * @param page The page.
     * @return The posts.
     */
    public Map<String, ?> listByCategorySlug(String slug, int page) {
        return categoryRepository.findBySlug(slug)
                .map(c -> getCategoryListModel(c, page))
                .orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Return the model for the list posts by category.
     *
     * @param category The category.
     * @param page The page number.
     * @return The model.
     */
    private Map<String, ?> getCategoryListModel(Category category, int page) {
        int postsPerPage = configurationParameterService.getInteger(Parameter.POSTS_PER_PAGE);
        PageRequest pageRequest = PageRequest.of(page - 1, postsPerPage, Sort.Direction.DESC, "publishedAt");
        Page<Post> posts = postRepository.findByStatusAndCategories(ContentStatus.PUBLISHED, category, pageRequest);

        return getListModel(posts, urlUtils.postListByCategory(category));
    }

    /**
     * Perform a full text search on posts.
     *
     * @param queryString The query string.
     * @return The posts.
     */
    public Map<String, ?> search(String queryString, int page) {
        int postsPerPage = configurationParameterService.getInteger(Parameter.POSTS_PER_PAGE);
        PageRequest pageRequest = PageRequest.of(page - 1, postsPerPage);
        // Perform the full text search
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
                .forEntity(Post.class).get();
        Query query = queryBuilder
                .keyword()
                .onFields("title", "content")
                .matching(queryString)
                .createQuery();
        FullTextQuery jpaQuery = fullTextEntityManager
                .createFullTextQuery(query, Post.class)
                .setFirstResult(postsPerPage * pageRequest.getPageNumber())
                .setMaxResults(postsPerPage);
        @SuppressWarnings("unchecked")
        List<Post> results = jpaQuery.getResultList();
        int totalResults = jpaQuery.getResultSize();

        // Create the page with the results.
        Page<Post> posts = new PageImpl<>(results, pageRequest, totalResults);

        return getListModel(posts, "/search");
    }

    /**
     * Return the posts page model.
     *
     * @param posts The posts.
     * @param urlPrefix The URL prefix.
     * @return The posts page model.
     */
    private Map<String, ?> getListModel(Page<Post> posts, String urlPrefix) {
        Map<String, Object> model = new HashMap<>();
        model.put("posts", posts);
        model.put("urlPrefix", urlPrefix);
        addCommonModel(model);

        return model;
    }

    /**
     * Return the model for a post page.
     *
     * @param id The content item identifier.
     * @return The page model.
     */
    public Map<String, ?> getById(int id) {
        return postRepository.findById(id).map(this::getPostModel).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Return the model for a post page.
     *
     * @param slug The content slug.
     * @return The post model.
     */
    public Map<String, ?> getBySlug(String slug) {
        return postRepository.findOneBySlug(slug).map(this::getPostModel).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Get the model for the post page.
     *
     * @param post The post.
     * @return The model for the post page.
     */
    private Map<String, ?> getPostModel(Post post) {
        Map<String, Object> model = new HashMap<>();
        // Make sure comments are loaded
        post.getComments().size();
        model.put("post", post);
        addCommonModel(model);

        return model;
    }

    /**
     * Add model objects common for all content pages.
     *
     * @param model The model.
     */
    private void addCommonModel(Map<String, Object> model) {
        model.put("archives", postRepository.countByMonth());
        model.put("categories", categoryRepository.findAllByOrderByNameAsc());
        model.put("config", configurationParameterService.allParameters());
        model.put("pages", pageRepository.findAll());
    }

    /**
     * Add a new comment to a content item.
     *
     * @param postId The post identifier.
     * @param comment The comment.
     */
    public void addComment(int postId, Comment comment) {
        Optional<Post> post = postRepository.findById(postId);
        post.ifPresent(p -> {
            comment.setPost(p);
            p.getComments().add(comment);
            commentRepository.save(comment);
        });
        post.orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Return a feed with the latest posts.
     *
     * @return a feed with the latest posts.
     */
    public Feed latestPostsFeed() {
        // Get the posts to include
        int postsPerPage = configurationParameterService.getInteger(Parameter.POSTS_PER_PAGE);
        PageRequest pageRequest = PageRequest.of(0, postsPerPage, Sort.Direction.DESC, "publishedAt");
        Page<Post> posts = postRepository.findByStatus(ContentStatus.PUBLISHED, pageRequest);

        return createFeed(posts.stream().map(post -> {
            Entry entry = new Entry();

            entry.setTitle(post.getTitle());
            Link link = new Link();
            link.setHref(urlUtils.post(post));
            entry.setAlternateLinks(Collections.singletonList(link));
            com.rometools.rome.feed.atom.Content summary = new com.rometools.rome.feed.atom.Content();
            summary.setType("text/plain");
            summary.setValue(post.getContent());
            entry.setSummary(summary);
            entry.setCreated(new Date(post.getCreatedAt().toInstant().toEpochMilli()));
            entry.setUpdated(new Date(post.getUpdatedAt().toInstant().toEpochMilli()));
            entry.setPublished(new Date(post.getPublishedAt().toInstant().toEpochMilli()));

            return entry;
        }).collect(Collectors.toList()));
    }

    /**
     * Return a feed with the latest comments.
     *
     * @return a feed with the latest comments.
     */
    public Feed latestCommentsFeed() {
        // Get the comments items to include
        int postsPerPage = configurationParameterService.getInteger(Parameter.POSTS_PER_PAGE);
        PageRequest pageRequest = PageRequest.of(0, postsPerPage, Sort.Direction.DESC, "createdAt");
        Page<Comment> comments = commentRepository.findPublished(pageRequest);

        return createFeed(comments.stream().map(comment -> {
            Entry entry = new Entry();

            entry.setTitle("Comment");
            Link link = new Link();
            link.setHref(urlUtils.comment(comment));
            entry.setAlternateLinks(Collections.singletonList(link));
            com.rometools.rome.feed.atom.Content summary = new com.rometools.rome.feed.atom.Content();
            summary.setType("text/plain");
            summary.setValue(comment.getComment());
            entry.setSummary(summary);
            entry.setCreated(new Date(comment.getCreatedAt().toInstant().toEpochMilli()));
            entry.setUpdated(new Date(comment.getUpdatedAt().toInstant().toEpochMilli()));

            return entry;
        }).collect(Collectors.toList()));
    }

    /**
     * Create a feed.
     *
     * @param entries The feed entries.
     * @return The feed.
     */
    private Feed createFeed(List<Entry> entries) {
        Feed feed = new Feed();
        feed.setFeedType("atom_1.0");
        feed.setTitle(configurationParameterService.getString(Parameter.TITLE));

        com.rometools.rome.feed.atom.Content subtitle = new com.rometools.rome.feed.atom.Content();
        subtitle.setType("text/plain");
        subtitle.setValue(configurationParameterService.getString(Parameter.SUBTITLE));
        feed.setSubtitle(subtitle);

        Link feedLink = new Link();
        feedLink.setHref(urlUtils.postList());
        feed.setAlternateLinks(Collections.singletonList(feedLink));
        feed.setEntries(entries);

        return feed;
    }
}
