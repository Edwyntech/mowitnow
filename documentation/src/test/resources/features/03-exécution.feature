#language: fr
@order-03
Fonctionnalité: Exécution

  Chaque tondeuse se déplace de façon séquentielle, ce qui signifie que la seconde tondeuse ne bouge que lorsque la première a exécuté intégralement sa série d'instructions.

  Scénario: A l'issue de l'exécution d'une série d'instructions par la tondeuse, chaque tondeuse communique sa position et son orientation.
    Etant donné le contenu de programmation
    """text
    5 5
    1 2 N
    GAGAGAGAA
    3 3 E
    AADAADADDA
    """
    Lorsque l'application crée la programmation
    Et que l'application exécute la programmation
    Alors les positions des tondeuses doivent être
      | Position |
      | 1, 3, N  |
      | 5, 1, E  |

  Scénario: Si la position aprés mouvement est en dehors de la pelouse, la tondeuse ne bouge pas, conserve son orientation et traite la commande suivante.
    Etant donné le contenu de programmation
    """text
    5 5
    0 3 N
    AAA
    """
    Lorsque l'application crée la programmation
    Et que l'application exécute la programmation
    Alors les positions des tondeuses doivent être
      | Position |
      | 0, 5, N  |
