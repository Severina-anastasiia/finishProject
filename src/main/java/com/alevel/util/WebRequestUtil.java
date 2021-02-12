package com.alevel.util;

import com.alevel.web.data.request.PageAndSizeData;
import com.alevel.web.data.request.SortData;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.WebRequest;

import java.util.Objects;

public class WebRequestUtil {

    private static final String OWNER_PARAM = "owner";
    private static final String PAGE_PARAM = "page";
    private static final String SIZE_PARAM = "size";
    private static final String SORT_PARAM = "sort";
    private static final String ORDER_PARAM = "order";
    public static final String DEFAULT_SORT_PARAM_VALUE = "created";
    public static final String DEFAULT_ORDER_PARAM_VALUE = "desc";
    public static final int DEFAULT_PAGE_PARAM_VALUE = 1;
    public static final int DEFAULT_SIZE_PARAM_VALUE = 10;

    public static PageAndSizeData generatePageAndSizeData(WebRequest webRequest){
        int page = webRequest.getParameter(PAGE_PARAM) != null ? Integer.parseInt(Objects
                .requireNonNull(webRequest.getParameter(PAGE_PARAM))) : DEFAULT_PAGE_PARAM_VALUE;
        int size = webRequest.getParameter(SIZE_PARAM) != null ? Integer.parseInt(Objects
                .requireNonNull(webRequest.getParameter(SIZE_PARAM))) : DEFAULT_SIZE_PARAM_VALUE;
        return new PageAndSizeData(page, size);
    }

    public static SortData generateSortData(WebRequest webRequest){
        String sort = StringUtils.isNotBlank(webRequest.getParameter(SORT_PARAM))
                ? Objects.requireNonNull(webRequest.getParameter(SORT_PARAM)) : DEFAULT_SORT_PARAM_VALUE;
        String order = StringUtils.isNotBlank(webRequest.getParameter(ORDER_PARAM))
                ? Objects.requireNonNull(webRequest.getParameter(ORDER_PARAM)) : DEFAULT_ORDER_PARAM_VALUE;
        return new SortData(sort, order);
    }

    public static boolean getOwner(WebRequest webRequest){
        return webRequest.getParameter(OWNER_PARAM) == null ||Boolean
                .parseBoolean(Objects.requireNonNull(webRequest.getParameter(OWNER_PARAM)));
    }
}
