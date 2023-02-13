/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.notelysia.newsandroidservices;

public class RandomNumber {
    //generate random number with length = 6
    public String generateRandomNumber() {
        int length = 6;
        String numbers = "0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (numbers.length() * Math.random());
            sb.append(numbers.charAt(index));
        }
        return sb.toString();
    }

    public String generateSSONumber() {
        int length = 8;
        String numbers = "0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (numbers.length() * Math.random());
            sb.append(numbers.charAt(index));
        }
        return sb.toString();
    }

    public String generateVerifyNumber() {
        int length = 12;
        String numbers = "0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (numbers.length() * Math.random());
            sb.append(numbers.charAt(index));
        }
        return sb.toString();
    }
}
