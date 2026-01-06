package com.project.finance_api.controller;

import com.project.finance_api.component.JwtUtil;
import com.project.finance_api.entity.Asset;
import com.project.finance_api.entity.User;
import com.project.finance_api.service.AssetService;
import com.project.finance_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/asset")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Asset> createAsset(@RequestBody Asset asset) {
        return ResponseEntity.ok(assetService.createAsset(asset));
    }

    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssets() {
        return ResponseEntity.ok(assetService.getAllAssets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long id) {
        return ResponseEntity.ok(assetService.getAssetById(id));
    }

    @GetMapping("/user")
    public ResponseEntity<List<Asset>> getAssetsByUser(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "").trim();
        User user = userService.getUserByToken(token);
        return ResponseEntity.ok(assetService.getAssetsByUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Asset> updateAsset(
            @PathVariable Long id,
            @RequestBody Asset asset
    ) {
        return ResponseEntity.ok(assetService.updateAsset(id, asset));
    }

    @DeleteMapping("/{id}")
    public void deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
    }

}
