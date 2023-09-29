package com.benzaied.gestiondestock.services.strategy;

import com.flickr4java.flickr.FlickrException;

import java.io.InputStream;

public interface Strategy<T> {

    T saveImage(Integer id ,InputStream image, String titre) throws FlickrException;

}
