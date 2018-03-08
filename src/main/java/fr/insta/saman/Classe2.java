package fr.insta.saman;

import java.util.List;

public class Classe2 {
    public String domain;
    public String url;
    public List<NotreClasse> node;

    public Classe2(String domain, String url) {
        this.domain = domain;
        this.url = url;
    }

    public Classe2(String domain, String url, List<NotreClasse> node) {
        this.domain = domain;
        this.url = url;
        this.node = node;
    }
}
