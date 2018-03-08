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

        System.out.println("");
        System.out.println("List domain");
        System.out.println("----------------" + '\n');

        listUniqueDomain.forEach((x,y)-> System.out.println("id : " + y + " domain: " + x));

        double [][] urlMatrix = getMatriceUrl(listDomain);

        System.out.println('\n');
        System.out.println("URL matrix");
        System.out.println("----------------");

        for (int i = 0; i<urlMatrix.length;i++) {
            System.out.println();
            for (int j = 0; j < urlMatrix[i].length;j++) {
                System.out.print( urlMatrix[j][i] + "|");
            }
        }

        System.out.println('\n');
        System.out.println("Normalized matrix");
        System.out.println("----------------");

        double [][] normalisedMatrix = getMatriceNormalised(urlMatrix);

        for (int i = 0; i<normalisedMatrix.length;i++) {
            System.out.println();
            for (int j = 0; j < normalisedMatrix[i].length;j++) {
                System.out.print( normalisedMatrix[j][i] + "|");
            }
        }

        double[][] v = new double [1][listUniqueDomain.size()];
        Random random = new Random();

        for (int i = 0; i < v.length; i++) {
            double total = 0d;
            for (int j = 0; j < v[i].length; j++) {
                v[i][j] = random.nextDouble()*1000;
                total += v[i][j];
            }

            for (int j = 0; j < v[i].length; j++) {
                v[i][j] /= total;
            }
        }

        double[][] ranking = Matrices.multiplicar(v, normalisedMatrix);

        System.out.println('\n');
        System.out.println("Ranking");
        System.out.println("----------------");
        System.out.println("Id | score");

        for (int i = 0; i<ranking[0].length;i++) {
            System.out.println(i+1 + " | " + ranking[0][i]);
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

    private static double[][] getMatriceUrl( List<Domain> listDomain) {
        double retour[][] = new double[listUniqueDomain.size()][listUniqueDomain.size()];

        listDomain.forEach(x -> x.node.forEach( y -> {
            int domainColonne = listUniqueDomain.get(x.domain);
            int domainLigne = listUniqueDomain.get(y.domain);

            if (domainColonne != domainLigne) {
                retour[domainColonne-1][domainLigne-1] += 1d;
            }
        }));

        return retour;
    }

    private static double[][] getMatriceNormalised(double[][] matriceUrl) {
        double retour[][] = matriceUrl.clone();

        for (int i=0; i<matriceUrl.length;i++) {
            double total = 0;
            for (int j = 0; j < matriceUrl[i].length; j++) {
                total += matriceUrl[i][j];
            }

            if (total > 0d) {
                for (int j = 0; j < matriceUrl[i].length; j++) {
                    matriceUrl[i][j] /= total;
                }
            }
        }

        return retour;
    }
}
