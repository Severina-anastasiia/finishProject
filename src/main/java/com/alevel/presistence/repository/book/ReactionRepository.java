package com.alevel.presistence.repository.book;

import com.alevel.presistence.entity.book.Reaction;
import com.alevel.presistence.repository.AbstractRepository;
import com.alevel.web.data.KeyValueData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReactionRepository extends AbstractRepository<Reaction> {

    List<Reaction> findAllByBookId(Integer id);
    List<Reaction> findAllByBookIdAndLikeTrue(Integer id);
    List<Reaction> findAllByBookIdAndLikeFalse(Integer id);
    Reaction findByBookIdAndPersonalId(Integer bookId, Integer personalId);

    @Query("select new com.alevel.web.data.KeyValueData(reaction.created, count(reaction.like)) " +
            "from Reaction as reaction where reaction.bookId in :bookIds " +
            "group by reaction.created order by reaction.created asc ")
    List<KeyValueData<Date, Long>> generateAllBookWithoutReaction(@Param("bookIds") List<Integer> postIds);

    @Query("select new com.alevel.web.data.KeyValueData(reaction.created, count(reaction.like)) " +
            "from Reaction as reaction where reaction.bookId in :bookIds and reaction.like = :react " +
            "group by reaction.created order by reaction.created asc ")
    List<KeyValueData<Date, Long>> generateAllBookByReaction(@Param("bookIds") List<Integer> bookIds, @Param("react") Boolean react);
}
