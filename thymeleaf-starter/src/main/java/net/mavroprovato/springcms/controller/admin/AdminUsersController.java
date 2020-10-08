package net.mavroprovato.springcms.controller.admin;

import net.mavroprovato.springcms.datatables.DataTableRequest;
import net.mavroprovato.springcms.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Controller for administrating users.
 */
@Controller
@RequestMapping("admin/users")
public class AdminUsersController {

    /** The admin service. */
    private final AdminService adminService;

    /**
     * Create the admin users controller.
     *
     * @param adminService The admin service.
     */
    @Autowired
    public AdminUsersController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Display the list of users.
     *
     * @return The model and view.
     */
    @GetMapping("")
    public ModelAndView listUsers() {
        return new ModelAndView("admin/users");
    }

    /**
     * Return all users as a JSON string.
     *
     * @return The response body.
     */
    @GetMapping("list")
    @ResponseBody
    public Map<String, Object> ajaxListUsers(DataTableRequest dataTableRequest) {
        return adminService.listUsers(dataTableRequest);
    }
}
