package demo.services;

import java.util.List;

import demo.entity.User;

public class PagedUsers {
    public long total;
    public int pageNumber;
    public int pageSize;
    public long totalPages;
    
    public List<User> items;

    public PagedUsers(long total, List<User> items) {
        this.total = total;
        this.items = items;
    }
    
    public PagedUsers(long total, int pageNumber, int pageSize, long totalPages, List<User> items) {
        this.total = total;
        this.items = items;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
    }
}