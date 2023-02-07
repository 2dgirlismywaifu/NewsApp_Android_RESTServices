package com.notelysia.newsandroidservices.azure;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Configuration
public class AzureBlobConfig {
    @Value("${azure.storage.connection.string}")
    private String connectionString;

    @Value("${azure.storage.container.name}")
    private String containerName;

    @Bean
    public BlobServiceClient clobServiceClient() {
        BlobServiceClient blobServiceClient =
            new BlobServiceClientBuilder()
            .connectionString(new String(Base64.getDecoder().decode(connectionString)))
         .buildClient();

        return blobServiceClient;

   }

   @Bean
   public BlobContainerClient blobContainerClient() {
        BlobContainerClient blobContainerClient = clobServiceClient()
          .getBlobContainerClient(new String(Base64.getDecoder().decode(containerName)));

        return blobContainerClient;
    }
}
