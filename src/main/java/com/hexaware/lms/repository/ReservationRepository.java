package com.hexaware.lms.repository;


import com.hexaware.lms.entity.Book;
import com.hexaware.lms.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {



    List<Reservation> findAllByBook(Book book);

    Reservation findByBook(Optional<Book> byId);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.book.id = :bookId")
    Integer getReservationCount(Long bookId);
}
