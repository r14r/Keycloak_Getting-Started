package net.mavroprovato.springcms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller that handles authentication
 */
@Controller
public class AuthenticationController {

    /**
     * The login page.
     *
     * @return The login page view name.
     */
    @GetMapping("login")
    public String login() {
        return "login";
    }
}
