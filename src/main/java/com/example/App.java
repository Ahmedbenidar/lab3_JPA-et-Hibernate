package com.example;

import com.example.model.Equipement;
import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.model.Utilisateur;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;

public class App {
    public static void main(String[] args) {
        // ouverture de la persistence unit
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestion-reservations-pu");
        EntityManager em = emf.createEntityManager();

        // ===== Creation des entites =====
        em.getTransaction().begin();

        Utilisateur user = new Utilisateur();
        user.setEmail("alice@example.com");
        user.setNom("Alice Dupont");

        Salle salle = new Salle();
        salle.setNom("Salle A");
        salle.setCapacite(30);

        Equipement projecteur = new Equipement();
        projecteur.setNom("Projecteur");

        salle.addEquipement(projecteur);

        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2026, 1, 10));
        reservation.setDateFin(LocalDate.of(2026, 1, 11));
        reservation.setSalle(salle);
        reservation.setUtilisateur(user);

        user.addReservation(reservation);

        // la salle doit etre persistee avant car Reservation.salle est obligatoire
        em.persist(salle);
        em.persist(user); // le cascade persist enregistre aussi la reservation

        em.getTransaction().commit();

        // ===== Suppression d'une reservation =====
        // avec orphanRemoval, la retirer de la collection suffit a la supprimer en base
        em.getTransaction().begin();
        Utilisateur user1 = em.find(Utilisateur.class, 1L);
        Reservation res = user1.getReservations().iterator().next();
        user1.getReservations().remove(res);
        em.getTransaction().commit();

        // fermeture des ressources
        em.close();
        emf.close();
    }
}
