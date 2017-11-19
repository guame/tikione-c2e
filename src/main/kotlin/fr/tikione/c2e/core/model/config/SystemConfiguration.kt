package fr.tikione.c2e.core.model.config

/**
 *  * `username` et `password` sont votre identifiant et mot de passe à l'abonnement CanardPC numérique, ces paramètres sont obligatoires.
 * `-cpcXXX` télécharger le numéro XXX, par exemple `-cpc348`.  *(depuis la version 1.1.0)* Télécharger plusieurs numéros, par exemple `-cpc348 -cpc349 -cpc350 -cpc351`. Vous pouvez aussi utiliser `-cpcall` pour télécharger l'intégralité des numéros à votre disposition.
 * `-nopic` ne pas téléchanger les images (un numéro contient 60~200Mo d'images, et ~500Ko de texte).
 * `-list` savoir quels numéros sont accessibles au téléchargement.
 * `-debug` affiche le détail du téléchargement dans un format proche de JSON.
 * `-resizeXX` redimensionne les images selon le ratio `XX` (ex: `-resize50` pour un ratio de 50%). Basé sur [ImageMagick](http://www.imagemagick.org), lequel doit être disponible dans le PATH ou packagé avec l'appli. Testé sous Windows uniquement, mais doit fonctionner partout où ImageMagick est disponible.
 * `-index` génère un sommaire CSV (`CPC-index.csv`) de tous les numéros disponibles au téléchargement, avec en détails la note, présence de DRM, poids au téléchargement, plateformes, etc. Attention, prévoir plusieurs dizaines de minutes pour ce traitement. Si le fichier `CPC-index.csv` existe déjà, il sera complété avec les numéros manquants.
 * `-proxy:address:port` utilise le proxy HTTP(S) définit par l'adresse `address` (nom de domaine ou adresse IP) et le port `port`. Cette option est généralement utile si vous vous connectez depuis le réseau d'un entreprise qui impose un proxy pour accéder au web.
 * `-sysproxy` utilise le proxy système.

 * @author Guillaume
 * 16/11/2017 - 21:12
 */

data class SystemConfiguration (
        
        var proxyUser : String,
        var proxyPort : Int,
        var proxyPassword: String

) 
