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

package com.notelysia.restservices.controller.legacykeygen;

import com.notelysia.restservices.model.dto.legacykey.officekey.Office95;
import com.notelysia.restservices.model.dto.legacykey.officekey.Office97;
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
@Tag(name = "Legacy Office Key", description = "Legacy Office Key Generator")
public class OfficeKeyController {
    private static final Logger logger = LogManager.getLogger(OfficeKeyController.class);
    Office95 office95 = new Office95();
    Office97 office97 = new Office97();

    @GetMapping(value = "/office", params = {"version"})
    public ResponseEntity<HashMap<String, String>> getOfficekey(
            @Parameter(name = "version", description = "You must enter version Office (95 or 97)")
            @RequestParam(value = "version") String version) {
        HashMap<String, String> respond = new HashMap<>();
        switch (version) {
            case "95" -> {
                respond.put("Office 95", office95.getOffice95Key());
                return ResponseEntity.ok(respond);
            }
            case "97" -> {
                respond.put("Office 97", office97.getOffice97Key());
                return ResponseEntity.ok(respond);
            }
            default -> {
                respond.put("key", "Invalid Version: " + version);
                logger.error("Invalid Version: " + version);
            }
        }
        return ResponseEntity.ok(respond);
    }
}
