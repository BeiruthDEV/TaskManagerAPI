package com.taskmanager.api.application.pagination;

import java.util.List;
import java.util.function.Function;

public record PageResult<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {

    public static <T> PageResult<T> of(List<T> content, PageQuery query, long totalElements) {
        int size = query.size();
        int page = query.page();
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) totalElements / (double) size);
        boolean first = page == 0;
        boolean last = totalPages == 0 || page >= totalPages - 1;
        return new PageResult<>(List.copyOf(content), page, size, totalElements, totalPages, first, last);
    }

    public <R> PageResult<R> map(Function<? super T, ? extends R> mapper) {
        List<R> mapped = content.stream().<R>map(mapper).toList();
        return new PageResult<>(mapped, page, size, totalElements, totalPages, first, last);
    }
}
