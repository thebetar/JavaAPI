package com.example.demo.dao;

import com.example.demo.model.Blog;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository("DB")
public class BlogDataAccessService implements BlogDao {

    public static String DB_NAME = "blogs.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:/Users/Lars Vonk/Desktop/Courses/Java/demo/" + DB_NAME;

    public static final String TABLE_BLOGS = "blogs";
    public static final String COLUMN_BLOG_ID = "_id";
    public static final String COLUMN_BLOG_TITLE = "title";
    public static final String COLUMN_BLOG_DESC = "description";
    public static final String COLUMN_BLOG_DATE = "date";
    public static final String COLUMN_BLOG_CATEGORY = "category";

    public static final String QUERY_BLOGS = "SELECT * FROM " + TABLE_BLOGS;
    public static final String QUERY_BLOG_BY_ID = "SELECT * FROM " + TABLE_BLOGS +
            " WHERE " + COLUMN_BLOG_ID + " = ?";
    public static final String QUERY_BLOG_BY_TITLE = "SELECT * FROM " + TABLE_BLOGS +
            " WHERE " + COLUMN_BLOG_TITLE + " = ?";
    public static final String CREATE_BLOG = "INSERT INTO " + TABLE_BLOGS + " (" +
            COLUMN_BLOG_ID + ", " +
            COLUMN_BLOG_TITLE + ", " +
            COLUMN_BLOG_DESC + ", " +
            COLUMN_BLOG_CATEGORY + ", " +
            COLUMN_BLOG_DATE + ") VALUES (?, ?, ?, ?, ?)";
    public static final String DELETE_BLOG = "DELETE FROM " + TABLE_BLOGS +
            " WHERE " + COLUMN_BLOG_ID + " = ?";
    public static final String UPDATE_BLOG = "UPDATE " + TABLE_BLOGS + " SET " +
            COLUMN_BLOG_TITLE + " = ?, " +
            COLUMN_BLOG_DESC + " = ?, " +
            COLUMN_BLOG_DATE + " = ?" +
            " WHERE " + COLUMN_BLOG_ID + " = ?";

    private Connection conn;

    private PreparedStatement queryBlogs;
    private PreparedStatement queryBlogById;
    private PreparedStatement queryBlogByTitle;
    private PreparedStatement insertBlog;
    private PreparedStatement deleteBlog;
    private PreparedStatement updateBlog;

    public BlogDataAccessService() {
        open();
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);

            queryBlogs = conn.prepareStatement(QUERY_BLOGS);
            queryBlogById = conn.prepareStatement(QUERY_BLOG_BY_ID);
            queryBlogByTitle = conn.prepareStatement(QUERY_BLOG_BY_TITLE);
            insertBlog = conn.prepareStatement(CREATE_BLOG);
            deleteBlog = conn.prepareStatement(DELETE_BLOG);
            updateBlog = conn.prepareStatement(UPDATE_BLOG);

            System.out.println("Connected to DB");

            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred when opening DB");
            return false;
        }
    }

    public void close() {
        try {
            if(queryBlogs != null) {
                queryBlogs.close();
            }

            if(queryBlogById != null) {
                queryBlogById.close();
            }

            if(queryBlogByTitle != null) {
                queryBlogByTitle.close();
            }

            if(insertBlog != null) {
                insertBlog.close();
            }

            if(deleteBlog != null) {
                deleteBlog.close();
            }

            if(updateBlog != null) {
                updateBlog.close();
            }

            if(conn != null) {
                conn.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred when closing application");
        }
    }

    @Override
    public int insertBlog(UUID id, LocalDate date, Blog blog) {
        Blog checkBlog = selectBlogByTitle(blog.getTitle());
        if(checkBlog != null) {
            System.out.println(checkBlog.getTitle());
            System.out.println("Blog title already taken!");
            return 0;
        }
        try {
            insertBlog.setString(1, id.toString());
            insertBlog.setString(2, blog.getTitle());
            insertBlog.setString(3, blog.getDescription());
            insertBlog.setString(4, blog.getCategory());
            insertBlog.setString(5, date.toString());

            int affectedRows = insertBlog.executeUpdate();

            if(affectedRows != 1) {
                throw new SQLException("Couldn't insert song!");
            }

            return 1;
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong in the insert");
            return 0;
        }
    }

    @Override
    public List<Blog> selectAllBlogs() {
        try {
            ResultSet results = queryBlogs.executeQuery();

            ArrayList<Blog> blogs = new ArrayList<>();
            while(results.next()) {
                Blog blog = new Blog(
                        UUID.fromString(results.getString(1)),
                        results.getString(2),
                        results.getString(3),
                        results.getString(5), // Is being parsed a little wrong, because category got added alter
                        LocalDate.parse(results.getString(4))
                );
                blogs.add(blog);
            }

            return blogs;
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong loading all blogs");
        }

        return null;
    }

    @Override
    public Blog selectBlogById(UUID id) {
        try {
            queryBlogById.setString(1, id.toString());
            ResultSet results = queryBlogById.executeQuery();

            results.next();

            Blog blog = new Blog(UUID.fromString(results.getString(1)), results.getString(2), results.getString(3), results.getString(5), LocalDate.parse(results.getString(4)));

            return blog;
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Error with finding blog with id " + id);
        }

        return null;
    }

    @Override
    public Blog selectBlogByTitle(String title) {
        try {
            queryBlogByTitle.setString(1, title);
            ResultSet results = queryBlogByTitle.executeQuery();

            while(results.next()) {
                Blog blog = new Blog(UUID.fromString(results.getString(1)), results.getString(2), results.getString(3), results.getString(5), LocalDate.parse(results.getString(4)));

                return blog;
            }
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Error with finding blog with id " + title);
        }

        return null;
    }

    @Override
    public int deleteBlogById(UUID id) {
        try {
            deleteBlog.setString(1, id.toString());

            int affectedRows = deleteBlog.executeUpdate();
            if(affectedRows != 1) {
                System.out.println("Something went wrong deleting blogpost with id " + id.toString());
                return 0;
            }

            return 1;
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong in the deletion of a post");
        }

        return 0;
    }

    @Override
    public int updateBlogById(UUID id, Blog blog) {
        try {
            Blog checkBlog = selectBlogById(id);
            if(checkBlog != null) {

                updateBlog.setString(1, blog.getTitle());
                updateBlog.setString(2, blog.getDescription());
                updateBlog.setString(3, blog.getDate().toString());
                updateBlog.setString(4, id.toString());

                int affectedRecords = updateBlog.executeUpdate();

                if(affectedRecords != 1) {
                    System.out.println("Something went wrong, user not updated...");
                    return 0;
                }

                return 1;
            }
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong, user not updated...");
        }

        return 0;
    }
}
