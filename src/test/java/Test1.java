import org.junit.Assert;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
 
public class Test1 {

	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub
		try (final WebClient webClient = new WebClient()) {

	        // Get the first page
	        final HtmlPage page1 = webClient.getPage("http://provean.jcvi.org/genome_submit_2.php?species=human");
	        Assert.assertEquals("PROVEAN Human Genome Variants", page1.getTitleText());
	        final String pageAsText = page1.asText();
	        Assert.assertTrue(pageAsText.contains("Ensembl Family Description"));
	        System.out.print("New page" + pageAsText);
	        // Get the form that we are dealing with and within that form, 
	        // find the submit button and the field that we want to change.
	        //HtmlForm form = page1.getFirstByXPath(".//*[@id='CHR']");
	        final HtmlTextArea form = page1.getFirstByXPath(".//*[@id='CHR']");
	       //System.out.print("\tFORM" + form.getText());
	        //final HtmlForm form = page1.getFormByName("chrForm");
	        //final HtmlForm form = page1.getForms().get(0);
	        //System.out.print("form" + form.asText());
	        //final HtmlElement button = form.getFirstByXPath("//button[@class='envoyer']");
	        final HtmlSubmitInput button = form.getFirstByXPath(".//*[@type=submit]");
	        //final HtmlTextInput textField = form.getFirstByXPath(".//*[@name='CHR']");

	        // Change the value of the text field
	        form.setText("1,100382265,C,G");
	        System.out.print("form" + form.asText());
	        HtmlCheckBoxInput checkBox = page1.getFirstByXPath(".//*[@value='gene_id']");
	        checkBox.setAttribute("checked", "checked");
	        System.out.print("checkbox" + checkBox.asText());
	        //System.out.print("button" + button.asText());
	        // Now submit the form by clicking the button and get back the second page.
	        final HtmlPage page2 = button.click();
	    }

	}

}
