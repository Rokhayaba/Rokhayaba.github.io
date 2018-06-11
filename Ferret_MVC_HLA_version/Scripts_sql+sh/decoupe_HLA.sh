#!/bin/bash
cut -d , -f 12 HLA.csv >> HLA_E.csv  # Récupère les colonnes contenant les données HLA spécifique au gène dont on a besoin
sort -n HLA_E.csv > HLA_E1.csv
uniq HLA_E1.csv >> HLA_E2.csv  # sort et uniq respectivement trient et enlevent les doublons des numéros d'allèles
sed -e "s/^/,/g" HLA_E2.csv >> HLA_E3.csv
cat HLA_E3.csv | tr -d "\n" >> HLA_E4.csv #  Pour transformer les lignes en colonnes , on mets d'abord des "," à toutes les fins de lignes puis ensuite on supprime les retours chariots
cut -d , -f 1,2 HLA.csv >> HLA_E4.csv # En fin on rajoute deux colonnes supp : id et subgroup à nos colonnes de numéros d'allèles. On aura donc les individus en fonction des numéros d'allèles.

paste HLA_955.csv Gender_955.csv >> HLA_955+Gender # colle le contenu de gender à la fin du fichier en premier
paste -d, separated_CHB+JPT_955.csv <(cut -d, -f3- sbgroup_955NV) >> sbgroup_955NV.csv #un meilleur paste on a plus besoin de separer la colonne voulu dans un autre fichier

