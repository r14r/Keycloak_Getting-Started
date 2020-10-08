package net.mavroprovato.springcms.repository;

import net.mavroprovato.springcms.entity.ConfigurationParameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The category repository
 */
public interface ConfigurationParameterRepository extends JpaRepository<ConfigurationParameter, Integer> {

    /**
     * Find the value of a configuration parameter by name.
     *
     * @param name The configuration parameter name.
     * @return The configuration parameter value.
     */
    Optional<ConfigurationParameter> findOneByName(String name);
}
