#setwd("/Users/vince-n/Documents/Nico/Ordi_Nico_NIH/Nico/Results/KIR_HLA/HLA\ expression/Summer_2015_66K_HLA_expression")
setwd("~/Projet _ferret/Données_HLA")
## Import HLA gene list, HLA alleles

HLA_genes <- read.table('HLA_genes.txt', stringsAsFactors = F) # List of HLA genes of interest

expr_data <- read.csv('HLA_955+Gender.csv', stringsAsFactors = F) # Import HLA expr data and HLA alleles in 2d
tab <- expr_data[,c(1,13)]
tab1 <- expr_data[,11:12]
#tab2 <- expr_data[,4]
## For loop to convert HLA alleles

for(gene in HLA_genes[,1]){ # Make a loop with all the HLA genes
  
  HLA <- na.omit(as.character(unique(c(unique(expr_data[,paste(gene, "_allele1", sep = "")]), unique(expr_data[,paste(gene, "_allele2", sep = "")]))))) # Make a list of all existing HLA alleles for each gene
  print(HLA)
  
  for(allele in HLA){ # Make a loop with all the HLA alleles for each gene, Convert the allele to 1,2 code (val1, val2), then to genotype (val3), and finally to recessive (val4) or dominant (val5) testable model
    for (i in 1: nrow(tab1)){
    val4 <- paste(gene, "_", allele, sep="")
    tab[i,val4] <- ifelse(tab1[i,1] == allele && tab1[i,2] == allele, "A/A",
                               ifelse(tab1[i,1] != allele && tab1[i,2] == allele  , "A/T",
                                      ifelse(tab1[i,1] == allele && tab1[i,2] != allele , "A/T",
                                             ifelse(tab1[i,1] != allele && tab1[i,2] != allele , "T/T", NA))))
    
  }		
}
}

#expr_data[,3:32] <- NULL # Remove unwanted columns

# Save results
write.table(tab, "HLA_DQB1_955", row.names=F, quote=F, col.names=T)
