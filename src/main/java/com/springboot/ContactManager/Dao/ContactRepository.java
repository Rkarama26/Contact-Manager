package com.springboot.ContactManager.Dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springboot.ContactManager.entities.Contact;
import com.springboot.ContactManager.entities.User;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

	//pagination.... done page and pageable interface
	
	@Query("from Contact as d where d.user.id = :userId")
	public Page<Contact> findContactsByUser(@Param("userId")int userId, Pageable pageable);

	//for search 
	public List<Contact> findByNameContainingAndUser(String name , User user);
}