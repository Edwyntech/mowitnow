#language: fr
@order-01
Fonctionnalité: Démarrage

  L'application de bureau se lance à l'aide de l'exécutable fournit par l'installateur.
  Elle affiche un écran unique pour piloter toutes les fonctionnalités.

  Scénario: L'écran principal se décompose en trois parties
    Lorsque la fenêtre principale s'affiche
    Alors la barre de menu doit s'afficher
    Et la zone d'édition de la programmation doit s'afficher
    Et la zone de simulation doit s'afficher

  Scénario: L'arrêt de l'application se fait via le menu Fichier->Quitter
    Lorsqu'un utilisateur quitte l'application
    Alors l'application doit s'être arrêtée
