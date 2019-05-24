package by.tryputs.bookssharing.repository;

import by.tryputs.bookssharing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends AbstractRepository<User, Long> {

    User findByEmail(String email);
}