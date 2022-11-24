package com.aliencat.springboot.component.base.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页数据结果集
 *
 * @param <T> 结果集项的类型
 * @author Looly
 */
@Data
public class PageResult<T> implements Serializable {
    public static final int DEFAULT_PAGE_SIZE = 20;
    private static final long serialVersionUID = 9056411043515781780L;
    /**
     * 数据列表
     */
	private List<T> rows;
    /**
     * 页码，0表示第一页
     */
    private int page;
    /**
     * 每页结果数
     */
    private int pageSize;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 总数
     */
    private int total;

    //---------------------------------------------------------- Constructor start

    /**
     * 构造
     */
    public PageResult() {
        this(0, DEFAULT_PAGE_SIZE);
    }

    /**
     * 构造
     *
     * @param page     页码，0表示第一页
     * @param pageSize 每页结果数
     */
    public PageResult(int page, int pageSize) {
        this.page = Math.max(page, 0);
        this.pageSize = pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
    }

    /**
     * 构造
     *
     * @param page     页码，0表示第一页
     * @param pageSize 每页结果数
     * @param total    结果总数
     */
    public PageResult(int page, int pageSize, int total) {
        this(page, pageSize);

        this.total = total;
        this.totalPage = totalPage(total, pageSize);
    }

	/**
	 * 构造
	 *
	 * @param rows     当前页数据
	 * @param total    结果总数
	 */
	public PageResult(int total,List<T> rows) {

		this.total = total;
		this.rows = rows;
		this.totalPage = totalPage(total, pageSize);
	}
    //---------------------------------------------------------- Constructor end

    //---------------------------------------------------------- Getters and Setters start

    /**
     * 根据总数计算总页数
     *
     * @param totalCount 总数
     * @param pageSize   每页数
     * @return 总页数
     * @since 5.8.5
     */
    public static int totalPage(long totalCount, int pageSize) {
        if (pageSize == 0) {
            return 0;
        }
        return Math.toIntExact(totalCount % pageSize == 0 ? (totalCount / pageSize) : (totalCount / pageSize + 1));
    }

    /**
     * 页码，0表示第一页
     *
     * @return 页码，0表示第一页
     */
    public int getPage() {
        return page;
    }

    /**
     * 设置页码，0表示第一页
     *
     * @param page 页码
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * @return 每页结果数
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页结果数
     *
     * @param pageSize 每页结果数
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return 总页数
     */
    public int getTotalPage() {
        return totalPage;
    }

    /**
     * 设置总页数
     *
     * @param totalPage 总页数
     */
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * @return 总数
     */
    public int getTotal() {
        return total;
    }
    //---------------------------------------------------------- Getters and Setters end

    /**
     * 设置总数
     *
     * @param total 总数
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * @return 是否最后一页
     */
    public boolean isLast() {
        return this.page >= (this.totalPage - 1);
    }
}
