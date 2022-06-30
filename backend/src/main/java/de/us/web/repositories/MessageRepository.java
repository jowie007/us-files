package de.us.web.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.us.web.domain.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
	List<Message> findAllByUserNameAndDeletedOrderByDateDesc(String userName, Boolean deleted);
	List<Message> findAllByUserNameAndDeletedAndRead(String userName, Boolean deleted, Boolean read);
}
