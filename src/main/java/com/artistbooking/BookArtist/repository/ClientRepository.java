package com.artistbooking.BookArtist.repository;

import com.artistbooking.BookArtist.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ClientRepository extends JpaRepository<Client,Integer> {

    Set<Client> findByEmail(String email);
}
