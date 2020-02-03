import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Assert;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
public class Test {

	public static void main(String[] args) throws Exception, MalformedURLException, IOException {
		// TODO Auto-generated method stub
		 try (final WebClient webClient = new WebClient()) {
		        final HtmlPage page = webClient.getPage("http://provean.jcvi.org/genome_submit_2.php?species=human");
		        Assert.assertEquals("PROVEAN Human Genome Variants", page.getTitleText());

//		        final String pageAsXml = page.asXml();
//		        Assert.assertTrue(pageAsXml.contains("<body class=\"composite\">"));
//
		        final String pageAsText = page.asText();
		        Assert.assertTrue(pageAsText.contains("Step"));
		        System.out.print("New page" + pageAsText);
		    }
	}

}
