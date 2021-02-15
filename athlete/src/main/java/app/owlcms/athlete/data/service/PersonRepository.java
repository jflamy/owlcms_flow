package app.owlcms.athlete.data.service;

import org.springframework.data.jpa.repository.JpaRepository;

import app.owlcms.athlete.data.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Integer> {

}