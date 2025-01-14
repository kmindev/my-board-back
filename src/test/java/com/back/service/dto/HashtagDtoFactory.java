package com.back.service.dto;

public class HashtagDtoFactory {

    private static final Long DEFAULT_ID = 1L;
    private static final String DEFAULT_HASHTAG_NAME = "HASHTAG1";

    /**
     * <p>
     * 기본값으로 구성된 {@link HashtagDto} 객체를 생성합니다.
     * <ul>
     *   <li>id: {@link HashtagDtoFactory#DEFAULT_ID}</li>
     *   <li>hashtagName: {@link HashtagDtoFactory#DEFAULT_HASHTAG_NAME}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link HashtagDto} 객체
     */
    public static HashtagDto createHashtagDto() {
        return new HashtagDto(
                DEFAULT_ID, DEFAULT_HASHTAG_NAME,
                null, null, null, null
        );
    }

}
