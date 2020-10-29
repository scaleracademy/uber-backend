package com.uber.uberapi.controllers;

import lombok.Builder;

import java.io.FileDescriptor;
class S3Connection {
}

class Image {
}

@Builder
public class S3Manager {
    S3Connection s3Connection;
    FileDescriptor fileDescriptor;

    private S3Manager() {
        s3Connection = new S3Connection();
        fileDescriptor = new FileDescriptor();
    }

    public String upload(Image img) {
        // upload the image
        // and return url
        return null;
    }

    public Image fetch(String uri) {
        // return the corresponding image
        return null;
    }
}


//    static {
//        // when the class is being defined by JVM
//        s3Connection = new S3Connection("config");
//    }
//    // advantages - simple to code, obvious
//    // disadvantages - high startup time - eager loading
//    // it might be impossible to init the resource at the class load time
