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
 * Controller for administration actions.
 */
@Controller
@RequestMapping("admin")
public class AdminDashboardController {

    /** The admin service. */
    private final AdminService adminService;

    /**
     * Create the admin controller.
     *
     * @param adminService The admin service.
     */
    @Autowired
    public AdminDashboardController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Display the admin dashboard.
     *
     * @return The model and view.
     */
    @GetMapping("")
    public ModelAndView dashboard() {
        return new ModelAndView("admin/dashboard", adminService.dashboard());
    }
}
