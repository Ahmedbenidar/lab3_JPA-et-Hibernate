package com.example.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity // table Utilisateur
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id auto
    private Long id;

    @Column(nullable = false, unique = true) // email obligatoire et unique
    private String email;

    @Column(nullable = false)
    private String nom;

    // un utilisateur peut avoir plusieurs reservations
    // cascade ALL + orphanRemoval : on persiste/supprime les reservations avec le user
    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reservation> reservations = new HashSet<>();

    public Utilisateur() { // constructeur vide obligatoire pour Hibernate
    }

    public Utilisateur(String email, String nom) {
        this.email = email;
        this.nom = nom;
    }

    // on met a jour les 2 cotes de la relation
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservation.setUtilisateur(this);
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
        reservation.setUtilisateur(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
}
