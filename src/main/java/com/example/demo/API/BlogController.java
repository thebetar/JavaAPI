package com.example.demo.API;

import com.example.demo.model.Blog;
import com.example.demo.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/blogs")
@RestController
public class BlogController {

    private final BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public List<Blog> getAllBlogs() {
        return blogService.getAllBlogs();
    }

    @GetMapping(path = "{id}")
    public Blog getBlogById(@PathVariable("id") UUID id) {
        return blogService.getBlogById(id);
    }

    @PostMapping
    public int insertBlog(@Valid @NonNull @RequestBody Blog blog) {
        return blogService.insertBlog(blog);
    }

    @DeleteMapping(path = "{id}")
    public void deleteBlog(@PathVariable("id") UUID id) {
        blogService.deleteBlog(id);
    }

    @PutMapping(path = "{id}")
    public void updateBlog(@PathVariable("id") UUID id, @Valid @NonNull @RequestBody Blog blog) {
        blogService.updateBlog(id, blog);
    }

}
