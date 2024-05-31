#language: fr
@order-01
Fonctionnalité: Programmation

  Pour programmer la tondeuse, on lui fournit un fichier d'entrée construit comme suit :

  - La premiére ligne correspond aux coordonnées du coin supérieur droit de la pelouse, celles du coin inférieur gauche sont supposées être (0, 0).
  - La suite du fichier permet de piloter toutes les tondeuses qui ont été déployées.

  Chaque tondeuse a deux lignes la concernant :

  - La premiére ligne donne la position initiale de la tondeuse, ainsi que son orientation.
  - La seconde ligne est une série d'instructions ordonnant à la tondeuse d'explorer la pelouse.

  Scénario: Une configuration renseignée décrit la pelouse et les tondeuses déployées
    Etant donné le contenu de programmation
    """text
    5 5
    1 2 N
    GAGAGAGAA
    3 3 E
    AADAADADDA
    """
    Lorsque l'application crée la programmation
    Alors la pelouse doit être de taille 6x6
    Et les tondeuses doivent être
      | Position | Instructions |
      | 1, 2, N  | GAGAGAGAA    |
      | 3, 3, E  | AADAADADDA   |
