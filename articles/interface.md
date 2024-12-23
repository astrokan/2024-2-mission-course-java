# 인터페이스(`interface`)
앞서 저희는 객체의 메세지, 책임, 역할에 대해 알아보았어요.  
객체는 책임에 따라 결과물을 낼 의무가 있을 뿐, 그 구현에는 신경쓰지 않아요.  
하지만, 일반적인 class 에서는 구현을 제외할 수 없어요.  
그렇다면, 코드 없이 의사코드로만 객체를 표현할 수 있을까요? 
  
이 때 자바의 인터페이스 문법이 등장합니다.  
인터페이스는 메소드의 정의부로만 이루어진 클래스 정의로,  
여러분이 구현없이 객체간의 협력관계를 코드로 표현할 때 유용합니다.

이에 대해 다음의 자료를 통해 공부하고 정리해봅시다.

### [인터페이스의 개념과 문법](https://inpa.tistory.com/entry/JAVA-%E2%98%95-%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4Interface%EC%9D%98-%EC%A0%95%EC%84%9D-%ED%83%84%ED%83%84%ED%95%98%EA%B2%8C-%EA%B0%9C%EB%85%90-%EC%A0%95%EB%A6%AC#%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4_%EB%8F%85%EB%A6%BD_%ED%8C%8C%EC%9D%BC)

공부한 내용을 바탕으로,  
아래의 기능을 간단하게 `interface` 로 정의해봅시다.

> 결제 및 배달 시스템  
> 유저가 결제 시스템을 통해 결제하면, 창고의 재고가 감소하고, 배달 시스템으로 전달됩니다.   


### 여러분의 답변을 적어주세요!
```mermaid
classDiagram
    class 사람 {
    <<interface>>
    +먹기()
    +자기()
    +걷기()
    +주문요청하기()
    +항의전화하기()
    }
    
    class 회원 {
    -이름
    -아이디
    -비번
    -주문번호
    -휴대폰번호
    +먹기()
    +자기()
    +걷기()
    +주문요청하기()
    +항의전화하기()
    +할인 가격으로 주문하기()
    }
    class 비회원 {
    -이름
    -주문번호
    -휴대폰번호
    +먹기()
    +자기()
    +걷기()
    +주문요청하기()
    +항의전화하기()
    +회원가입하기()
    +바가지 가격으로 주문하기()
    
    }
    
    class 창고 {
    <<interface>>
    +재고여부파악()
    +재고줄이기()
    +상품제공()
    }
    
    class 실온창고 {
    -이름
    -지역
    -설치된 에어컨 수
    +재고여부파악()
    +재고줄이기()
    +상품제공()
    }
    
    class 냉동창고 {
    -이름
    -지역
    -온도
    +재고여부파악()
    +재고줄이기()
    +상품제공()
    }
    
    class 결제시스템 {
    - 상담 직원 수
    - 회원 리스트
    - 주문 리스트
    
    +로그인()
    +회원가입()
    +결제기록조회()
    }
    
    class 배달시스템 {
    -상품 위치 기록 리스트
    -배달원 리스트
    -반송 리스트
    +허브 변경하기()
    +배달완료 처리하기()
    }
    
    결제시스템 --> 사람
    결제시스템 --> 창고
    결제시스템 --> 배달시스템
    
    사람 <|-- 회원
    사람 <|-- 비회원
    
    창고 <|-- 냉동창고
    창고 <|-- 실온창고

```