package com.back.domain;

public class UserAccountMockDataFactory {

    private static final String DEFAULT_PASSWORD = "password1";
    private static final String DEFAULT_EMAIL = "user1@gmail.com";
    private static final String DEFAULT_NICKNAME = "닉네임1";

    /**
     * <p>
     * 기본값으로 구성된 {@link UserAccount} 객체를 생성합니다.
     * <ul>
     *   <li>userId: {@param userId}</li>
     *   <li>password: {@link UserAccountMockDataFactory#DEFAULT_PASSWORD}</li>
     *   <li>email: {@link UserAccountMockDataFactory#DEFAULT_EMAIL}</li>
     *   <li>nickname: {@link UserAccountMockDataFactory#DEFAULT_NICKNAME}</li>
     *   <li>role: {@link UserRoleType#USER}</li>
     * </ul>
     * </p>
     *
     * @param userId 유저 id
     * @return 기본값으로 구성된 {@link UserAccount} 객체
     */
    public static UserAccount createDBUserAccountFromUserId(String userId) {
        return UserAccount.of(
                userId,
                DEFAULT_PASSWORD,
                DEFAULT_EMAIL,
                DEFAULT_NICKNAME,
                null,
                UserRoleType.USER
        );
    }

}
