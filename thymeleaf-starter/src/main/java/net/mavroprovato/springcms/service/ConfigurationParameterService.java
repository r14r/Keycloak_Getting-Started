package net.mavroprovato.springcms.service;

import net.mavroprovato.springcms.entity.ConfigurationParameter;
import net.mavroprovato.springcms.entity.Parameter;
import net.mavroprovato.springcms.repository.ConfigurationParameterRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The configuration parameter service.
 */
@Service
@Transactional
public class ConfigurationParameterService {

    /** The configuration parameter repository. */
    private final ConfigurationParameterRepository configurationParameterRepository;

    /**
     * Create the configuration parameter service.
     *
     * @param configurationParameterRepository The configuration parameter repository.
     */
    public ConfigurationParameterService(ConfigurationParameterRepository configurationParameterRepository) {
        this.configurationParameterRepository = configurationParameterRepository;
    }

    /**
     * Return all parameters as a map.
     *
     * @return All parameters as a map.
     */
    public Map<String, String> allParameters() {
        Map<String, String> params = configurationParameterRepository.findAll().stream().collect(
                Collectors.toMap(ConfigurationParameter::getName, ConfigurationParameter::getValue));

        // Put default values for missing parameters
        Stream.of(Parameter.values())
                .filter(p -> !params.containsKey(p.name()))
                .forEach(p -> params.put(p.name(), p.defaultValue().toString()));

        return params;
    }

    /**
     * Get a string parameter.
     *
     * @param parameter The configuration parameter.
     * @return The configuration parameter value.
     */
    public String getString(Parameter parameter) {
        Optional<ConfigurationParameter> configurationParameter = configurationParameterRepository.findOneByName(
                parameter.name());

        return configurationParameter.map(ConfigurationParameter::getValue).orElse(parameter.defaultValue().toString());
    }

    /**
     * Get an integer parameter.
     *
     * @param parameter The configuration parameter.
     * @return The configuration parameter value.
     */
    public int getInteger(Parameter parameter) {
        Optional<ConfigurationParameter> configurationParameter = configurationParameterRepository.findOneByName(
                parameter.name());

        return configurationParameter
                .map(c -> Integer.valueOf(c.getValue()))
                .orElse(Integer.valueOf(parameter.defaultValue().toString()));
    }
}
