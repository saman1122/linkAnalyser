package fr.insta.saman;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Hello world!
 *
 */
public class App
{

    public static void main( String[] args ) throws URISyntaxException, MalformedURLException {
        List<Classe2> listClasse2 = new ArrayList<Classe2>();
        String urlPrincipal = "https://www-apr.lip6.fr/~buixuan/mrinsta2018";

        List<NotreClasse> notreClasses = getLinksFromURL(urlPrincipal);
        String domain = getDomainName(urlPrincipal);
        Classe2 classe2 = new Classe2(domain,urlPrincipal,notreClasses);
        listClasse2.add(classe2);

        for (NotreClasse nc : notreClasses) {
            List<NotreClasse> sousClasse = getLinksFromURL(nc.url);

            Classe2 classeSS = new Classe2(nc.domain,nc.url,sousClasse);
            listClasse2.add(classeSS);
        }

        listClasse2.forEach(x -> x.node.forEach(y ->  {
            System.out.println("Domain: " + y.domain + " URL: " + y.url);

        }) );

    }

    private static List<NotreClasse> getLinksFromURL(String url) throws URISyntaxException, MalformedURLException {
        List<NotreClasse> retour = new ArrayList<NotreClasse>();
        StringBuilder html = new StringBuilder();

        //Calculer domain
        URI uri = new URI(url);
        String domain=uri.getHost();

            try {
                URL oracle = new URL(url);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(oracle.openStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    html.append(inputLine);
                in.close();
            }catch (Exception ignored) {

            }

            HTMLLinkExtractor htmlLinkExtractor = new HTMLLinkExtractor();
            Vector<HTMLLinkExtractor.HtmlLink> links = htmlLinkExtractor.grabHTMLLinks(html.toString());


            for (int i = 0; i<links.size();i++) {
                HTMLLinkExtractor.HtmlLink htmlLink = links.get(i);
                NotreClasse nc = new NotreClasse();
                nc.url = htmlLink.getLink();
                nc.domain=getDomainName(nc.url);
                if (nc.domain.isEmpty()) nc.domain = getDomainName(url);
                if (!nc.url.startsWith("#")) retour.add(nc);
            }

        return retour;
    }

    private static String getDomainName(String url) throws URISyntaxException {
        String domain = "";

        if (isValid(url)) {
            URI uri = new URI(url);
            domain = uri.getHost();
            String[] split = domain.split("\\.");

            if (split.length > 1)
            domain = split[split.length-2] + "." + split[split.length-1];
        }

        return domain;
    }

    /* Returns true if url is valid */
    public static boolean isValid(String url)
    {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }
}
