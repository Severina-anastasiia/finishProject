package com.alevel.web.data.response.personal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class PersonalDashboardChartData {

    private List<Date> labels;
    private List<Long> allBook;
    private List<Long> likeBook;
    private List<Long> dislikeBook;

    public PersonalDashboardChartData(){
        this.labels = Collections.emptyList();
        this.allBook = Collections.emptyList();
        this.likeBook = Collections.emptyList();
        this.dislikeBook = Collections.emptyList();
    }
}
