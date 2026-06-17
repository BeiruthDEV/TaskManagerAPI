package com.taskmanager.api.application.pagination;

public record PageQuery(int page, int size) {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 200;

    public PageQuery {
        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size must be > 0");
        }
        if (size > MAX_SIZE) {
            throw new IllegalArgumentException("size must be <= " + MAX_SIZE);
        }
    }

    public static PageQuery of(int page, int size) {
        return new PageQuery(page, size);
    }

    public static PageQuery defaults() {
        return new PageQuery(DEFAULT_PAGE, DEFAULT_SIZE);
    }

    public long offset() {
        return (long) page * (long) size;
    }
}
