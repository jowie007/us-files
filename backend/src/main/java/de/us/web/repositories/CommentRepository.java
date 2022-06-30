package de.us.web.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.us.web.domain.Comment;

// https://stackoverflow.com/questions/25486583/how-to-use-orderby-with-findall-in-spring-data
// https://stackoverflow.com/questions/19733464/order-by-date-asc-with-spring-data
@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
	List<Comment> findAllBySongIdOrderByDateDesc(Long songId, Pageable pageable);
	List<Comment> findAllBySongIdOrderByDateDesc(Long songId);
}

