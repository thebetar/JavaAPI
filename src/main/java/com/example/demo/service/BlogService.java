package com.example.demo.service;

import com.example.demo.dao.BlogDao;
import com.example.demo.model.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BlogService {

    private final BlogDao blogdao;

    @Autowired
    public BlogService(@Qualifier("DB") BlogDao blogdao) {
        this.blogdao = blogdao;
    }

    public List<Blog> getAllBlogs() {
        return blogdao.selectAllBlogs();
    }

    public Blog getBlogById(UUID id) {
        return blogdao.selectBlogById(id);
    }

    public int insertBlog(Blog blog) {
        return blogdao.insertBlog(blog);
    }

    public int deleteBlog(UUID id) {
        return blogdao.deleteBlogById(id);
    }

    public int updateBlog(UUID id, Blog blog) {
        return blogdao.updateBlogById(id, blog);
    }

}
