package com.nhnacademy.ailibraryteam3batch.util;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.util.StringUtils;

public class TextPreprocessor {
    // html 태그
    private static final String HTML_TAG_PATTERN = "<]^>/*>";
    // 특수 문자
    private static final String SPECIAL_CHAR_PATTERN = "[^가-힣a-zA-Z0-9\\s]";
    // 공백
    private static final String SPACE_PATTERN = "\\s+";

    public static String preprocess(String text){
        if(!StringUtils.hasText(text)){
            return "";
        }

        // html 엔티티 디코딩
        String decoded = StringEscapeUtils.unescapeHtml4(text);

        // html 태그 제거
        String cleaned = decoded.replaceAll(HTML_TAG_PATTERN, " ");
        // 특수문자 제거
        cleaned = cleaned.replaceAll(SPECIAL_CHAR_PATTERN, "");
        // 연속된 공백 통합
        cleaned = cleaned.replaceAll(SPACE_PATTERN, " ");

        // 앞뒤 공백 제거 및 소문자 변환
        return cleaned.trim().toLowerCase();
    }
}
