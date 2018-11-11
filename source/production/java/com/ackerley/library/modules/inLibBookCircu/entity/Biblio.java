package com.ackerley.library.modules.inLibBookCircu.entity;

import com.ackerley.library.common.entity.BaseEntity;
import com.ackerley.library.modules.sys.entity.BiblioCls;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 在库书目
 */
public class Biblio extends BaseEntity {
    @JsonProperty(value="ISBN13")
    private String ISBN13;
    private BiblioCls cls;            //biblio classification 书目分类
    private String title;
    private String subtitle;
    private String originalTitle;
    private String authors;
    private String authorsIntro;      //作者简介
    private String translators;
    private String pages;             //页数
    private String summary;           //简介
    private String publisher;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date pubDate;
    private Float price;
    private String priceUnit;
    private String hrefCoverImg;      //封面图片
    private String hrefDouban;        //豆瓣书目页面

    //重写equals，集合的contains method用到...  for场景：biblio list去重...
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Biblio)) {
            return false;
        }
        Biblio other = (Biblio)obj;
        return other.getID().equals(this.getID());
    }

    public String getISBN13() {
        return ISBN13;
    }

    public void setISBN13(String ISBN13) {
        this.ISBN13 = ISBN13;
    }

    public BiblioCls getCls() {
        return cls;
    }

    public void setCls(BiblioCls cls) {
        this.cls = cls;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getAuthorsIntro() {
        return authorsIntro;
    }

    public void setAuthorsIntro(String authorsIntro) {
        this.authorsIntro = authorsIntro;
    }

    public String getTranslators() {
        return translators;
    }

    public void setTranslators(String translators) {
        this.translators = translators;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getPubDate() {
        return pubDate;
    }
    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public Float getPrice() {
        return price;
    }
    public void setPrice(Float price) {
        this.price = price;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public String getHrefCoverImg() {
        return hrefCoverImg;
    }

    public void setHrefCoverImg(String hrefCoverImg) {
        this.hrefCoverImg = hrefCoverImg;
    }

    public String getHrefDouban() {
        return hrefDouban;
    }

    public void setHrefDouban(String hrefDouban) {
        this.hrefDouban = hrefDouban;
    }
}