package hello.springmvc.basic.requestmapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class MappingController {

    private Logger log = LoggerFactory.getLogger(getClass());


    @RequestMapping("/hello-basic")
    public String helloBasic(){

        log.info("hello basic");

        return "ok";
    }

    /**
     * pathVariable 사용
     * 변수명 같으면 생략가능
     * @param data
     * @return
     */
    @GetMapping("/mapping/{userId}")
    public String mappingPath(@PathVariable("userId") String data){
        log.info("mappingPath userId={}", data);
        return "ok";
    }

    /**
     * 다중 pathVariable 사용
     * @param userId
     * @param orderId
     * @return
     */
    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    public String mappingPath(@PathVariable String userId,
                              @PathVariable Long orderId){
        log.info("mappingPath userId={}, orderId={}", userId, orderId );
        return "ok";
    }

    /**
     * 파라미터로 추가 매핑
     * params="mode",
     * params="!mode"
     * params="mode=debug"
     * params="mode!=debug" (! = )
     * params = {"mode=debug","data=good"}
     */
    //파라메터가 있어야 매핑이 됨(최근에는 경로변수를 주로 사용하기 때문에 이런 방식은 잘 사용하지 않아)
    @GetMapping(value = "/mapping-param", params = "mode=debug")
    public String mappingParam() {
        log.info("mappingParam");
        return "ok";
    }

    /**
     * 특정 헤더로 추가 매핑
     * headers="mode",
     * headers="!mode"
     * headers="mode=debug"
     * headers="mode!=debug" (! = )
     */
    //파라미터 매핑과 비슷하지만, HTTP 헤더를 사용한다.
    //테스트시 postman 사용
    @GetMapping(value = "/mapping-header", headers = "mode=debug")
    public String mappingHeader() {
        log.info("mappingHeader");
        return "ok";
    }


    /**
     * Content-Type 헤더 기반 추가 매핑 Media Type
     * consumes="application/json"
     * consumes="!application/json"
     * consumes="application/*"
     * consumes="*\/*"
     * MediaType.APPLICATION_JSON_VALUE
     */
//    Postman으로 테스트 해야 한다.
//    consumes: HTTP 요청의 Content-Type(아래 메소드의 경우 json) 헤더를 기반으로 미디어 타입으로 매핑한다.
//    만약 맞지 않으면 HTTP 415 상태코드(Unsupported Media Type)을 반환한다

    //뒤에 나올 produces 와 구분해서 잘 사용하자
    //produces 의 경우 해당 메소드가 생산해내는 컨텐츠로
    //view 에서 설정한 accept content 설정과 관련이 있다

    //반면 consume 의 경우
    //http 에서 해당 메서드로 전송하는 content-type 과 관련있다.
//    @PostMapping(value = "/mapping-consume", consumes = "application/json")
    @PostMapping(value = "/mapping-consume", consumes = MediaType.APPLICATION_JSON_VALUE) //처럼 사용
    public String mappingConsumes() {
        log.info("mappingConsumes");
        return "ok";
    }

    /**
     * Accept 헤더 기반 Media Type
     * produces = "text/html"
     * produces = "!text/html"
     * produces = "text/*"
     * produces = "*\/*"
     */
//    @PostMapping(value = "/mapping-produce", produces = "text/html")
    @PostMapping(value = "/mapping-produce", produces = MediaType.TEXT_HTML_VALUE)
    public String mappingProduces() {
        log.info("mappingProduces");
        return "ok";
    }


}
