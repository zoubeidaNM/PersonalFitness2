package com.example.personalfitness;

import org.springframework.data.repository.CrudRepository;

import java.util.LinkedHashSet;

public interface CommentRepository extends CrudRepository<Comment,Long> {

    public LinkedHashSet<Comment> findAllByReceiverNameOrderByPosteddateDesc(String userName);

}