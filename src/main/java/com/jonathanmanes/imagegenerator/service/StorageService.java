package com.jonathanmanes.imagegenerator.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {

    @Resource(name = "amazonS3")
    private final AmazonS3 amazonS3Client;
    private final String bucketName = "dall-e-history";

    public void upload(String receivedUrl) {
        try {
            URL url = new URL(receivedUrl);
            InputStream inputStream = url.openStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/png");
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, bucketName + UUID.randomUUID() + ".png", inputStream, metadata).withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3Client.putObject(putObjectRequest);
            inputStream.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public InputStream download(String fileName) {
        try {
            S3Object object = amazonS3Client.getObject(bucketName, fileName);
            return object.getObjectContent();
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to download the file", e);
        }
    }

    public List<S3ObjectSummary> listObjects() {
        ObjectListing objectListing = amazonS3Client.listObjects(bucketName);
        return objectListing.getObjectSummaries();
    }

    public void deleteObject(String name) {
        amazonS3Client.deleteObject(bucketName, name);
    }
}
