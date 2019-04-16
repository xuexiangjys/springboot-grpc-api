package com.xuexiang.springbootgrpcapi.model;

import javax.persistence.*;

@Table(name = "book")
public class Book {
    @Id
    @Column(name = "book_id")
    private Integer bookId;

    private String name;

    private Float price;

    private String author;

    @Column(name = "sales_volume")
    private Integer salesVolume;

    private Integer score;

    private Integer mark;

    private String description;

    private String picture;

    /**
     * @return book_id
     */
    public Integer getBookId() {
        return bookId;
    }

    /**
     * @param bookId
     */
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return price
     */
    public Float getPrice() {
        return price;
    }

    /**
     * @param price
     */
    public void setPrice(Float price) {
        this.price = price;
    }

    /**
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return sales_volume
     */
    public Integer getSalesVolume() {
        return salesVolume;
    }

    /**
     * @param salesVolume
     */
    public void setSalesVolume(Integer salesVolume) {
        this.salesVolume = salesVolume;
    }

    /**
     * @return score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * @param score
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * @return mark
     */
    public Integer getMark() {
        return mark;
    }

    /**
     * @param mark
     */
    public void setMark(Integer mark) {
        this.mark = mark;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * @param picture
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }
}