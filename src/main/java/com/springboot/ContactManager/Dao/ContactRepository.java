package com.springboot.ContactManager.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.ContactManager.entities.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {


}
