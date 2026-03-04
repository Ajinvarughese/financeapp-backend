package com.project.finance_api.controller;


import com.project.finance_api.component.FileUpload;
import com.project.finance_api.entity.Liability;
import com.project.finance_api.entity.User;
import com.project.finance_api.service.LiabilityService;
import com.project.finance_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/liability")
@RequiredArgsConstructor
public class LiabilityController {
    private final LiabilityService liabilityService;
    private final UserService userService;
    private final FileUpload fileUpload;

    @PostMapping(
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Liability> createLiability(
            @RequestPart("liability") Liability liability,
            @RequestPart(name = "file", required = false) MultipartFile document
    ) throws IOException {

        Liability newLiability = liabilityService.createLiability(liability);

        if (document != null && !document.isEmpty()) {
            String fileUrl = fileUpload.uploadFile(document);
            newLiability.setDocument(fileUrl);
            newLiability = liabilityService.updateDocumentString(newLiability);
        }

        return ResponseEntity.ok(newLiability);
    }

    @GetMapping
    public ResponseEntity<List<Liability>> getAllLiability() {
        return ResponseEntity.ok(liabilityService.getAllLiability());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Liability> getLiabilityById(@PathVariable Long id) {
        return ResponseEntity.ok(liabilityService.getLiabilityById(id));
    }

    @GetMapping("/user")
    public ResponseEntity<List<Liability>> getAssetsByUser(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "").trim();
        User user = userService.getUserByToken(token);
        return ResponseEntity.ok(liabilityService.getLiabilityByUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Liability> updateLiability(
            @PathVariable Long id,
            @RequestBody Liability liability
    ) {
        return ResponseEntity.ok(liabilityService.updateLiability(id, liability));
    }

    @DeleteMapping("/{id}")
    public void deleteLiability(@PathVariable Long id) {
        liabilityService.deleteLiability(id);
    }

}
