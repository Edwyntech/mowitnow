#language: fr
@order-02
Fonctionnalité: Validation

  Un ensemble de règles relatives au format et à la consistance de la programmation sont mises en place.

  Dans le cas où une programmation serait mal définie, un message d'erreur doit être émis à l'utilisateur.

  Scénario: Une programmation doit au minimum définir une taille de pelouse
    Etant donné le contenu de programmation
    """text
    """
    Lorsque l'application crée la programmation
    Alors l'application doit émettre le message d'erreur
    """text
    Ligne 0 - Pas de contenu.
    """

  Scénario: Une tondeuse doit être décrite par deux lignes, sa position et ses instructions.
    Etant donné le contenu de programmation
    """text
    5 5
    0 0 N
    """
    Lorsque l'application crée la programmation
    Alors l'application doit émettre le message d'erreur
    """text
    Ligne 2 - Tondeuse incomplète.
    """

  Scénario: Les coordonnées se composent de deux nombres positifs séparés par un espace.

  L'expression régulière valide est `\\d+ \\d+`.

    Etant donné le contenu de programmation
    """text
    (2, 3)
    """
    Lorsque l'application crée la programmation
    Alors l'application doit émettre le message d'erreur
    """text
    Ligne 1 - Coordonnées invalides: '(2, 3)'.
    """

  Scénario: La position de la tondeuse est représentée par une combinaison de coordonnées (x, y) et d'une lettre indiquant l'orientation selon la notation cardinale anglaise (N, E, W, S).

  L'écriture des valeurs de la position se fait sans ponctuation, séparées par un espace.
  L'expression régulière valide est `\\d+ \\d+ [NEWS]`.

    Etant donné le contenu de programmation
    """text
    5 5
    0, 0, N
    GAGAGAGAA
    """
    Lorsque l'application crée la programmation
    Alors l'application doit émettre le message d'erreur
    """text
    Ligne 2 - Position invalide: '0, 0, N'.
    """

  Scénario: Les instructions sont une séquence simple de lettres.

  Les lettres possibles sont « D », « G » et « A ».
  L'expression régulière valide est `[DGA]+`.

    Etant donné le contenu de programmation
    """text
    5 5
    1 2 N
    POKER
    """
    Lorsque l'application crée la programmation
    Alors l'application doit émettre le message d'erreur
    """text
    Ligne 3 - Instructions invalides: 'POKER'.
    """

  Scénario: Une tondeuse déployée au-delà de la pelouse déclenche un message d'erreur
    Etant donné le contenu de programmation
    """text
    5 5
    6 6 E
    AADAADADDA
    """
    Lorsque l'application crée la programmation
    Alors l'application doit émettre le message d'erreur
    """text
    Ligne 2 - Tondeuse en dehors de la pelouse.
    """

