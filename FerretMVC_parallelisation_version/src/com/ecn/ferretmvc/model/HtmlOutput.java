package com.ecn.ferretmvc.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import javax.swing.JOptionPane;

// This class outputs an html file containing the frq file data, in the same folder and with the same name.
// The writeFile method writes the whole file,
public class HtmlOutput {

	// -------------------------------------------------------
	// ----------------- Declaring variables -----------------
	// -------------------------------------------------------

	private static final String TAB = "    ";

	private String absolutePath, name;
	private BufferedWriter htmlWriter;

	// ------------------------------------------------
	// ----------------- Constructors -----------------
	// ------------------------------------------------

	/**
	 * Creates an html file with the same name and path as the frq file
	 * 
	 * @param path
	 *            The path of the frq file
	 * @param name
	 *            The name of the frq file
	 */
	public HtmlOutput(String path, String name) {
		this.absolutePath = path;
		this.name = name;

		try {
			this.htmlWriter = new BufferedWriter(
					new FileWriter(absolutePath.substring(0, absolutePath.length() - 3) + "html"));
		} catch (IOException e) {
			e.printStackTrace();
			this.htmlWriter = null;
		}
	}

	// -------------------------------------------
	// ----------------- Methods -----------------
	// -------------------------------------------

	/**
	 * Main method which writes the whole html file, using sub-methods
	 * 
	 * @param annot
	 *            Specifies which option of annotations is selected : no, default,
	 *            advanced. Used to write the head lines of the html table.
	 */
	public void writeFile(String annot) {
		if (htmlWriter == null) {
			JOptionPane.showMessageDialog(null, "I/O exception occured, Ferret was unable to write html file", "Error",
					JOptionPane.OK_OPTION);
		} else {
			try {
				// Header of the html file : encoding and title
				write("<!DOCTYPE html>", 0);
				write("<html>", 0);
				write("<head>", 1);
				write("<meta charset=\"utf-8\" />", 2);
				write("<title>" + this.name + "</title>", 2);

				// <style> tag + css code of the page
				copyCss();

				// End of the header
				write("</head>", 1);
				htmlWriter.newLine();

				// Beginning of the body
				write("<body>", 1);

				// Beginning of the table : caption of the array
				write("<table>", 2);
				write("<caption>" + this.name + "</caption>", 3);

				// Head line of the array, depending on the annotation choice made by the user
				write("<thead>", 3);
				writeTableHead(annot);
				write("</thead>", 3);
				htmlWriter.newLine();

				// Content of the array
				writeTableBody(annot);
				htmlWriter.newLine();

				// Head line of the array copied at the end for convenience
				write("<tfoot>", 3);
				writeTableHead(annot);
				write("</tfoot>", 3);

				// End of the html file
				write("</table>", 2);
				write("</body>", 1);
				write("</html>", 0);

			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (htmlWriter != null) {
				try {
					htmlWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Writes the specified String with indentation in the html file and adds a new line afterwards.
	 * 
	 * @param str
	 *            The String to be written in the html file.
	 * @param offset
	 *            Number of tabulations (4 white spaces) used for the indentation of the
	 *            code.
	 * @throws IOException
	 *             In case of problems.
	 */
	private void write(String str, int offset) throws IOException {
		for (int i = 0; i < offset; i++) {
			str = TAB + str;
		}
		htmlWriter.write(str);
		htmlWriter.newLine();
	}

	/**
	 * Copies the .css file found in the src folder, into the header of the html
	 * file
	 */
	private void copyCss() {
		BufferedReader cssReader = null;
		String str;
		// Instantiates the buffered reader with the css file found in the resource
		// folder of the jar
		try {
			cssReader = new BufferedReader(
					new FileReader(new File(getClass().getResource("/style.css").toURI().getPath())));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		if (cssReader == null) {
			// if instantiation failed, an error message is added at the beginning of the
			// html file and html code is left raw
			try {
				write("<p>Ferret was unable to retrieve the css file, the current page is just raw html.</p>", 2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// writes the <style> tags and copies the css file
			try {
				write("<style type=\"text/css\">", 2);
				str = cssReader.readLine();
				while (str != null) {
					write(str, 0);
					str = cssReader.readLine();
				}
				write("</style>", 2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Writes the head of the html table, used for the head and the foot of the
	 * array.
	 * 
	 * @param annot
	 *            Changes the head of the table depending on whether the user chose
	 *            advanced, default or no annotations.
	 */
	private void writeTableHead(String annot) {
		try {
			write("<tr>", 4);

			// The columns written when "no annotation" is selected are always written
			write("<th>Chromosome</th>", 5);
			write("<th>Variant</th>", 5);
			write("<th>Position</th>", 5);
			write("<th>Allele 1</th>", 5);
			write("<th>Allele 2</th>", 5);
			write("<th>Nb chr</th>", 5);
			write("<th>1000G A1 frequency</th>", 5);
			write("<th>1000G A2 frequency</th>", 5);

			// Columns in common between default and advanced annotations
			if (annot.equals("def") || annot.equals("adv")) {
				write("<th>Gene name</th>", 5);
				write("<th>Gene ID</th>", 5);
				write("<th>Function</th>", 5);
				write("<th>Protein position</th>", 5);
				write("<th>AA change</th>", 5);
				write("<th>Protein acc</th>", 5);
			}

			// The only column specific to default annotations. Kept at the right extremity
			// of the array.
//			if (annot.equals("def")) {
//				write("<th>Other variants</th>", 5);
//			}

			// Only advanced notifications
			if (annot.equals("adv")) {
				write("<th>RegulomeDB score</th>", 5);
				//write("<th>RegulomeDB prediction</th>", 5);
				write("<th>Sift score</th>", 5);
				//write("<th>Sift prediction</th>", 5);
				write("<th>Polyphen score</th>", 5);
				//write("<th>Polyphen prediction</th>", 5);
				write("<th>Provean score</th>", 5);
				//write("<th>Provean prediction</th>", 5);
				write("<th>CADD score</th>", 5);
			}

			write("</tr>", 4);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes the lines of the html table which will contain the frq file data.
	 * Copies the frq file after it is written.
	 */
	private void writeTableBody(String annot) {
		BufferedReader frqReader = null;
		String str;
		int i;

		// Opens the frq file in a buffered reader
		try {
			frqReader = new BufferedReader(new FileReader(this.absolutePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Ferret was unable to find the frq file, please make sure you didn't alter it before the process ended",
					"Error", JOptionPane.OK_OPTION);
		}

		// Copies the frq file into an html table
		if (frqReader != null) {
			try {
				try{
				// The first line contains the head of the array and is not required here.
				str = frqReader.readLine();
				str = frqReader.readLine();
				System.out.println("str :" + str );
				String vert = "vert";
				String red = "red";
				String orange = "orange";
			
				

				while (str != null) {
					i = 0;
					write("<tr>", 4);
					String[] strSplit = str.split("\t");
					for (String s : strSplit) {
						// Some columns need a link on the data they contain. This switch block gives
						// them a particular treatment.
						switch (i) {
						case 1:
							write("<td><a href = \"https://www.ncbi.nlm.nih.gov/snp/" + s + "\">" + s + "</td>", 5);
							break;
						case 8:
							if (s.equals("unavailable")){
								write("<td>" + s + "</td>", 5);
							}
							else {
							write("<td><a href = \"https://www.ncbi.nlm.nih.gov/gene/" + strSplit[9] + "\">" + s
									+ "</td>", 5);
							}
							break;
						case 9:
							if (s.equals("unavailable")){
								write("<td>" + s + "</td>", 5);
							}
							else {
							write("<td><a href = \"https://www.ncbi.nlm.nih.gov/gene/" + s + "\">" + s + "</td>", 5);
							}
							break;
//						case 13:
//							if (annot.equals("def")) {
//								write("<td><a href = \"https://www.ncbi.nlm.nih.gov/gene/" + strSplit[9] + "\"></td>",
//										5);
//							} else {
//								write("<td>" + s + "</td>", 5);
//							}
							//break;
						case 14:
							// RegulomeDB links displays the rsid in the title if the position is decremented
							// by 1, else it displays "n/a". Other information in the page are the same if
							// position is left as it is
							String c;
							if (s.equals("not in the base")) {
								c = "The variant is not in the database but exists online";
//								write("<td class = \"" + c + "\"><a href = \"http://www.regulomedb.org/snp/chr"
//										+ strSplit[0] + "/" + String.valueOf(Integer.parseInt(strSplit[2]) - 1)
//										+ "\">Not in the base, link works though</td>", 5);
							} else {
								c = s;
//								write("<td class = \"" + c + "\"><a href = \"http://www.regulomedb.org/snp/chr"
//										+ strSplit[0] + "/" + String.valueOf(Integer.parseInt(strSplit[2]) - 1)
//										+ "\">" + c +"</td>", 5);
							}
							
							write("<td class = \"" + c + "\"><a href = \"http://www.regulomedb.org/snp/chr"
							+ strSplit[0] + "/" + String.valueOf(Integer.parseInt(strSplit[2]) - 1)
							+"\">" + c +"</td>", 5);
							break;
						case 15:
							// Regdb prediction skipped
							break;
						case 16:
							if (s.equals(".")){
								write("<td>" + s + "</td>", 5);
							}
							else {
							if (Float.valueOf(s) < 0.05){
								write("<td class = \"" + red + "\">" + s + "</td>", 5);
							}
							else{
								write("<td class = \"" + vert + "\">" + s + "</td>", 5);
							}
							}
							
//							else {
//								write("<td>" + s + "</td>", 5);
//							}
							break;
						case 17:
							// SIFT prediction skipped
							break;
						case 18:
							if (s.equals(".")){
								write("<td>" + s + "</td>", 5);
							}
							else{
							if ( Float.valueOf(s)> 0.453 && Float.valueOf(s)< 0.956){
								write("<td class = \"" + orange + "\">" + s + "</td>", 5);
							}
							else if (Float.valueOf(s)> 0.956){
								write("<td class = \"" + red + "\">" + s + "</td>", 5);
							}
							else {
								write("<td class = \"vert\">" + s + "</td>", 5);
							}
							
							
								
							}
							break;
						case 19:
							// polyphen  prediction skipped
							break;
						case 20:
							if (s.equals(".")){
								write("<td>" + s + "</td>", 5);
							}
							else{
							if (Float.valueOf(s) < 2.5){
								write("<td class = \"" + red + "\">" + s + "</td>", 5);
							}
							else {
								write("<td class = \"" + vert + "\">" + s + "</td>", 5);
							}
						}
							
							break;
						case 21:
							// Provean prediction skipped
							break;
						case 22:
							if (s.equals(".")){
								write("<td>" + s + "</td>", 5);
								}
							else{
							if (Float.valueOf(s) < 15){
								write("<td class = \"" + vert + "\">" + s + "</td>", 5);
							}
							else {
								write("<td class = \"" + red + "\">" + s + "</td>", 5);
							}
							}
							
							break;
						default:
							// In any other case, no link is added so the information remain as they are
							
							write("<td>" + s + "</td>", 5);
							
						}
						i++;
					}
					str = frqReader.readLine();
					write("</tr>", 4);
				}
				
				}catch(NumberFormatException ex){ // handle your exception
					System.out.println("not a number"); 
					}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
