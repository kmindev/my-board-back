SELECT user_id, user_password,  email, nickname, memo, social_provider, social_id, created_at, created_by, modified_at, modified_by FROM USER_ACCOUNT;
SELECT id, user_id, title, content, created_at, created_by, modified_at, modified_by FROM ARTICLE;
SELECT id, article_id, user_id, content, created_at, created_by, modified_at, modified_by FROM COMMENT;
SELECT id, hashtag_name, created_at, created_by, modified_at, modified_by FROM HASHTAG;
SELECT id, article_id, hashtag_id, created_at, created_by, modified_at, modified_by FROM ARTICLE_HASHTAG;