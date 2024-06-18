#language: fr
@order-03
Fonctionnalité: Édition De La Programmation

  Scénario: La programmation peut être éditée directement depuis le champ d'édition
    Lorsqu'un utilisateur renseigne le champ de programmation avec le texte
    """text
    5 5
    1 2 N
    GAGAGAGAA
    3 3 E
    AADAADADDA
    """
    Alors aucun message d'erreur ne doit s'afficher
    Et l'action Simulation|Démarrer doit être disponible

  Scénario: La programmation peut être importée depuis un fichier
    Lorsqu'un utilisateur ouvre le fichier programs/lawn6x6-2mowers.txt
    Alors le champ de programmation doit contenir
    """text
    5 5
    1 2 N
    GAGAGAGAA
    3 3 E
    AADAADADDA
    """
    Alors aucun message d'erreur ne doit s'afficher
    Et l'action Simulation|Démarrer doit être disponible

  Scénario: La programmation peut être sauvegardée dans un fichier si elle est valide
    Lorsqu'un utilisateur renseigne le champ de programmation avec le texte
    """text
    5 5
    1 2 N
    GAGAGAGAA
    3 3 E
    AADAADADDA
    """
    Et qu'un utilisateur enregistre la programmation dans un fichier de sauvegarde
    Alors le fichier de sauvegarde doit contenir
    """text
    5 5
    1 2 N
    GAGAGAGAA
    3 3 E
    AADAADADDA
    """

