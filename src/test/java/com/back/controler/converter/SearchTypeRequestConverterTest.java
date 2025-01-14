package com.back.controler.converter;

import com.back.domain.constant.SearchType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SearchTypeRequestConverterTest {

    private final SearchTypeRequestConverter sut = new SearchTypeRequestConverter();

    @Test
    void givenSearchTypeStr_whenConverter_thenReturnSearchType() {
        // Given
        String titleStr = "제목";
        String contentStr = "본문";
        String userIdStr = "작성자";
        String nicknameStr = "닉네임";
        String hashtagStr = "해시태그";
        String unexpectedStr = "제공X";

        // When
        SearchType titleResult = sut.convert(titleStr);
        SearchType contentResult = sut.convert(contentStr);
        SearchType userIdResult = sut.convert(userIdStr);
        SearchType nicknameResult = sut.convert(nicknameStr);
        SearchType hashtagResult = sut.convert(hashtagStr);
        SearchType unExpectedResult = sut.convert(unexpectedStr);

        // Then
        assertThat(titleResult).isEqualTo(SearchType.TITLE);
        assertThat(contentResult).isEqualTo(SearchType.CONTENT);
        assertThat(userIdResult).isEqualTo(SearchType.USER_ID);
        assertThat(nicknameResult).isEqualTo(SearchType.NICKNAME);
        assertThat(hashtagResult).isEqualTo(SearchType.HASHTAG);
        assertThat(unExpectedResult).isNull();
    }

}