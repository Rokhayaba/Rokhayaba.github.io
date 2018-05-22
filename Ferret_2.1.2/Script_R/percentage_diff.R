#setwd("/Users/vince-n/Documents/Nico/Ordi_Nico_NIH/Nico/Results/KIR_HLA/HLA\ expression/Summer_2015_66K_HLA_expression")
#setwd("~/Projet _ferret/Données_HLA")
setwd("~/Projet_ferret/Données_HLA/HLA_par_pop")
## Import HLA gene list, HLA alleles

#HLA_genes <- read.table('HLA_genes_DP_DQ_DR.txt', stringsAsFactors = F) # List of HLA genes of interest

tab1 <- read.csv('PUR.csv', stringsAsFactors = F) # Import HLA expr data and HLA alleles in 2d
tab2 <- read.csv('PUR_4dg.csv', stringsAsFactors = F)
#print(tab1)
#print(tab2)
mpheno <- matrix(c(0),ncol = ncol(tab1)-1, nrow=nrow(tab1))#initialisation de la matrice
sortie <- matrix(c(0),ncol = 2, nrow=nrow(tab1))#initialisation de la matrice
for(i in 1 : nrow(tab1)){#Recherche de l'idendit? ou non entre l'entr?e et notre pr?diction par Upgrade
  #mpheno <- matrix(c(0),ncol = ncol(tab1)-3, nrow=nrow(tab1))#initialisation de la matrice
  y=2 
  while (y < ncol(tab1)){  
    mpheno[i,y-1]<-sum(tab1[i,y]==tab2[i,y] || tab1[i,y]==tab2[i,y+1])
    mpheno[i,y]<-sum(tab1[i,y+1]==tab2[i,y] || tab1[i,y+1]==tab2[i,y+1])
    y=y+2
  } 
  sortie[i,1]=tab1[i,1]
  sortie[i,2]=sum(mpheno[i,])
}
sortie[sortie[,2]!=10,]