#language: fr
@order-05
Fonctionnalité: Simulation De La Programmation

  Une fois la programmation définie et validée, vous pourrez démarrer une simulation.

  Scénario: Démarrer une simulation
    Etant donné qu'un utilisateur renseigne le champ de programmation avec le texte
    """text
    5 5
    1 2 N
    GAGAGAGAA
    3 3 E
    AADAADADDA
    """
    Lorsqu'un utilisateur sélectionne l'action Simulation|Démarrer
    Alors la pelouse doit apparaitre tondue aux coordonnées
      | Coordonnées |
      | 1, 2        |
      | 1, 3        |
      | 3, 3        |
      | 5, 1        |
    Et les tondeuses doivent finir aux positions
      | Position |
      | 1, 3, N  |
      | 5, 1, E  |

