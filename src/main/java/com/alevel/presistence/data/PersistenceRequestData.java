package com.alevel.presistence.data;

import com.alevel.util.WebRequestUtil;
import com.alevel.web.data.request.PageAndSizeData;
import com.alevel.web.data.request.SortData;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class PersistenceRequestData {

    private int page;
    private int size;
    private String sort;
    private String order;
    private boolean owner;
    private Map<String, Object> parameters;

    public PersistenceRequestData(WebRequest request){
        PageAndSizeData pageAndSizeData = WebRequestUtil.generatePageAndSizeData(request);
        SortData sortData = WebRequestUtil.generateSortData(request);
        this.page = pageAndSizeData.getPage();
        this.size = pageAndSizeData.getSize();
        this.sort = sortData.getSort();
        this.order = sortData.getOrder();
        this.owner = WebRequestUtil.getOwner(request);
    }
}
