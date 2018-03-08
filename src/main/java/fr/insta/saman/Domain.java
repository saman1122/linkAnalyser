package fr.insta.saman;

import java.util.List;

class Domain {
    public final String domain;
    public final String url;
    public List<Node> node;

    public Domain(String domain, String url) {
        this.domain = domain;
        this.url = url;
    }

    public Domain(String domain, String url, List<Node> node) {
        this.domain = domain;
        this.url = url;
        this.node = node;
    }
}
