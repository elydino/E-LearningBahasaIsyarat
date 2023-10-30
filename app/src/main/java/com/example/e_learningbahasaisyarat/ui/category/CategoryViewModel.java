package com.example.e_learningbahasaisyarat.ui.category;

import androidx.lifecycle.ViewModel;

public class CategoryViewModel extends ViewModel {

    private String docID;
    private String name;
    private int noOfTests;

    public CategoryViewModel(String docID, String name, int noOfTests) {
        this.docID = docID;
        this.name = name;
        this.noOfTests = noOfTests;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoOfTests() {
        return noOfTests;
    }

    public void setNoOfTests(int noOfTests) {
        this.noOfTests = noOfTests;
    }
}