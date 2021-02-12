package com.alevel.service.book;

import com.alevel.presistence.entity.book.Reaction;
import com.alevel.web.data.KeyValueData;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReactionService {

    void deleteByBookId(Integer bookId);
    void like(Integer bookId, Integer personalId);
    void dislike(Integer bookId, Integer personalId);
    List<Reaction> findAllByBookIdAndLikeTrue(Integer bookId);
    List<Reaction> findAllByBookIdAndLikeFalse(Integer bookId);
    Map<String, List<KeyValueData<Date, Long>>> generatedChartByBookReaction();
}
