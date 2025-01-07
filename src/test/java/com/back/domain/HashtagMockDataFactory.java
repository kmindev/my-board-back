package com.back.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class HashtagMockDataFactory {

    /**
     * <p>
     * {@link Hashtag} 객체를 생성합니다.
     * <ul>
     *   <li>hashtagName: {@param hashtagName}</li>
     * </ul>
     * </p>
     *
     * @return {@link Hashtag} 객체
     */
    public static Hashtag createHashtagFromHashtagName(String hashtagName) {
        return Hashtag.from(hashtagName);
    }

    /**
     * <p>
     * {@link Hashtag} 객체를 생성합니다.
     * <ul>
     *   <li>id: {@code 1L}</li>
     *   <li>hashtagName: {@param hashtagName}</li>
     * </ul>
     * </p>
     *
     * @return {@link Hashtag} 객체
     */
    public static Hashtag createDBHashtagFromIdAndHashtagName(Long id, String hashtagName) {
        Hashtag hashtag = Hashtag.from(hashtagName);
        ReflectionTestUtils.setField(hashtag, "id", id);
        return hashtag;
    }

}
