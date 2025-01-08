package com.back.service;

import com.back.domain.Hashtag;
import com.back.repository.HashtagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.back.domain.HashtagMockDataFactory.createDBHashtagFromIdAndHashtagName;
import static com.back.domain.HashtagMockDataFactory.createHashtagFromHashtagName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("비즈니스 로직 - 해시태그")
@ExtendWith(MockitoExtension.class)
class HashtagServiceTest {

    @InjectMocks
    private HashtagService sut;

    @Mock
    private HashtagRepository hashtagRepository;
    @Mock
    private HashtagExtractor hashtagExtractor;

    @DisplayName("해시태그가 포함되지 않은 content를 전달하면, 빈 Set 반환한다.")
    @Test
    void givenContentWithoutHashtags_whenExtractAndSaveHashtags_thenReturnsEmptySet() {
        // Given
        String content = "";
        given(hashtagExtractor.extractHashtagNamesFromContent(content)).willReturn(Set.of());

        // When
        Set<Hashtag> result = sut.extractAndSaveHashtags(content);

        // Then
        assertThat(result).isEmpty();
        then(hashtagExtractor).should().extractHashtagNamesFromContent(content);
        then(hashtagRepository).shouldHaveNoInteractions();
    }

    @DisplayName("DB에 이미 모든 해시태그가 존재하면, DB에서 가져온 해시태그만 반환한다.")
    @Test
    void givenAllHashtagsExistInDB_whenExtractAndSaveHashtags_thenReturnsExistingHashtags() {
        // Given
        String content = "본문입니다. #EXIT_HATHTAG1 #EXIT_HASHTAG2";
        Set<String> hashtagNamesInContent = Set.of("EXIT_HATHTAG1", "EXIT_HASHTAG2");
        Set<Hashtag> existingHashtags = new HashSet<>(
                Set.of(
                        createDBHashtagFromIdAndHashtagName(1L, "EXIT_HATHTAG1"),
                        createDBHashtagFromIdAndHashtagName(2L, "EXIT_HASHTAG2")
                )
        );

        given(hashtagExtractor.extractHashtagNamesFromContent(content)).willReturn(hashtagNamesInContent);
        given(hashtagRepository.findByHashtagNameIn(hashtagNamesInContent)).willReturn(existingHashtags);

        // When
        Set<Hashtag> result = sut.extractAndSaveHashtags(content);

        // Then
        assertThat(result).containsExactlyInAnyOrderElementsOf(existingHashtags);
        then(hashtagRepository).should().findByHashtagNameIn(hashtagNamesInContent);
        then(hashtagRepository).shouldHaveNoMoreInteractions();
    }

    @DisplayName("새로운 해시태그가 있으면 저장 후 기존 + 새로운 해시태그 반환한다.")
    @Test
    void givenNewHashtags_whenExtractAndSaveHashtags_thenSavesAndReturnsAllHashtags() {
        // Given
        String content = "본문입니다. #NEW_HASHTAG #EXIT_HASHTAG";
        Set<String> hashtagNamesInContent = Set.of("NEW_HASHTAG", "EXIT_HASHTAG");
        Set<Hashtag> existingHashtags = new HashSet<>(
                Set.of(createDBHashtagFromIdAndHashtagName(1L, "EXIT_HASHTAG"))
        );
        List<Hashtag> newHashtags = List.of(createHashtagFromHashtagName("NEW_HASHTAG"));
        given(hashtagExtractor.extractHashtagNamesFromContent(content)).willReturn(hashtagNamesInContent);
        given(hashtagRepository.findByHashtagNameIn(hashtagNamesInContent)).willReturn(existingHashtags);
        given(hashtagRepository.saveAll(anySet())).willReturn(newHashtags);

        // When
        Set<Hashtag> result = sut.extractAndSaveHashtags(content);

        // Then
        assertThat(result).hasSize(hashtagNamesInContent.size());
        then(hashtagExtractor).should().extractHashtagNamesFromContent(content);
        then(hashtagRepository).should().findByHashtagNameIn(hashtagNamesInContent);
        then(hashtagRepository).should().saveAll(anySet());
    }


}