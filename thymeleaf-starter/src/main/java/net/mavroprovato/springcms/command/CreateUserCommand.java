package net.mavroprovato.springcms.command;

import net.mavroprovato.springcms.entity.Role;
import net.mavroprovato.springcms.entity.User;
import net.mavroprovato.springcms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.annotation.ComponentScan;

import java.util.Scanner;

/**
 * A command that creates users.
 */
@ComponentScan("net.mavroprovato.springcms")
public class CreateUserCommand implements ApplicationRunner {

    /** The user service **/
    private final UserService userService;

    /**
     * Value class to hold the options passed through the command line arguments
     */
    private static final class Options {
        /** The user name */
        String userName;
        /** The user password */
        String password;
        /** The user email */
        String email;
        /** The user role */
        Role role;
    }

    /**
     * Create the command.
     *
     * @param userService The user service.
     */
    @Autowired
    public CreateUserCommand(UserService userService) {
        this.userService = userService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(ApplicationArguments args) {
        Options options = parseArguments(args);
        if (options == null) {
            return;
        }

        User user = new User();
        user.setUserName(options.userName);
        user.setPassword(options.password);
        user.setEmail(options.email);
        user.setRole(options.role);

        userService.save(user);
    }

    /**
     * Parse the command line arguments.
     *
     * @param args The command line arguments.
     * @return The parsed command line arguments as options, or null if a parsing error has occurred.
     */
    private Options parseArguments(ApplicationArguments args) {
        Scanner scanner = new Scanner(System.in);
        Options options = new Options();

        // Read the user name
        if (args.containsOption("username")) {
            options.userName = args.getOptionValues("username").get(0);
        } else {
            System.out.println("Enter the user name: ");
            options.userName = scanner.nextLine();
        }

        // Read the user password
        if (args.containsOption("password")) {
            options.password = args.getOptionValues("password").get(0);
        } else {
            System.out.println("Enter the user password: ");
            options.password = scanner.nextLine();
        }

        // Read the user email
        if (args.containsOption("email")) {
            options.email = args.getOptionValues("email").get(0);
        } else {
            System.out.println("Enter the user email: ");
            options.email = scanner.nextLine();
        }

        // Read the user role
        if (args.containsOption("role")) {
            options.role = Role.valueOf(args.getOptionValues("email").get(0).trim().toUpperCase());
        } else {
            System.out.println("Enter the user role: ");
            options.role = Role.valueOf(scanner.nextLine().trim().toUpperCase());
        }

        return options;
    }

    /**
     * The entry point of the command.
     *
     * @param args The command line arguments.
     */
    public static void main(String... args) {
        SpringApplication command = new SpringApplication(CreateUserCommand.class);
        command.setWebApplicationType(WebApplicationType.NONE);
        command.run(args);
    }
}
