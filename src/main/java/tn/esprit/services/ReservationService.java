package tn.esprit.services;

import tn.esprit.main.ReservationDAO;
import tn.esprit.entites.Reservation;

import java.sql.SQLException;
import java.util.List;

public class ReservationService {
    private ReservationDAO reservationDAO = new ReservationDAO();

    public void insert(Reservation reservation) {
        try {
            reservationDAO.createReservation(reservation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Reservation> getAll() {
        try {
            return reservationDAO.getAllReservations();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delete(int id) {
        try {
            reservationDAO.deleteReservation(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Reservation reservation) {
        try {
            reservationDAO.updateReservation(reservation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Reservation getSelectedReservation() {
        return null;
    }
}

