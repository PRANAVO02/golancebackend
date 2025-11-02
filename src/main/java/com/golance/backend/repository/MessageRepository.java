package com.golance.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.golance.backend.model.Message;
import com.golance.backend.model.User;

public interface MessageRepository extends JpaRepository<Message, Long> {
  /*  List<Message> findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampAsc(
        User sender, User receiver, User receiverAlt, User senderAlt
    );

    */
	//for more understandability, custom query is built using JPQL -> Java Persistence Query Language

	@Query("SELECT m FROM Message m WHERE " +
			"((m.sender.id = :u1Id AND m.receiver.id = :u2Id) OR " +
			" (m.sender.id = :u2Id AND m.receiver.id = :u1Id)) " +
			"ORDER BY m.timestamp ASC")
	List<Message> getConversation(@Param("u1Id") Long user1Id, @Param("u2Id") Long user2Id);



	@Query("""
			    SELECT DISTINCT m.receiver 
			    FROM Message m 
			    WHERE m.sender.id = :userId
			    UNION
			    SELECT DISTINCT m.sender 
			    FROM Message m 
			    WHERE m.receiver.id = :userId
			""")
	List<User> findChatContacts(@Param("userId") Long userId);

	boolean existsBySenderAndReceiverOrReceiverAndSender(User sender, User receiver, User receiver2, User sender2);



}