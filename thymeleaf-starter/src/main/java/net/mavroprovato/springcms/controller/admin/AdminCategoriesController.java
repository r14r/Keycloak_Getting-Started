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
 * Controller for administrating categories.
 */
@Controller
@RequestMapping("admin/categories")
public class AdminCategoriesController {

    /** The admin service. */
    private final AdminService adminService;

    /**
     * Create the admin categories controller.
     *
     * @param adminService The admin service.
     */
    @Autowired
    public AdminCategoriesController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Display the list of categories.
     *
     * @return The model and view.
     */
    @GetMapping("")
    public ModelAndView listCategories() {
        return new ModelAndView("admin/categories");
    }

    /**
     * Return all categories as a JSON string.
     *
     * @return The response body.
     */
    @GetMapping("list")
    @ResponseBody
    public Map<String, Object> ajaxListCategories(DataTableRequest dataTableRequest) {
        return adminService.listCategories(dataTableRequest);
    }
}
