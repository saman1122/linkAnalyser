package fr.insta.saman;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

class App
{
    private static HashMap<String,Integer> listUniqueDomain = new HashMap<>();

    public static void main( String[] args ) throws URISyntaxException {
        List<Domain> listDomain = new ArrayList<>();
        String urlPrincipal = args[0];

        List<Node> listNodes = getLinksFromURL(urlPrincipal);
        listDomain.add(new Domain(getDomainName(urlPrincipal), urlPrincipal, listNodes));

        for (Node nc : listNodes) {
            listDomain.add(new Domain(nc.domain, nc.url, getLinksFromURL(nc.url)));
            listUniqueDomain.putIfAbsent(nc.domain,listUniqueDomain.size()+1);
        }

        listDomain.forEach(x -> x.node.forEach(y ->  {
            System.out.println("Domain: " + y.domain + " URL: " + y.url);

        }) );

        System.out.println(listUniqueDomain.toString());

        Double [][] listTest = getMatriceUrl(listDomain);

        for (int i = 0; i<listTest.length;i++) {
            System.out.println();
            for (int j = 0; j < listTest[i].length;j++) {
                System.out.print( listTest[i][j] + "|");
            }
        }

        System.out.println('\n');

        Double [][] listTest2 = getMatriceNormalised(listTest);

        for (int i = 0; i<listTest2.length;i++) {
            System.out.println();
            for (int j = 0; j < listTest2[i].length;j++) {
                System.out.print( listTest2[i][j] + "|");
            }
        }

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
            listUniqueDomain.putIfAbsent(nc.domain,listUniqueDomain.size()+1);
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

    private static Double[][] getMatriceUrl( List<Domain> listDomain) {
        Double retour[][] = new Double[listUniqueDomain.size()][listUniqueDomain.size()];

        for (int i = 0; i<retour.length;i++) {
            for (int j = 0; j<retour[i].length; j++) retour[i][j] = 0d;
        }

        listDomain.forEach(x -> x.node.forEach( y -> {
            int domainColonne = listUniqueDomain.get(x.domain);
            int domainLigne = listUniqueDomain.get(y.domain);

            if (domainColonne != domainLigne) {
                retour[domainLigne-1][domainColonne-1] += 1d;
            }
        }));

        return retour;
    }

    private static Double[][] getMatriceNormalised(Double[][] matriceUrl) {
        Double retour[][] = matriceUrl.clone();

        for (int i=0; i<matriceUrl.length;i++) {
            double total = 0;
            for (int j = 0; j < matriceUrl[i].length; j++) {
                total += matriceUrl[j][i];
            }

            if (total > 0d) {
                for (int j = 0; j < matriceUrl[i].length; j++) {
                    matriceUrl[j][i] /= total;
                }
            }
        }

        return retour;
    }
}
