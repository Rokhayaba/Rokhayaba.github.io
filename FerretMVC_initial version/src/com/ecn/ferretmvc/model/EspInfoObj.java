package com.ecn.ferretmvc.model;

public class EspInfoObj {

    //Class use for the data Exome Sequencing Project
    private String chromosome;
    private String snpName;
    private String refAllele;
    private String altAllele;
    private int position;
    private double eaFreq; 
    private double aaFreq;

    EspInfoObj(String chromosome, int position, String snpName, String refAllele, String altAllele, double eaFreq, double aaFreq) {
        this.chromosome = chromosome;
        this.snpName = snpName;
        this.position = position;
        this.eaFreq = eaFreq;
        this.aaFreq = aaFreq;
        this.refAllele = refAllele;
        this.altAllele = altAllele;
    }

    EspInfoObj(String chromosome, String position, String snpName, String refAllele, String altAllele, String refEA, String altEA, String refAA, String altAA) {
        this.chromosome = chromosome;
        this.snpName = snpName;
        this.position = Integer.parseInt(position);
        double totalEA = Double.parseDouble(refEA) + Double.parseDouble(altEA);
        double totalAA = Double.parseDouble(refAA) + Double.parseDouble(altAA);
        // Note that totalEA and totalAA are double so no need to worry about auto casting/floor to int
        this.eaFreq = (Double.parseDouble(refEA) / totalEA);
        this.aaFreq = (Double.parseDouble(refAA) / totalAA);
        this.refAllele = refAllele;
        this.altAllele = altAllele;
    }

    public String getChr() {
        return this.chromosome;
    }

    public int getChrAsInt() {
        return Integer.parseInt(chromosome);
    }

    public String getSNP() {
        return this.snpName;
    }

    public String getRefAllele() {
        return this.refAllele;
    }

    public String getAltAllele() {
        return this.altAllele;
    }

    public int getPos() {
        return this.position;
    }

    public double getEAFreq() {
        return this.eaFreq;
    }

    public double getAAFreq() {
        return this.aaFreq;
    }

    public double getEAFreqAlt() {
        return (1.0 - this.eaFreq);
    }

    public double getAAFreqAlt() {
        return (1.0 - this.aaFreq);
    }

}
