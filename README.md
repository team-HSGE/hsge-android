# 내 강아지 취향저격 "환승견애"
**사용자 위치 기반 반려견 매칭 서비스**

👉 동네 반려견 친구 추천 서비스 제공

👉 좋아요 기반 채팅 서비스 제공

👉 손흔들기 기능 제공을 통해 적극적인 커뮤니케이션 유도

## 1. UI

## 2. People
|<img width=150 src="https://user-images.githubusercontent.com/85485290/191734505-e5be8b0d-86e7-48f1-a673-716ff00272a0.png" />|<img width=150 src="https://user-images.githubusercontent.com/110798031/212794861-ee4da79b-8989-4bbe-8b6e-8c661e73fcd8.png" />|<img width=150 src="https://user-images.githubusercontent.com/110798031/212795353-baef1a29-a72f-4f7d-9d9d-15030d98b80d.png" />|
|:----:|:----:|:----:|
| [이서윤](https://github.com/seoyoon513) | [신예빈](https://github.com/syb8200) | [안정은](https://github.com/jaydks) |

## 3. Architecture
> Clean Architecture + MVVM Pattern

![image](https://user-images.githubusercontent.com/110798031/212804472-21d243d5-5ebe-4270-a7ec-dccf97648d15.png)

**Presentation**

- 화면에 애플리케이션 데이터를 표시하는 레이어입니다.
- 화면에 데이터를 렌더링하는 UI 요소를 포함하고, 데이터를 보유하고 로직을 처리하는 ViewModel을 포함합니다.

**Domain**
- 비즈니스 로직이 들어있는 레이어입니다.
- 비즈니스 로직에서 필요한 Model 과 UseCase를 포함합니다.

**Data**
- 데이터 노출 및 변경사항 등을 집중적으로 관리하는 레이어입니다.
- Local Repository가 존재하며 앱의 전반적인 비즈니스 로직을 처리합니다.



**Foldering**
```
├── common
│   └── constants
├── data
│   ├── api
│   ├── interface
│   ├── model
│   ├── repository
│   └── source
├── di
├── domain
│   ├── mapper
│   ├── model
│   ├── repository
│   ├── usecase
│   └── util
├── network
└── presentation
    ├── common
    ├── dialog
    ├── login
    ├── main
    ├── onboarding
    ├── register
    └── splash
```

## 4.Libraries
- Tools : Android Studio Dolphin
- Language : Kotlin
- Architecture Pattern : MVVM Pattern
- Android Architecture Components(AAC)
  - Flow
  - ViewModel
  - Coroutine
  - Data Binding
- Retrofit
- OkHttp
- Kotlin Serialization
- Hilt
- Kakao
- Kakao Map
- Timber
- Navigation
- Viewpager2
- CardStackView
- DataStore
- Google Location
- Stomp
- Glide
- Lottie

