package com.alten.hotelBooking.repositories;

import com.alten.hotelBooking.repositories.entities.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {

    Optional<RoomEntity> findByRoomContaining(LocalDate date);
}
