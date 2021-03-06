package com.travelmaker.store.domain;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class StoreHotelPaging {
    private int currentPage;
    private int pageBlock;
    private int pageSize;
    private int totalA;
    private StringBuffer pagingHTML;

    public void makePagingHTML() {
        pagingHTML = new StringBuffer();

        int totalP = (totalA + pageSize - 1) / pageSize;

        int startPage = (currentPage - 1) / pageBlock * pageBlock + 1;
        int endPage = startPage + pageBlock - 1;

        if (endPage > totalP) {
            endPage = totalP;
        }
        if (startPage > pageBlock) {
            pagingHTML.append("<li><a class='direction' href='/store/list/" + (startPage - 1) + "'>이전</a></li>");
        }

        for (int i = startPage; i <= endPage; i++) {
            if (i == currentPage) {
                pagingHTML.append("<li><a class='on' href='/store/list/" + i + "'>" + i + "</a></li>");
            } else {
                pagingHTML.append("<li><a href='/store/list/" + i + "'>" + i + "</a></li>");
            }
        }
        if (endPage < totalP) {
            pagingHTML.append("<li><a class='direction' href='/store/list/" + (endPage + 1) + "'>다음</a></li>");
        }
    }

    public void makeSearchPagingHTML() {
        pagingHTML = new StringBuffer();

        int totalP = (totalA + pageSize - 1) / pageSize;

        int startPage = (currentPage - 1) / pageBlock * pageBlock + 1;
        int endPage = startPage + pageBlock - 1;

        if (endPage > totalP) {
            endPage = totalP;
        }
        if (startPage > pageBlock) {
            pagingHTML.append("[<span id='paging' onclick='boardSearch(" + (startPage - 1) + ")'>이전</span>]");
        }

        for (int i = startPage; i <= endPage; i++) {
            if (i == currentPage) {
                pagingHTML.append("[<span id='currentPaging' onclick='boardSearch(" + i + ")'>" + i + "</span>]");
            } else {
                pagingHTML.append("[<span id='paging' onclick='boardSearch(" + i + ")'>" + i + "</span>]");
            }
        }
        if (endPage < totalP) {
            pagingHTML.append("[<span id='paging' onclick='boardSearch(" + (endPage + 1) + ")'>다음</span>]");
        }
    }
}
