# 🛵 핫식스맨 (HotSixMan)

**Outsourcing 배달 애플리케이션 프로젝트**

배달 애플리케이션을 개발하는 **아웃소싱 프로젝트**로, 일정 내에 완성도를 높여 배포 가능한 애플리케이션을 구축하는 것을 목표로 진행되었습니다.

## 🚀 프로젝트 개요

- **프로젝트명:** 핫식스맨 (HotSixMan)
- **개발 기간:** 2024.02.28 ~ 2024.03.07
- **개발 인원:** 5명
- **사용 기술 스택:**
  - **Backend:** Spring Boot, JPA, MySQL, Redis, JWT
  - **Cloud Storage:** AWS S3
  - **API:** Kakao Maps API
  - **Version Control:** Git & GitHub

---

## 👨‍💻 팀원 소개 및 담당 역할

| 이름   | 역할  | 담당 기능 |
|--------|------|--------------------------------|
| **이정민** | 팀장 | 회원가입, 로그인, 소셜 로그인, 이미지 저장 |
| **류성현** | 팀원 | 리뷰, 통합 검색, 인기 검색어, 사용자 주소, 장바구니 |
| **윤현호** | 팀원 | 가게, 즐겨찾기, 가게 공지, 가게 대시보드 |
| **장윤혁** | 팀원 | 주문, 포인트 및 쿠폰 |
| **전승민** | 팀원 | 메뉴, 메뉴 옵션, 관리자 대시보드 |


---

## 📌 핵심 기능

### 🔥 인기 검색어 시스템
- **Redis의 Sorted Set을 활용**하여 실시간으로 인기 검색어를 관리.
- 검색어 점수를 자동 정렬하여 빠른 조회 가능.
- **15분 단위로 그룹화하여 카운팅 후, 1시간 뒤 자동 삭제.**

### 🏠 가게 및 유저 위치 기반 서비스
- **Kakao Maps API**를 이용하여 배달 가능 지역을 설정 (4KM 이내 가게만 주문 가능).
- **MySQL의 공간 함수(ST_CONTAINS, ST_Distance_Sphere)**를 활용한 거리 계산.

### 📊 대시보드 최적화
- **100만 개의 주문 데이터를 삽입하여 성능 테스트** 진행.
- **쿼리 최적화 및 인덱스 적용**하여 성능 개선.
- DATE_FORMAT 등의 좌변 연산을 피하고, 원본 데이터를 활용하여 조회 속도 향상.

### ⚡ 거리 계산 성능 최적화
- **ST_CONTAINS 공간 함수 사용 시 성능 저하** 문제 해결.
- **SPATIAL INDEX를 적용하여 조회 성능 개선.**

### 🔑 AOP를 활용한 권한 관리
- ADMIN, OWNER 권한 검증을 AOP 기반으로 구현하여 **비즈니스 로직과 권한 로직을 분리**.
- 코드 유지보수성이 높아짐.

---

## 📊 데이터베이스 ERD

핫식스맨 프로젝트의 데이터베이스 관계도입니다.

