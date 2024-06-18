#language: fr
@order-03
Fonctionnalité: Accès à La Documentation

  En plus de l'écran principal, l'utilisateur aura accès

  - À un écran d'information sur l'application
  - À la documentation PDF

  Scénario: L'écran d'information s'affiche via le menu Aide->À Propos
    Lorsqu'un utilisateur sélectionne l'action Aide|À Propos
    Alors un dialogue d'information doit s'afficher

  Scénario: La documentation PDF s'affiche via le menu Aide->Documentation
    Lorsqu'un utilisateur sélectionne l'action Aide|Documentation
    Alors la documentation PDF doit s'afficher

