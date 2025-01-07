package com.back.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("비즈니스 로직 - 해시태그 추출")
class HashtagExtractorTest {

    private final HashtagExtractor sut = new HashtagExtractor();

    @DisplayName("해시태그가 포함된 content를 전달하면, 해시태그명을 추출하여 Set 형식으로 반환한다.")
    @Test
    void givenContentWithHashtags_whenExtractHashtagNamesFromContent_thenReturnsHashtagNameSet() {
        // Given
        String content = "게시글 본문입니다. #JAVA, #JPA";

        // When
        Set<String> result = sut.extractHashtagNamesFromContent(content);

        // Then
        Set<String> expectedHashTagNames = Set.of("JAVA", "JPA");
        assertThat(result).isNotNull().containsExactlyElementsOf(expectedHashTagNames);
    }

    @DisplayName("해시태그가 포함되지 않은 content를 전달하면, 빈 Set을 반환한다.")
    @Test
    void givenContentWithoutHashtags_whenExtractHashtagNamesFromContent_thenReturnsEmptySet() {
        // Given
        String content = "게시글 본문입니다.";

        // When
        Set<String> result = sut.extractHashtagNamesFromContent(content);

        // Then
        assertThat(result).isEmpty();
    }

}