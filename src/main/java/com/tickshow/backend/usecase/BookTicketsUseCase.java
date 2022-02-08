package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.coreEntity.CoreSeat;
import com.tickshow.backend.model.entity.*;
import com.tickshow.backend.repository.*;
import com.tickshow.backend.request.BookTicketsRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class BookTicketsUseCase {
    private static final Logger log = LoggerFactory.getLogger(BookTicketsUseCase.class);

    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;
    private final BookingRepository bookingRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final SeatRepository seatRepository;
    private final BookTicketsRequest request;

    public String execute() throws EntityNotFoundException {
        Movie movie = movieRepository.findByName(request.getMovieName());

        if (movie == null) {
            log.error("Movie with name {} not found", request.getMovieName());
            throw new EntityNotFoundException("Movie not found");
        }

        Theatre theatre = theatreRepository.findByName(request.getTheatre());

        if (theatre == null) {
            log.error("Theatre with name {} not found", request.getTheatre());
            throw new EntityNotFoundException("Theatre not found");
        }

        TimeSlot timeSlot = timeSlotRepository.findByTime(request.getTimeSlot());

        if (timeSlot == null) {
            log.error("TimeSlot not found");
            throw new EntityNotFoundException("TimeSlot not found");
        }

        List<CoreSeat> coreSeats = request.getSeats();
        List<Seat> seats = new ArrayList<>();

        for (CoreSeat coreSeat : coreSeats) {
            Seat seat = seatRepository.findByRowAndSeatNumber(coreSeat.getRow(), coreSeat.getSeatNumber());
            seats.add(seat);
        }

        Booking booking = Booking.builder()
                .email(request.getEmail())
                .noOfFullTickets(request.getNoOfFullTickets())
                .noOfHalfTickets(request.getNoOfHalfTickets())
                .movie(movie)
                .theatre(theatre)
                .timeSlot(timeSlot)
                .seats(seats)
                .build();

        bookingRepository.save(booking);

        return "Tickets successfully booked, and check your mail for confirmation";
    }
}