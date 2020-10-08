package net.mavroprovato.springcms.component;

import net.mavroprovato.springcms.command.GeneratePostsCommand;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Rebuild the search index when the application starts up.
 */
@Component
@Transactional
public class BuildSearchIndex implements ApplicationListener<ApplicationReadyEvent> {

    /** Logger for the class */
    private static Logger logger = LoggerFactory.getLogger(GeneratePostsCommand.class);

    /** The entity manager */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Create an initial Lucene index for the data already present in the database. This method is called when Spring's
     * startup.
     *
     * @param event The application event.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        try {
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
            fullTextEntityManager.createIndexer().startAndWait();
        }
        catch (InterruptedException ex) {
            logger.error("Error occurred while trying to build the search index", ex);
        }
    }
}
