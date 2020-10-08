package net.mavroprovato.springcms.controller;

import net.mavroprovato.springcms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * The page controller.
 */
@Controller
@RequestMapping("/page")
public class PageController {

    /** The page service */
    private final PageService pageService;

    /**
     * Create the controller.
     *
     * @param pageService The content service.
     */
    @Autowired
    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    /**
     * Display the content by its id.
     *
     * @param id The content id.
     * @return The model and view.
     */
    @GetMapping("/id/{id:\\d+}")
    public ModelAndView byId(@PathVariable("id") int id) {
        return new ModelAndView("page", pageService.getById(id));
    }

    /**
     * Display the content by its slug.
     *
     * @param slug The content slug.
     * @return The model and view.
     */
    @GetMapping("/{slug:\\D\\S+}")
    public ModelAndView bySlug(@PathVariable("slug") String slug) {
        return new ModelAndView("page", pageService.getBySlug(slug));
    }

}
