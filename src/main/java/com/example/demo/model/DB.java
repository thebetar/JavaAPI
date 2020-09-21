package com.example.demo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DB {

    public static String DB_NAME = "blogs.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:/Users/Lars Vonk/Desktop/Courses/Java/demo/" + DB_NAME;

    public static final String TABLE_BLOGS = "blogs";
    public static final String COLUMN_BLOG_ID = "_id";
    public static final String COLUMN_BLOG_TITLE = "title";
    public static final String COLUMN_BLOG_DESC = "description";

    public static final String QUERY_BLOGS = "SELECT * FROM " + TABLE_BLOGS;
    public static final String QUERY_BLOG_BY_ID = "SELECT * FROM " + TABLE_BLOGS +
            " WHERE " + COLUMN_BLOG_ID + " = ?";
    public static final String QUERY_BLOG_BY_TITLE = "SELECT * FROM " + TABLE_BLOGS +
            " WHERE " + COLUMN_BLOG_TITLE + " = ?";

    public static final String CREATE_BLOG = "INSERT INTO " + TABLE_BLOGS + " (" +
            COLUMN_BLOG_ID + ", " +
            COLUMN_BLOG_TITLE + ", " +
            COLUMN_BLOG_DESC + ") VALUES (?, ?, ?)";

    public static final String DELETE_BLOG = "DELETE FROM " + TABLE_BLOGS +
            " WHERE " + COLUMN_BLOG_ID + " = ?";

    public static final String UPDATE_BLOG = "UPDATE " + TABLE_BLOGS +
            " SET " + COLUMN_BLOG_TITLE + " = ?, " + COLUMN_BLOG_DESC + " = ?" +
            " WHERE " + COLUMN_BLOG_ID + " = ?";

    private Connection conn;

    private PreparedStatement queryBlogs;
    private PreparedStatement queryBlogById;
    private PreparedStatement queryBlogByTitle;
    private PreparedStatement insertBlog;
    private PreparedStatement deleteBlog;
    private PreparedStatement updateBlog;

    public PreparedStatement getQueryBlogs() {
        return queryBlogs;
    }

    public PreparedStatement getQueryBlogById() {
        return queryBlogById;
    }

    public PreparedStatement getQueryBlogByTitle() {
        return queryBlogByTitle;
    }

    public PreparedStatement getInsertBlog() {
        return insertBlog;
    }

    public PreparedStatement getDeleteBlog() {
        return deleteBlog;
    }

    public PreparedStatement getUpdateBlog() {
        return updateBlog;
    }

    public DB() {
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

}
