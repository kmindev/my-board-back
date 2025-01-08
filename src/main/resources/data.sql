--- user_account
insert into user_account (user_id, user_password, email, nickname, memo, social_provider, social_id, role, created_at,
                          created_by, modified_at, modified_by)
values ('admin1', '{noop}qwer1234', 'admin1@naver.com', '별빛1', null, 'kakao', 'qdwd12ddd31dwd', 'ROLE_ADMIN',
        '2015-03-17 23:20:26', 'admin1', '2010-03-05 08:28:54', 'admin1');
insert into user_account (user_id, user_password, email, nickname, memo, social_provider, social_id, role, created_at,
                          created_by, modified_at, modified_by)
values ('user1', '{noop}qwer1234', 'user1@google.com', '아기상어5', null, null, null, 'ROLE_USER',
        '2002-11-16 04:23:32', 'user1', '2002-11-23 04:51:37', 'user1');
insert into user_account (user_id, user_password, email, nickname, memo, social_provider, social_id, role, created_at,
                          created_by, modified_at, modified_by)
values ('player789', '{noop}aOs420y6g', 'player789@google.com', '별빛2', null, null, null, 'ROLE_USER',
        '2013-04-04 05:12:41',
        'player789', '2003-09-26 15:31:56', 'player789');
insert into user_account (user_id, user_password, email, nickname, memo, social_provider, social_id, role, created_at,
                          created_by, modified_at, modified_by)
values ('customer331', '{noop}1L1LNKfD3pvi', 'customer331@daum.com', '아기상어3', null, 'kakao', 'asdqqwddwd12dvsd',
        'ROLE_USER',
        '2009-01-27 20:24:30', 'customer331', '2016-12-13 12:27:53', 'customer331');
insert into user_account (user_id, user_password, email, nickname, memo, social_provider, social_id, role, created_at,
                          created_by, modified_at, modified_by)
values ('customer341', '{noop}3Hp271Zo', 'customer341@google.com', '야구팬4', null, null, null, 'ROLE_USER',
        '2018-10-15 01:02:14', 'customer341', '2006-03-20 14:23:55', 'customer341');
insert into user_account (user_id, user_password, email, nickname, memo, social_provider, social_id, role, created_at,
                          created_by, modified_at, modified_by)
values ('user1234', '{noop}rTw80xiNO2as', 'user1234@google.com', '야구팬2', null, null, null, 'ROLE_USER',
        '2009-08-14 03:31:32',
        'user1234', '2007-01-18 11:24:26', 'user1234');
insert into user_account (user_id, user_password, email, nickname, memo, social_provider, social_id, role, created_at,
                          created_by, modified_at, modified_by)
values ('account4256', '{noop}0C1SoS29gaD', 'account4256@google.com', '아기상어1', null, 'kakao', 'adw2d12df3vv',
        'ROLE_USER',
        '2009-07-16 17:59:25', 'account4256', '2013-07-06 03:53:35', 'account4256');
insert into user_account (user_id, user_password, email, nickname, memo, social_provider, social_id, role, created_at,
                          created_by, modified_at, modified_by)
values ('customer3121', '{noop}Bp020cIHj', 'customer3121@naver.com', '달님2', null, null, null, 'ROLE_USER',
        '2006-04-21 02:25:32', 'customer3121', '2005-12-09 05:51:55', 'customer3121');
insert into user_account (user_id, user_password, email, nickname, memo, social_provider, social_id, role, created_at,
                          created_by, modified_at, modified_by)
values ('player7849', '{noop}7ov0AWw8F24s', 'player7849@naver.com', '별빛4', null, null, null, 'ROLE_USER',
        '2018-06-07 13:31:11', 'player7849', '2007-06-06 10:23:12', 'player7849');
insert into user_account (user_id, user_password, email, nickname, memo, social_provider, social_id, role, created_at,
                          created_by, modified_at, modified_by)
values ('account4526', '{noop}ukjt0YMye', 'account4526@naver.com', '아기상어2', null, null, null, 'ROLE_USER',
        '2015-05-05 21:44:54', 'account4526', '2018-01-30 14:20:46', 'account4526');

