package com.ecn.ferretmvc.model;

public class FoundGeneAndRegion {
    
    //Class use for GENE search, containing genes names and LOCUS of these genes in LocationOfFoundGenes
    //an a boolean true if all genes were found
	private String geneNamesFound;
	private InputRegion[] locationOfFoundGenes;
	private Boolean allFound;
	
	FoundGeneAndRegion(String geneNamesFound, InputRegion[] locationOfFoundGenes, Boolean allFound){
		this.geneNamesFound = geneNamesFound; 
		this.locationOfFoundGenes = locationOfFoundGenes;
		this.allFound = allFound;
	}
	
	public String getFoundGenes(){
		return this.geneNamesFound;
	}
	
	public InputRegion[] getInputRegionArray(){
		return this.locationOfFoundGenes;
	}
	
	public Boolean getFoundAllGenes(){
		return this.allFound;
	}
	
}
