#language: fr
@order-04
Fonctionnalité: Validation De La Programmation

  L'édition de la programmation peut entrainer des erreurs ou des incohérences de configuration.
  Si tel est le cas, un message d'erreur s'affiche pour vous guider dans l'édition d'une programmation.

  CAUTION: Une programmation invalide rendra indisponibles le démarrage de la simulation et l'enregistrement d'un fichier.

  Scénario: La simulation s'active sitôt la configuration validée
    Lorsqu'un utilisateur renseigne le champ de programmation avec le texte
    """text
    5 5
    1 2 N
    GAGAGAGAA
    3 3 E
    AADAADADDA
    """
    Alors l'action Simulation|Démarrer doit être disponible
    Et la simulation doit afficher une pelouse de taille 6x6

  Scénario: Une erreur de format affichera un message d'erreur à l'écran
    Lorsqu'un utilisateur renseigne le champ de programmation avec le texte
    """text
    5x5
    """
    Alors le message d'erreur doit afficher
    """text
    Ligne 1 - Coordonnées invalides: '5x5'.
    """
    Et la simulation ne doit pas afficher la pelouse
    Et l'action Simulation|Démarrer doit être indisponible
    Et l'action Fichier|Enregistrer Sous... doit être indisponible

