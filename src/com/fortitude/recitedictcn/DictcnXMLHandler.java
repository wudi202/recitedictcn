/* -*- coding: utf-8 -*-
 *
 * Copyright (C) 2011-2011 fortitude.zhang
 * 
 * Author: fortitude.zhang@gmail.com
 */

package com.fortitude.recitedictcn;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DictcnXMLHandler extends DefaultHandler {
    String pron = null;
    String def = null;
    String example = new String("");
    boolean buffering = false;
    StringBuffer buff = null;
    
    @Override
    public void startDocument() throws SAXException {
        // Some sort of setting up work
    } 
    
    @Override
    public void endDocument() throws SAXException {
        // Some sort of finishing up work
    } 
    
    @Override
    public void startElement(String namespaceURI, String localName, String qName, 
                             Attributes atts) throws SAXException {
        if (localName.equals("pron")) {
            buff = new StringBuffer("");
            buffering = true;
        }   
        else if (localName.equals("def")) {
            buff = new StringBuffer("");
            buffering = true;
        }
        else if (localName.equals("orig")) {
            buff = new StringBuffer("");
            buffering = true;
        }
        else if (localName.equals("trans")) {
            buff = new StringBuffer("");
            buffering = true;
        }
    } 
    
    @Override
    public void characters(char ch[], int start, int length) {
        if(buffering) {
            buff.append(ch, start, length);
        }
    } 
    
    @Override
    public void endElement(String namespaceURI, String localName, String qName) 
        throws SAXException {
        if (localName.equals("pron")) {
            buffering = false; 
            pron = buff.toString();
        }
        else if (localName.equals("def")) {
            buffering = false;
            def = buff.toString();
        }
        else if (localName.equals("orig")) {
            buffering = false;
            example = example +  buff.toString();
        }
        else if (localName.equals("trans")) {
            buffering = false;
            example = example +  buff.toString();
        }
    }

    public String getWordContent() {
        example = example.replaceAll("<em>", "");
        example = example.replaceAll("</em>", "");
	
        return "发音: " + pron + "\r\n释义: " + def + "\r\n例句: " + example;
    }
}
