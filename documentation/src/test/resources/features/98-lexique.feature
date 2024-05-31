#language: fr
@asciidoc
@order-98
@glossary
Fonctionnalité: Lexique

  Vous retrouverez ici les concepts utilisés dans cette documentation, ainsi que leur équivalent en anglais.

  [glossary]

  Coordonnées (anglais: _Coordinates_):: Système de localisation utilisé par l'application pour une pelouse.
  Par exemple, la position de la tondeuse pourrait être « 0, 0, N », ce qui signifie qu'elle se situe dans le coin
  inférieur gauche de la pelouse, et orientée vers le nord.

  Instructions (anglais: _Instructions_):: Séquence de lettres simples qui décrit les commandes à envoyer à une tondeuse.
  Les commandes possibles sont au nombre de trois :

  |===
  |Commande|Signal|Action

  |Droite|D|Pivote la tondeuse de 90° à droite, sans la déplacer.
  |Gauche|G|Pivote la tondeuse de 90° à gauche, sans la déplacer.
  |Avance|A|Avance la tondeuse d'une case dans la direction à laquelle elle fait face, et sans modifier son orientation.
  |===

  Orientation (anglais: _Direction_):: L'orientation cardinale des tondeuses.
  Elle s'exprime d'une lettre indiquant l'orientation selon la notation cardinale anglaise (N, E, W, S).

  |===
  |Orientation|Sens|Notation

  |Au nord|Haut|N
  |À l'est|Droite|E
  |À l'ouest|Gauche|W
  |Au sud|Bas|S
  |===

  Pelouse (anglais: _Lawn_):: L'espace vert sur lequel opère les tondeuses.
  Les pelouses sont considérées comme des surfaces rectangulaires planes.
  Au besoin d'une rotation, on considèrera la pelouse comme alignée aux axes.
  La pelouse est divisée en grille pour simplifier la navigation.

  Position (anglais: _Position_):: Décrit l'état d'une tondeuse sur la pelouse.
  Cette position se compose de deux données :

  - Les coordonnées de la tondeuse
  - Son orientation

  Tondeuse (anglais: _Mower_):: Tondeuse à gazon, automatisée, proposée par la société MowItNow.
  Elle est capable de tondre une pelouse de façon autonome en suivant un jeu d'instructions.
  L'application peut piloter une ou plusieurs tondeuses déployées sur une pelouse selon la configuration.

  Scénario: Root
