#setwd("/Users/vince-n/Documents/Nico/Ordi_Nico_NIH/Nico/Results/KIR_HLA/HLA\ expression/Summer_2015_66K_HLA_expression")
setwd("~/Projet _ferret/Données_HLA")
## Filling a tab with the 5 main subgroup

tabb <- read.csv('sbgroup_955.csv', stringsAsFactors = F)
y <- 1
    for (i in tab[,2]){
      val4 <- paste("group")
      print(i)
      
      print(y)
      #print(tab[,2])
      tabb[y,val4] <- ifelse(i == "CHB+JPT" || i == "CHD" || i == "CHS", "EAS",
                            ifelse(i == "CEU" || i == "FIN" || i == "GBR" || i== "TSI", "EUR",
                                   ifelse(i == "CLM" || i == "PUR" || i == "MXL", "AMR",
                                          ifelse(i == "ASW" || i == "LWK" || i == "YRI","AFR", NA))))
      y <- y +1
    }
#write.table(tab, "sbgroup_NV", row.names=F, quote=F, col.names=T)

#HLA_genes <- read.table('HLA_genes.txt', stringsAsFactors = F) # List of HLA genes of interest
HLA_genes <- read.table('HLA_DRB1_955', stringsAsFactors = F, header = T) # Import HLA expr data and HLA alleles in 2d
expr_data <- read.csv('HLA_955.csv', stringsAsFactors = F) # Import HLA expr data and HLA alleles in 2d
tab <- HLA_genes[,1:2]
tab <- as.data.frame(tab)
tab1 <- HLA_genes[,3:55]
#tab2 <- expr_data[,4]
## For loop to convert HLA alleles
gene <- "HLA_DRB1"
  HLA <- na.omit(as.character(unique(c(unique(expr_data[,paste(gene, "_allele1", sep = "")]), unique(expr_data[,paste(gene, "_allele2", sep = "")]))))) # Make a list of all existing HLA alleles for each gene
  print(HLA)
  z <- 1
  for(allele in HLA){ # Make a loop with all the HLA alleles for each gene, Convert the allele to 1,2 code (val1, val2), then to genotype (val3), and finally to recessive (val4) or dominant (val5) testable model
      val <- paste(gene, "_", allele,sep="")
      val1 <- paste(gene, "_", allele,"_2",sep="")
      val2 <- list(c(val,val1))
      val3 <- val2[[1]]
      #paste(val,val1,sep="")
      #(paste(gene, "_", allele ,sep="")))
      x <- 1
      for (i in tab1[,z]){
      tab[x,val3[1]] <- ifelse(i == "A/A", "A",
                            ifelse(i == "A/T", "A",
                                   ifelse(i == "T/T" , "T", NA)))
      
      tab[x,val3[2]] <- ifelse(i == "A/A", "A",
                            ifelse(i == "A/T", "T",
                                   ifelse(i == "T/T" , "T", NA)))
      x <- x+1
      
      }
      z <- z+1
  }
#expr_data[,3:32] <- NULL # Remove unwanted columns

# Save results
write.csv(tab, "HLA_DRB1_955NV.csv", row.names=F, quote=F, col.names=F,sep=",")