![image](https://github.com/user-attachments/assets/5940b0b9-6343-4845-9c2a-9b80332ba425)



---

## 📡 API 명세서

<details>
<summary>🔹 회원 관련 API</summary>

| Method  | 기능  | URL |
|---------|----------------|-----------------|
| `POST`   | 회원 가입  | `/auth/signup` |
| `PATCH`  | 회원 수정  | `/users/profile` |
| `DELETE` | 회원 탈퇴  | `/users` |
| `GET`    | 사용자 프로필 확인  | `/users/{id}` |
| `POST`   | 로그인  | `/auth/logins` |
| `POST`   | 로그아웃  | `/auth/logout` |

</details>

<details>
<summary>🔹 유저 주소 관련 API</summary>

| Method  | 기능  | URL |
|---------|-----------------|-------------------------------------|
| `GET`    | 주소 조회  | `/user-addresses` |
| `POST`   | 주소 생성  | `/user-addresses` |
| `PATCH`  | 주소 업데이트  | `/user-addresses/{userAddressId}` |
| `PATCH`  | 기본 주소로 업데이트  | `/user-addresses/{userAddressId}/default` |
| `DELETE` | 주소 삭제  | `/user-address/{userAddressId}` |

</details>

<details>
<summary>🔹 가게 관련 API</summary>

| Method  | 기능  | URL |
|---------|----------------|--------------------------------|
| `POST`   | 가게 생성  | `/stores` |
| `PATCH`  | 가게 수정  | `/stores/{storeId}` |
| `GET`    | 가게 다건 조회  | `/stores?searchKeyword=cq&page=&size=1&page=1&orderBy=rate` |
| `GET`    | 가게 카테고리 다건 조회  | `/stores/categories/{categoryId}?page=1&size=1&orderBy=rate` |
| `GET`    | 가게 단건 조회  | `/stores/{storeId}` |
| `DELETE` | 가게 폐업  | `/stores/{storeId}` |
| `PATCH`  | 가게 공지 수정  | `/stores/{storeId}/notice` |
| `PATCH`  | 가게 상태 수정  | `/stores/{storeId}/status` |

</details>

<details>
<summary>🔹 가게 즐겨찾기 관련 API</summary>

| Method  | 기능  | URL |
|---------|----------------------|------------------------------|
| `POST`   | 가게 즐겨찾기 토글  | `/stores/{storeId}/favorits` |
| `GET`    | 나의 즐겨찾기 가게 조회  | `/stores/favorits` |

</details>

<details>
<summary>🔹 가게 통계 관련 API</summary>

| Method  | 기능  | URL |
|---------|---------------------|---------------------------------|
| `GET`    | 가게 주문 수 통계 조회  | `/statistics/orders?date=2025-03-07` |
| `GET`    | 가게 매출 통계 조회  | `/statistics/price?date=2025-03-07` |

</details>

<details>
<summary>🔹 메뉴 관련 API</summary>

| Method  | 기능  | URL |
|---------|----------------|----------------------|
| `POST`   | 메뉴 생성  | `/menus` |
| `PATCH`  | 메뉴 수정  | `/menus/{menuId}` |
| `DELETE` | 메뉴 삭제  | `/menus/{menuId}` |

</details>

<details>
<summary>🔹 메뉴 옵션 관련 API</summary>

| Method  | 기능  | URL |
|---------|----------------|----------------------|
| `POST`   | 옵션 추가  | `/menus/{menuId}/options` |
| `PATCH`  | 옵션 수정  | `/menus/{menuId}/options/{optionId}` |
| `DELETE` | 옵션 삭제  | `/menus/{menuId}/options/{optionId}` |
| `GET`    | 옵션 다건 조회  | `/menus/{menuId}/options` |

</details>

<details>
<summary>🔹 주문 관련 API</summary>

| Method  | 기능  | URL |
|---------|----------------|------------------|
| `POST`   | 주문 생성  | `/orders` |
| `GET`    | 주문 내역 전체 조회  | `/orders` |
| `GET`    | 주문 내역 단건 조회  | `/orders/{orderId}` |
| `PUT`    | 주문 상태 수정  | `/orders/status` |
| `DELETE` | 주문 취소 (new 상태만 가능) | `/orders` |

</details>

<details>
<summary>🔹 쿠폰 관련 API</summary>

| Method  | 기능  | URL |
|---------|----------------|------------------|
| `POST`   | 쿠폰 생성  | `/coupons` |
| `GET`    | 쿠폰 전체 조회  | `/coupons` |
| `GET`    | 쿠폰 단건 조회  | `/coupons/{couponId}` |
| `DELETE` | 쿠폰 삭제  | `/coupons/{couponId}` |

</details>

<details>
<summary>🔹 리뷰 관련 API</summary>

| Method  | 기능  | URL |
|---------|----------------|----------------------|
| `POST`   | 리뷰 생성  | `/reviews/{orderId}` |
| `DELETE` | 리뷰 삭제  | `/reviews/{reviewId}` |
| `PATCH`  | 리뷰 수정  | `/reviews/{reviewId}` |
| `GET`    | 가게 리뷰 조회  | `/reviews/stores/{storeId}?last=2024-03-07T12:34:56&start=3&end=5` |

</details>

<details>
<summary>🔹 카테고리 관련 API</summary>

| Method  | 기능  | URL |
|---------|----------------|----------------------|
| `GET`    | 카테고리 다건 조회  | `/categories` |
| `POST`   | 카테고리 추가  | `/categories` |
| `DELETE` | 카테고리 제거  | `/categories/{categoryId}` |
| `PATCH`  | 카테고리 수정  | `/categories/{categoryId}` |

</details>

<details>
<summary>🔹 홈 관련 API</summary>

| Method  | 기능  | URL |
|---------|----------------|----------------------|
| `GET`    | 메인 화면 정보 조회  | `/home` |

</details>




---

## ⚠ 트러블슈팅

### 1️⃣ **대량 데이터 최적화 문제**
- **문제:** 대시보드에서 100만 개 이상의 주문 데이터를 다룰 때 성능 저하 발생.
- **해결:** 프로시저를 활용한 테스트 후 **쿼리 최적화 및 인덱스 추가**.

### 2️⃣ **거리 계산 성능 저하**
- **문제:** ST_CONTAINS 공간 함수 사용으로 성능이 떨어짐.
- **해결:** SPATIAL INDEX를 활용하여 성능 개선.

### 3️⃣ **권한 검증 로직 유지보수 문제**
- **문제:** ADMIN, OWNER 권한 검증이 여러 메서드에서 필요.
- **해결:** AOP 기반 검증을 적용하여 비즈니스 로직과 분리.

### 4️⃣ **Refresh Token 재발급 오류**
- **문제:** Refresh Token 발급 시 `400 Bad Request` 발생.
- **해결:** `userRole`을 포함하지 않도록 수정하여 보안 문제 해결.


