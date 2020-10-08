package net.mavroprovato.springcms.service;

import net.mavroprovato.springcms.entity.User;
import net.mavroprovato.springcms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

/**
 * The user service.
 */
@Service
@Transactional
public class UserService implements UserDetailsService {

    /** The user repository */
    private final UserRepository userRepository;

    /**
     * Create the user service.
     *
     * @param userRepository The user repository.
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Load a user by the user name.
     *
     * @param userName The user name.
     * @return The user.
     * @throws UsernameNotFoundException If the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findOneByUserName(userName)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUserName(), user.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()))))
                .orElseThrow(() -> new UsernameNotFoundException(userName));
    }

    /**
     * Save a user.
     *
     * @param user The user to save.
     */
    public void save(User user) {
        user.setPassword(encoder().encode(user.getPassword()));

        userRepository.save(user);
    }

    /**
     * The password encoder used.
     *
     * @return The password encoder.
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
