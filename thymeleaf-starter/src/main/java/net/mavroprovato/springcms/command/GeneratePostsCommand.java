package net.mavroprovato.springcms.command;

import net.mavroprovato.springcms.entity.Category;
import net.mavroprovato.springcms.entity.ContentStatus;
import net.mavroprovato.springcms.entity.Post;
import net.mavroprovato.springcms.entity.Tag;
import net.mavroprovato.springcms.entity.User;
import net.mavroprovato.springcms.repository.CategoryRepository;
import net.mavroprovato.springcms.repository.PostRepository;
import net.mavroprovato.springcms.repository.TagRepository;
import net.mavroprovato.springcms.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.annotation.ComponentScan;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A command line command that generates test posts.
 */
@ComponentScan("net.mavroprovato.springcms")
public class GeneratePostsCommand implements ApplicationRunner {

    /** Logger for the class */
    private static Logger logger = LoggerFactory.getLogger(GeneratePostsCommand.class);

    /** The default number of content items to generate */
    private static final int DEFAULT_COUNT = 100;

    /** The default minimum publication date for the content items */
    private static final OffsetDateTime DEFAULT_START_DATE = OffsetDateTime.now().minus(1, ChronoUnit.YEARS);

    /** The default maximum publication date for the content items */
    private static final OffsetDateTime DEFAULT_END_DATE = OffsetDateTime.now();

    /** The default number of tags to apply to a content item */
    private static final int DEFAULT_TAG_COUNT = 2;

    /** The default number of categories to apply to a content item */
    private static final int DEFAULT_CATEGORY_COUNT = 2;

    /** The post repository */
    private final PostRepository postRepository;

    /** The tag repository */
    private final TagRepository tagRepository;

    /** The category repository */
    private final CategoryRepository categoryRepository;

    /** The user repository */
    private final UserRepository userRepository;

    /**
     * Value class to hold the options passed through the command line arguments
     */
    private static final class Options {
        int count = DEFAULT_COUNT;
        OffsetDateTime startDate = DEFAULT_START_DATE;
        OffsetDateTime endDate = DEFAULT_END_DATE;
        int tagCount = DEFAULT_TAG_COUNT;
        int categoryCount = DEFAULT_CATEGORY_COUNT;
    }

