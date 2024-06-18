#language: fr
@order-02
Fonctionnalité: Menus Disponibles

  Les actions possibles sont accessibles depuis les menu de l'application.

  Scénario: Menu Fichier
    Lorsque le menu Fichier est capturé
    Alors le menu Fichier affiche les actions
      | Action              | Raccourci | Description                                   |
      | Ouvrir...           | Ctrl+O    | Récupère une programmation depuis un fichier. |
      | Enregistrer Sous... | Ctrl+S    | Sauvegarde une programmation dans un fichier. |
      | Quitter             | Ctrl+Q    | Arrête l'application.                         |

  Scénario: Menu Simulation
    Lorsque le menu Simulation est capturé
    Alors le menu Simulation affiche les actions
      | Action   | Raccourci | Description             |
      | Démarrer | F5        | Démarre une simulation. |
      | Arrêter  | F6        | Arrête une simulation.  |

  Scénario: Menu Aide
    Lorsque le menu Aide est capturé
    Alors le menu Aide affiche les actions
      | Action        | Raccourci | Description                                |
      | Documentation | F1        | Affiche la documentation de l'application. |
      | À Propos      | F2        | Affiche une fenêtre d'information.         |

