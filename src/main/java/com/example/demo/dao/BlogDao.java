package com.example.demo.dao;

import com.example.demo.model.Blog;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BlogDao {

    int insertBlog(UUID id, LocalDate date, Blog blog);

    default int insertBlog(Blog blog) {
        UUID id = UUID.randomUUID();
        LocalDate date = (blog.getDate() == null) ? LocalDate.now() : blog.getDate();
        System.out.println(date);
        return insertBlog(id, date, blog);
    }

    List<Blog> selectAllBlogs();

    Blog selectBlogById(UUID id);

    Blog selectBlogByTitle(String title);

    int deleteBlogById(UUID id);

    int updateBlogById(UUID id, Blog blog);

}
