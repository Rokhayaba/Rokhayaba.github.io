package com.ecn.ferretmvc.model;
public class InputRegion implements Comparable<InputRegion> {
    
    //Class containing all the information about a LOCUS
	private String chromosome;
	private int startPosition;
	private int endPosition;
	
	public InputRegion(String chromosome, int startPosition, int endPosition){
		this.chromosome = chromosome;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
	}
	
	public int compareTo(InputRegion o) {
		return this.startPosition - o.startPosition;
	}

	public int getStart(){
		return this.startPosition;
	}
        
        public void setStart(int s){
            this.startPosition=s;
        }
	
	public int getEnd(){
		return this.endPosition;
	}
        
        public void setEnd(int e){
            this.endPosition=e;
        }
	
	public String getChr(){
		return this.chromosome;
	}
	
	public int getChrAsInt(){
		if(chromosome.equals("X")){
			return 23;
		} else {
			return Integer.parseInt(chromosome);
		}
	}
	
}
