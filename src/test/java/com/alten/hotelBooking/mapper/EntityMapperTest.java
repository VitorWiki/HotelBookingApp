package com.alten.hotelBooking.mapper;

import com.alten.hotelBooking.controller.request.PatchBookingRequest;
import com.alten.hotelBooking.controller.request.PostBookingRequest;
import com.alten.hotelBooking.controller.response.GetBookingResponse;
import com.alten.hotelBooking.controller.response.PatchBookingResponse;
import com.alten.hotelBooking.repositories.entities.RoomEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

public class EntityMapperTest {

    EntityMapper mapper = Mappers.getMapper(EntityMapper.class);

    @Test
    public void must_map_values_from_postRequest_to_roomEntity() {
        PostBookingRequest postRequest = new PostBookingRequest("Vitor", LocalDate.now(), LocalDate.now().plusDays(2));

        RoomEntity room = mapper.mapPostFrom(postRequest, LocalDate.now().plusDays(1));

        Assertions.assertNotNull(room);
        Assertions.assertEquals(room.getReservationStartDate(), postRequest.getReservationStartDate());
        Assertions.assertEquals(room.getReservationEndDate(), postRequest.getReservationEndDate());
        Assertions.assertEquals(room.getClientName(), postRequest.getClientName());
        Assertions.assertEquals(room.getReservationMiddleDate(), LocalDate.now().plusDays(1));

    }

    @Test
    public void must_map_values_from_patchRequest_to_roomEntity() {
        PatchBookingRequest patchRequest = new PatchBookingRequest(1, LocalDate.now().plusDays(2), LocalDate.now().plusDays(4));
        RoomEntity previousRoom = new RoomEntity(1, "Vitor", LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2), LocalDate.now().plusDays(3));

        RoomEntity room = mapper.mapPatchFrom(patchRequest, previousRoom, LocalDate.now().plusDays(3));

        Assertions.assertNotNull(room);
        Assertions.assertEquals(room.getReservationStartDate(), patchRequest.getNewReservationStartDate());
        Assertions.assertEquals(room.getReservationEndDate(), patchRequest.getNewReservationEndDate());
        Assertions.assertEquals(room.getClientName(), previousRoom.getClientName());
        Assertions.assertEquals(room.getBookId(), previousRoom.getBookId());
        Assertions.assertEquals(room.getReservationMiddleDate(), LocalDate.now().plusDays(3));

    }

    @Test
    public void must_map_values_from_roomEntity_to_patchResponse() {
        RoomEntity room = new RoomEntity(1, "Vitor", LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2), LocalDate.now().plusDays(3));

        PatchBookingResponse patchResponse = mapper.mapPatchFromEntity(room);

        Assertions.assertNotNull(patchResponse);
        Assertions.assertEquals(patchResponse.getReservationStartDate(), room.getReservationStartDate());
        Assertions.assertEquals(patchResponse.getReservationEndDate(), room.getReservationEndDate());
        Assertions.assertEquals(patchResponse.getClientName(), room.getClientName());
        Assertions.assertEquals(patchResponse.getBookId(), room.getBookId());

    }

    @Test
    public void must_map_values_from_roomEntity_to_getResponse() {
        RoomEntity room = new RoomEntity(1, "Vitor", LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2), LocalDate.now().plusDays(3));

        GetBookingResponse getResponse = mapper.mapGetFromExistingEntity(room);

        Assertions.assertNotNull(getResponse);
        Assertions.assertEquals(getResponse.getReservationStartDate(), room.getReservationStartDate());
        Assertions.assertEquals(getResponse.getReservationEndDate(), room.getReservationEndDate());
        Assertions.assertEquals(getResponse.getStatusMessage(), "Room not available for booking on this day");
    }
}
