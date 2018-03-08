package fr.insta.saman;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

class App
{

    public static void main( String[] args ) throws URISyntaxException {
        List<Domain> listDomain = new ArrayList<>();
        String urlPrincipal = args[0];

        List<Node> listNodes = getLinksFromURL(urlPrincipal);
        listDomain.add(new Domain(getDomainName(urlPrincipal), urlPrincipal, listNodes));

        for (Node nc : listNodes) {
            listDomain.add(new Domain(nc.domain, nc.url, getLinksFromURL(nc.url)));
        }

        listDomain.forEach(x -> x.node.forEach(y ->  {
            System.out.println("Domain: " + y.domain + " URL: " + y.url);

        }) );

    }

    private static List<Node> getLinksFromURL(String url) throws URISyntaxException {
        List<Node> retour = new ArrayList<>();
        StringBuilder html = new StringBuilder();

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


        for (HTMLLinkExtractor.HtmlLink htmlLink : links) {
            Node nc = new Node();
            nc.url = htmlLink.getLink();
            nc.domain = getDomainName(nc.url);
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
    private static boolean isValid(String url)
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