--- article
insert into article (user_id, title, content, created_at, created_by, modified_at, modified_by)
values ('admin1', '우승 예상팀', '우승 예상팀우승 예상팀우승 예상팀우승 예상팀우승 예상팀 #java #jpa', '2005-12-14', 'admin1', '2011-07-28',
        'admin1');
insert into article (user_id, title, content, created_at, created_by, modified_at, modified_by)
values ('admin1', '야구 시즌 시작', '야구 시즌 시작야구 시즌 시작야구 시즌 시작야구 시즌 시작야구 시즌 시작 #jpa', '2008-07-06', 'admin1',
        '2004-10-17', 'admin1');
insert into article (user_id, title, content, created_at, created_by, modified_at, modified_by)
values ('user1', '선수 부상 소식', '선수 부상 소식선수 부상 소식 #spring', '2019-07-28', 'user1', '2008-03-20', 'user1');
insert into article (user_id, title, content, created_at, created_by, modified_at, modified_by)
values ('user1', '야구 시즌 시작', '세 번째 게시글 내용', '2015-08-06', 'user1', '2012-09-25', 'user1');
insert into article (user_id, title, content, created_at, created_by, modified_at, modified_by)
values ('account4256', '우승 예상팀', '승 예상팀승 예상팀승 예상팀승 예상팀승 예상팀', '2016-05-03', 'account4256', '2004-02-28',
        'account4256');
insert into article (user_id, title, content, created_at, created_by, modified_at, modified_by)
values ('player7849', '이적설 확인', '이적설 확인이적설 확인이적설 확인이적설 확인', '2014-07-23', 'player7849', '2001-09-12',
        'player7849');
insert into article (user_id, title, content, created_at, created_by, modified_at, modified_by)
values ('player7849', '팀 승리 기록', '팀 승리 기록팀 승리 기록', '2006-04-28', 'player7849', '2001-04-30', 'player7849');
insert into article (user_id, title, content, created_at, created_by, modified_at, modified_by)
values ('player7849', '우승 예상팀', '우승 예상팀우승 예상팀', '2010-08-20', 'player7849', '2006-02-12', 'player7849');
insert into article (user_id, title, content, created_at, created_by, modified_at, modified_by)
values ('player7849', '야구 시즌 시작', '야구 시즌 시작야구 시즌 시작', '2014-10-30', 'player7849', '2016-06-27', 'player7849');
insert into article (user_id, title, content, created_at, created_by, modified_at, modified_by)
values ('player7849', '야구 시즌 시작', '야구 시즌 시작야구 시즌 시작야구 시즌 시작', '2003-08-10', 'player7849', '2008-09-28',
        'player7849');

--- hashtag
insert into hashtag (hashtag_name, created_at, created_by, modified_at, modified_by)
values ('#java', '2002-03-31', 'admin1', '2015-03-19', 'admin1');
insert into hashtag (hashtag_name, created_at, created_by, modified_at, modified_by)
values ('#jpa', '2007-03-13', 'admin1', '2011-05-21', 'admin1');
insert into hashtag (hashtag_name, created_at, created_by, modified_at, modified_by)
values ('#spring', '2013-06-22', 'admin1', '2020-08-04', 'admin1');

-- article_hashtag
insert into article_hashtag (article_id, hashtag_id, created_at, created_by, modified_at, modified_by)
values (1, 1, '2002-03-31', 'admin1', '2015-03-19', 'admin1');
insert into article_hashtag (article_id, hashtag_id, created_at, created_by, modified_at, modified_by)
values (1, 2, '2002-03-31', 'admin1', '2015-03-19', 'admin1');
insert into article_hashtag (article_id, hashtag_id, created_at, created_by, modified_at, modified_by)
values (2, 2, '2007-03-13', 'admin1', '2011-05-21', 'admin1');
insert into article_hashtag (article_id, hashtag_id, created_at, created_by, modified_at, modified_by)
values (3, 3, '2013-06-22', 'admin1', '2020-08-04', 'admin1');