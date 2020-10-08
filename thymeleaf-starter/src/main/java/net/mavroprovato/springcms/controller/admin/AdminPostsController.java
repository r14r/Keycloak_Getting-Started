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
 * Controller for administrating posts.
 */
@Controller
@RequestMapping("admin/posts")
public class AdminPostsController {

    /** The admin service. */
    private final AdminService adminService;

    /**
     * Create the admin posts controller.
     *
     * @param adminService The admin service.
     */
    @Autowired
    public AdminPostsController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Display the list of posts.
     *
     * @return The model and view.
     */
    @GetMapping("")
    public ModelAndView listPosts() {
        return new ModelAndView("admin/posts");
    }

    /**
     * Return all posts as a JSON string.
     *
     * @return The response body.
     */
    @GetMapping("/list")
    @ResponseBody
    public Map<String, Object> ajaxListPosts(DataTableRequest dataTableRequest) {
        return adminService.listPosts(dataTableRequest);
    }
}
