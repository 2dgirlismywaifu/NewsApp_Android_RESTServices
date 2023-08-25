/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.notelysia.restservices.legacykeygen.controller;

import com.notelysia.restservices.legacykeygen.model.winoem.WindowsOEMKey;
import com.notelysia.restservices.legacykeygen.model.winrtm.WindowsRTMKey;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v2")
@Tag(name = "Legacy Windows Key", description = "Legacy Windows Key Generator")
public class WindowsKeyController {
    private static final Logger logger = LogManager.getLogger(WindowsKeyController.class);
    WindowsOEMKey winOEMKey = new WindowsOEMKey();
    WindowsRTMKey winRTMKey = new WindowsRTMKey();

    @GetMapping(value = "/win", params = {"os"})
    public ResponseEntity<HashMap<String, String>> getWinKey(
            @Parameter(name = "os", description = "You must enter version Windows (95 or nt4)")
            @RequestParam(value = "os") String os) {
        HashMap<String, String> respond = new HashMap<>();
        switch (os) {
            case "95" -> {
                respond.put("Windows 95 OEM", winOEMKey.getWindows95Key());
                respond.put("Windows 95 Retail", winRTMKey.getWindows95Key());
                return ResponseEntity.ok(respond);
            }
            case "nt4" -> {
                respond.put("Windows NT 4 OEM", winOEMKey.getWindowsNTKey());
                respond.put("Windows NT 4 Retail", winRTMKey.getWindowsNTKey());
            }
            default -> {
                respond.put("key", "Invalid OS: " + os);
                logger.error("Invalid OS: " + os);
                return ResponseEntity.ok(respond);
            }
        }
        return ResponseEntity.ok(respond);
    }
}