    /**
     * Create the generate content command.
     *
     * @param postRepository The post repository.
     * @param tagRepository The tag repository.
     * @param categoryRepository The category repository.
     */
    @Autowired
    public GeneratePostsCommand(PostRepository postRepository, TagRepository tagRepository,
                                CategoryRepository categoryRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(ApplicationArguments args) {
        // Parse the command line options
        Options options = parseArguments(args);
        if (options == null) {
            // An error occurred during parsing
            return;
        }

        // Generate the content items
        logger.info("Generating {} content items between {} and {}.", options.count, options.startDate,
                options.endDate);
        for (int i = 0; i < options.count; i++) {
            Post post = new Post();
            post.setTitle("Test Title");
            post.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                    "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
                    "ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit " +
                    "in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat " +
                    "cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
            post.setStatus(ContentStatus.PUBLISHED);
            post.setPublishedAt(randomDateTime(options.startDate, options.endDate));
            for (Tag tag: getRandomTags(options.tagCount)) {
                post.getTags().add(tag);
            }
            for (Category category: getRandomCategories(options.categoryCount)) {
                post.getCategories().add(category);
            }
            post.setAuthor(getRandomAuthor());

            // Save the post
            postRepository.save(post);

            // Set the slug
            post.setSlug("test-title-" + post.getId());
            postRepository.save(post);
        }
        logger.info("Content items generated.");
    }

    /**
     * Parse the command line arguments.
     *
     * @param args The command line arguments.
     * @return The parsed command line arguments as options, or null if a parsing error has occurred.
     */
    private Options parseArguments(ApplicationArguments args) {
        Options options = new Options();
        // Parse the count argument.
        if (args.containsOption("count")) {
            try {
                options.count = Integer.parseInt(args.getOptionValues("count").get(0));
            } catch (NumberFormatException e) {
                logger.error("Cannot parse the count argument ({}) as an integer.",
                        args.getOptionValues("count").get(0));
                return null;
            }
            if (options.count <= 0) {
                logger.error("Count must be a positive integer.");
                return null;
            }
        }

        // Parse the start date argument
        if (args.containsOption("start-date")) {
            try {
                String startDateString = args.getOptionValues("start-date").get(0);
                options.startDate = LocalDate.parse(
                        startDateString, DateTimeFormatter.ISO_DATE).atStartOfDay().atOffset(ZoneOffset.UTC);
            } catch (DateTimeParseException e) {
                logger.error("Start date ({}) cannot be parsed.", args.getOptionValues("start-date").get(0));
                return null;
            }
        }

        // Parse the end date argument
        if (args.containsOption("end-date")) {
            try {
                String endDateString = args.getOptionValues("end-date").get(0);
                options.endDate = LocalDate.parse(
                        endDateString, DateTimeFormatter.ISO_DATE).atStartOfDay().atOffset(ZoneOffset.UTC);
            } catch (DateTimeParseException e) {
                logger.error("End date ({}) cannot be parsed.", args.getOptionValues("end-date").get(0));
                return null;
            }
        }

        // Check if start date is before end date
        if (!options.startDate.isBefore(options.endDate)) {
            logger.error("Start date must be before end date.");
            return null;
        }

        // Parse the tag count argument.
        if (args.containsOption("tag-count")) {
            try {
                options.tagCount = Integer.parseInt(args.getOptionValues("tag-count").get(0));
            } catch (NumberFormatException e) {
                logger.error("Cannot parse the tag count argument ({}) as an integer.",
                        args.getOptionValues("tag-count").get(0));
                return null;
            }
            if (options.tagCount <= 0) {
                logger.error("Tag count must be a positive integer.");
                return null;
            }
        }

        // Parse the category count argument.
        if (args.containsOption("category-count")) {
            try {
                options.categoryCount = Integer.parseInt(args.getOptionValues("category-count").get(0));
            } catch (NumberFormatException e) {
                logger.error("Cannot parse the category count argument ({}) as an integer.",
                        args.getOptionValues("category-count").get(0));
                return null;
            }
            if (options.categoryCount <= 0) {
                logger.error("Category count must be a positive integer.");
                return null;
            }
        }

        return options;
    }

    /**
     * Generate an iterable with random tags from the database.
     *
     * @param count The number of random tags to fetch.
     * @return An iterable with random tags from the database.
     */
    private Iterable<? extends Tag> getRandomTags(int count) {
        List<Tag> allTags = tagRepository.findAll();
        int postTagCount = Math.min(count, allTags.size());
        List<Tag> postTags = new ArrayList<>();

        for (int i = 0; i < postTagCount; i++) {
            postTags.add(allTags.remove(ThreadLocalRandom.current().nextInt(allTags.size())));
        }

        return postTags;
    }

    /**
     * Generate an iterable with random categories from the database.
     *
     * @param count The number of random categories to fetch.
     * @return An iterable with random categories from the database.
     */
    private Iterable<? extends Category> getRandomCategories(int count) {
        List<Category> allCategories = categoryRepository.findAll();
        int postCategoryCount = Math.min(count, allCategories.size());
        List<Category> postCategories = new ArrayList<>();

        for (int i = 0; i < postCategoryCount; i++) {
            postCategories.add(allCategories.remove(ThreadLocalRandom.current().nextInt(allCategories.size())));
        }

        return postCategories;
    }

    /**
     * Return a random date between two dates.
     *
     * @param startDate The minimum date.
     * @param endDate The maximum date.
     * @return The random date time.
     */
    private OffsetDateTime randomDateTime(OffsetDateTime startDate, OffsetDateTime endDate) {
        long min = startDate.toEpochSecond();
        long max = endDate.toEpochSecond();
        long random = ThreadLocalRandom.current().nextLong(min, max);

        return OffsetDateTime.ofInstant(Instant.ofEpochSecond(random), ZoneId.of("UTC"));
    }

    /**
     * Return a random author from the list of users.
     *
     * @return The random author.
     */
    private User getRandomAuthor() {
        List<User> allUsers = userRepository.findAll();
        if (allUsers.isEmpty()) {
            throw new IllegalStateException("No users are defined.");
        }

        return allUsers.get(ThreadLocalRandom.current().nextInt(allUsers.size()));
    }

    /**
     * The entry point of the command.
     *
     * @param args The command line arguments.
     */
    public static void main(String... args) {
        SpringApplication command = new SpringApplication(GeneratePostsCommand.class);
        command.setWebApplicationType(WebApplicationType.NONE);
        command.run(args);
    }
}
