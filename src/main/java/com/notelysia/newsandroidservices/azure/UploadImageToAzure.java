package com.notelysia.newsandroidservices.azure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
@RestController
@RequestMapping("/account/avatar/upload")
public class UploadImageToAzure {
    @Autowired
    private AzureBlobServices azureBlobAdapter;

   @PostMapping
   public ResponseEntity<HashMap<String,String>> upload
         (@RequestParam MultipartFile file)
               throws IOException {

      String fileName = azureBlobAdapter.upload(file);
      return ResponseEntity.ok().body(new HashMap<String,String>(){{
         put("fileName",fileName);
         put("status","success");
      }});
   }

   @GetMapping
   public ResponseEntity<List<String>> getAllBlobs() {

      List<String> items = azureBlobAdapter.listBlobs();
      return ResponseEntity.ok(items);
   }

   @DeleteMapping
   public ResponseEntity<Boolean> delete
           (@RequestParam String fileName) {

      azureBlobAdapter.deleteBlob(fileName);
      return ResponseEntity.ok().build();
   }

}
