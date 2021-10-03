package com.presto.auth.domain.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PagedContent<T> {

    private long totalElements;
    private int totalPages;
    private int page;
    private int size;
    private boolean hasNextPage;
    private boolean hasPreviousPage;
    private boolean isFirst;
    private boolean isLast;
    private List<T> data;

    public PagedContent(Page pagedData, List<T> data) {
        this.setData(data);
        this.setTotalElements(pagedData.getTotalElements());
        this.setTotalPages(pagedData.getTotalPages());
        this.setPage(pagedData.getPageable().getPageNumber());
        this.setSize(pagedData.getPageable().getPageSize());
        this.setHasNextPage(pagedData.hasNext());
        this.setHasPreviousPage(pagedData.hasPrevious());
        this.setFirst(pagedData.isFirst());
        this.setLast(pagedData.isLast());
    }
}