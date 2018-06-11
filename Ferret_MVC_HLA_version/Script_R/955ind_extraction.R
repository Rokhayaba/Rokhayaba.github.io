#setwd("/Users/vince-n/Documents/Nico/Ordi_Nico_NIH/Nico/Results/KIR_HLA/HLA\ expression/Summer_2015_66K_HLA_expression")
setwd("~/Projet _ferret/Données_HLA")
## Import HLA gene list, HLA alleles

data <- read.csv('1KG_panel.csv', stringsAsFactors = F)
data1 <- read.csv('id_955.csv', stringsAsFactors = F)
tab2 <- data[is.element(data$id,data1$id),]
#write.table(tab2, "separated_CHB+JPT_955", row.names=F, quote=F, col.names=T)
