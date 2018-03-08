package fr.insta.saman;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class HTMLLinkExtractor {

    private final Pattern patternTagA, patternLinkA;

    private static final String HTML_A_TAG_PATTERN = "(?i)<a([^>]+)>(.+?)</a>";
    private static final String HTML_A_HREF_TAG_PATTERN =
            "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
    HTMLLinkExtractor() {
        patternTagA = Pattern.compile(HTML_A_TAG_PATTERN);
        patternLinkA = Pattern.compile(HTML_A_HREF_TAG_PATTERN);
    }

    /**
     * Validate html with regular expression
     *
     * @param html
     *            html content for validation
     * @return Vector links and link text
     */
    public Vector<HtmlLink> grabHTMLLinks(final String html) {

        Vector<HtmlLink> result = new Vector<>();

        Matcher matcherTagA = patternTagA.matcher(html);

        while (matcherTagA.find()) {

            String href = matcherTagA.group(1); // href
            String linkText = matcherTagA.group(2); // link text

            Matcher matcherLinkA = patternLinkA.matcher(href);

            while (matcherLinkA.find()) {

                String link = matcherLinkA.group(1); // link
                HtmlLink obj = new HtmlLink();
                obj.setLink(link);
                obj.setLinkText(linkText);

                result.add(obj);

            }

        }

        return result;

    }

    class HtmlLink {

        String link;
        String linkText;

        HtmlLink(){}

        @Override
        public String toString() {
            return "Link : " + this.link +
                    " Link Text : " + this.linkText;
        }

        public String getLink() {
            return link;
        }

        void setLink(String link) {
            this.link = replaceInvalidChar(link);
        }

        public String getLinkText() {
            return linkText;
        }

        void setLinkText(String linkText) {
            this.linkText = linkText;
        }

        private String replaceInvalidChar(String link){
            link = link.replaceAll("'", "");
            link = link.replaceAll("\"", "");
            return link;
        }

    }
}
