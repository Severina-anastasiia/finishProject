package com.alevel.web.data.request;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageAndSizeData {

    int page;
    int size;
}
