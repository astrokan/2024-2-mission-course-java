# enum
java 의 enum 기능은 강력해요.  
생각보다 다양한 기능을 할 수 있고, 그만큼 복잡해요.  
한번 알아봅시다.  

기본적인 문법은 다음 자료를 확인해주세요!  
> [enum 기초와 응용](https://inpa.tistory.com/entry/JAVA-%E2%98%95-%EC%97%B4%EA%B1%B0%ED%98%95Enum-%ED%83%80%EC%9E%85-%EB%AC%B8%EB%B2%95-%ED%99%9C%EC%9A%A9-%EC%A0%95%EB%A6%AC#enum_%EC%84%A0%EC%96%B8)  

enum 의 예시로 에러 메시지를 한 곳에서 관리할 수 있어요.
아래의 예시를 한번 볼께요.
```java
    public enum Error {
        FILE_NOT_FOUND("file 경로를 찾을 수 없습니다."),
        FILE_FORMAT_NOT_MATCH("file 의 형식이 올바르지 않습니다."),
        INPUT_WRONG_INPUT("잘못된 입력입니다. 다시 입력해 주세요."),
        PRODUCT_EXCEEDED("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.")
        ;

        private final String message;
    
        Error(String message) {
            String prefix = Config.ERROR_PREFIX; // == [ERROR]
            this.message = prefix + " " + message;
        }
        public String message() {
            return message;
        }
    }
```
위와 같이 작성함으로서, 에러 메세지를 일괄적으로 관리할 수 있었어요.
`Error.FILE_NOT_FOUND` 로 호출된 메세지는 모두 동일한 메세지를 갖고, 변경에도 용이해요.  
또한, 어떤 에러들이 있는지도 한 곳에서 쉽게 확인할 수 있다는 장점이 있어요.  

이 외에도 에러에 따른 상태코드, 기본값 등 다양한 설정에 활용할 수 있어요.