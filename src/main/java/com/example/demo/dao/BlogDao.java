package com.example.demo.dao;

import com.example.demo.model.Blog;

import java.util.List;
import java.util.UUID;

public interface BlogDao {

    int insertBlog(UUID id, Blog blog);

    default int insertBlog(Blog blog) {
        UUID id = UUID.randomUUID();
        System.out.println(id);
        return insertBlog(id, blog);
    }

    List<Blog> selectAllBlogs();

    Blog selectBlogById(UUID id);

    Blog selectBlogByTitle(String title);

    int deleteBlogById(UUID id);

    int updateBlogById(UUID id, Blog blog);

}
