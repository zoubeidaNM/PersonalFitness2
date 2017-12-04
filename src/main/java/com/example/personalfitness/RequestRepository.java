package com.example.personalfitness;

import org.springframework.data.repository.CrudRepository;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public interface RequestRepository extends CrudRepository<Request, Long> {
   public LinkedHashSet<Request> findAllBySenderNameOrderByPosteddateDesc(String senderName);
   public LinkedHashSet<Request> findAllByReceiverNameOrderByPosteddateDesc(String receiverName);

}
