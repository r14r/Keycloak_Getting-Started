package net.mavroprovato.springcms.command;

import net.mavroprovato.springcms.entity.Category;
import net.mavroprovato.springcms.entity.Tag;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

/**
 * A command that creates users.
 */
@ComponentScan("net.mavroprovato.springcms")
public class GenerateTaxonomyCommand implements ApplicationRunner {

    /** The entity manager */
    private EntityManager entityManager;

    /**
     * Create the command.
     *
     * @param entityManager The entity manager.
     */
    public GenerateTaxonomyCommand(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        // Create tags
        for (int i = 1; i <= 5; i++) {
            Tag tag = new Tag();
            tag.setName("Tag " + i);
            tag.setSlug("tag-" + i);
            tag.setDescription("Tag " + i);

            entityManager.persist(tag);
        }

        // Create categories
        for (int i = 1; i <= 5; i++) {
            Category category = new Category();
            category.setName("Category " + i);
            category.setSlug("category-" + i);
            category.setDescription("Category " + i);

            entityManager.persist(category);
        }
    }

    /**
     * The entry point of the command.
     *
     * @param args The command line arguments.
     */
    public static void main(String... args) {
        SpringApplication command = new SpringApplication(GenerateTaxonomyCommand.class);
        command.setWebApplicationType(WebApplicationType.NONE);
        command.run(args);
    }
}
