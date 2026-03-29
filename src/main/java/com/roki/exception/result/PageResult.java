package com.roki.exception.result;

import java.util.List;

/**
 * Paginated payload used together with {@link Result}.
 * 与 {@link Result} 配套使用的分页载荷对象。
 */
public class PageResult<T> {

    private Long pageNo;
    private Long pageSize;
    private Long total;
    private Long totalPages;
    private Boolean hasNext;
    private Boolean hasPrevious;
    private List<T> records;

    public static <T> PageResult<T> of(Long total, List<T> records) {
        PageResult<T> result = new PageResult<>();
        result.setPageNo(1L);
        result.setPageSize(records == null ? 0L : (long) records.size());
        result.setTotal(total);
        result.setTotalPages(calculateTotalPages(total, result.getPageSize()));
        result.setHasNext(false);
        result.setHasPrevious(false);
        result.setRecords(records);
        return result;
    }

    public static <T> PageResult<T> of(Long pageNo, Long pageSize, Long total, List<T> records) {
        PageResult<T> result = new PageResult<>();
        result.setPageNo(pageNo);
        result.setPageSize(pageSize);
        result.setTotal(total);
        result.setTotalPages(calculateTotalPages(total, pageSize));
        result.setHasNext(result.getTotalPages() != null && pageNo != null && pageNo < result.getTotalPages());
        result.setHasPrevious(pageNo != null && pageNo > 1);
        result.setRecords(records);
        return result;
    }

    private static Long calculateTotalPages(Long total, Long pageSize) {
        if (total == null || pageSize == null || pageSize <= 0) {
            return 0L;
        }
        return (total + pageSize - 1) / pageSize;
    }

    public Long getPageNo() {
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public Boolean getHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(Boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
