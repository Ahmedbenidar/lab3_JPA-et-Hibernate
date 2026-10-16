# TP3 - JPA / Hibernate : Gestion des réservations de salles

## Objectif

Mettre en place une application Java qui associe des entités JPA avec Hibernate, en
utilisant une base H2 en mémoire. Le but du TP est de manipuler les cardinalités
`@OneToMany` / `@ManyToOne` / `@ManyToMany` et les notions de cascade et d'orphanRemoval.

## Technologies

| Élément | Version |
|---|---|
| JPA (javax.persistence-api) | 2.2 |
| Hibernate ORM | 5.4.30.Final |
| H2 (base en mémoire) | 1.4.200 |
| JUnit | 4.13.2 |
| Java | 1.8 |
| Maven | 3.8.5 |

## Structure du projet

```
gestion-reservations
├── pom.xml
├── README.md
└── src
    └── main
        ├── java/com/example
        │   ├── App.java
        │   └── model
        │       ├── Equipement.java
        │       ├── Reservation.java
        │       ├── Salle.java
        │       └── Utilisateur.java
        └── resources/META-INF/persistence.xml
```

## Modèle de données

- **Utilisateur** : `id`, `email` (unique), `nom`
- **Salle** : `id`, `nom` (unique), `capacite`
- **Reservation** : `id`, `dateDebut`, `dateFin`
- **Equipement** : `id`, `nom` (unique)

## Relations

- `Utilisateur` **1 — N** `Reservation` : `@OneToMany(mappedBy = "utilisateur", cascade = ALL, orphanRemoval = true)`
  → supprimer une réservation de la collection la supprime aussi en base.
- `Reservation` **N — 1** `Utilisateur` : `@ManyToOne`, colonne `utilisateur_id` (non nulle)
- `Reservation` **N — 1** `Salle` : `@ManyToOne`, colonne `salle_id` (non nulle)
- `Salle` **N — N** `Equipement` : `@ManyToMany(cascade = {PERSIST, MERGE})` avec la table de jointure
  `salle_equipement (salle_id, equipement_id)`. Le côté inverse est `Equipement.salles` (`mappedBy = "equipements"`).
  Pas de `REMOVE` dans la cascade : un équipement peut être partagé par plusieurs salles.

## Configuration JPA

Le fichier `src/main/resources/META-INF/persistence.xml` déclare l'unité de persistence
`gestion-reservations-pu` en `RESOURCE_LOCAL` avec :

- URL : `jdbc:h2:mem:test;DB_CLOSE_DELAY=-1`
- utilisateur : `sa`, mot de passe vide
- dialecte : `org.hibernate.dialect.H2Dialect`
- `hibernate.hbm2ddl.auto = update` (création automatique des tables)
- `hibernate.show_sql = true` et `format_sql = true` (SQL affiché dans la console)

## Exécution

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.example.App"
```

Ce que fait `App` :

1. crée un utilisateur, une salle, un équipement et une réservation ;
2. associe l'équipement à la salle (table de jointure) ;
3. persiste la salle puis l'utilisateur (la réservation est enregistrée par cascade) ;
4. recharge l'utilisateur et retire sa réservation de la collection, ce qui la supprime en base
   grâce à `orphanRemoval`.

La console affiche les ordres SQL `create table`, les `insert into` et le `delete` final.
