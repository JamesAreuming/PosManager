select user(), database();

show databases;

use order9db;

show tables;

-- order9db 총 테이블 개수 : 113개
select count(*) from information_schema.TABLES where TABLE_SCHEMA ='order9db'; -- 113개

-- tb_user_group : 관리자 / 대표 / 브랜드 / 매장 / 지역에이전트
select * from tb_user_group;


-- ★ 오더9 기기 사용

-- (1) agent(대리점) 로그인 : installman / 1111
select * from tb_user where USERNAME = 'installman';

-- (2) 라이센스 등록
--     등록 라이센스 & 미등록 라이센스(STATUS 차이 : [등록]354002 / [미등록]354001)
select * from tb_svc_device_license where LICENSE_KEY = '5BFD-4C3F-9D44-BDD0' -- 등록라이센스
union
select * from tb_svc_device_license where LICENSE_KEY = 'C4D2-4FE5-88E3-D36C'; -- 미등록라이센스

-- (3) staff(매장) 로그인 : 
