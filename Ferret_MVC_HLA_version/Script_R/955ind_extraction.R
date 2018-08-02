#setwd("/Users/vince-n/Documents/Nico/Ordi_Nico_NIH/Nico/Results/KIR_HLA/HLA\ expression/Summer_2015_66K_HLA_expression")
setwd("~/Projet _Ferret")
## Import HLA gene list, HLA alleles

data <- read.table('THBD_EUR_Annot.info', stringsAsFactors = F)
data1 <- read.table('THBM_EUR_EXONS.frq', stringsAsFactors = F)
tab2 <- data[is.element(data$V1,data1$V2),]
write.table(tab2, "THBDinfo", row.names=F, quote=F, col.names=T)