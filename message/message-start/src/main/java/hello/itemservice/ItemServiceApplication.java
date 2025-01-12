package hello.itemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ItemServiceApplication {

	/*
		스프링부트에서는
		application.properties 파일에
		spring.messages.basename=messages,config.i18n.messages
		를 입력해주면 국제화가 가능하다

		default 값은
		spring.message.basename=messages 이다

		MessageSource 를 스프링 빈으로 등록하지 않고, 스프링 부트와 관련된 별도의 설정을 하지 않으면
		messages 라는  이름으로 기본 등록된다. 따라서
		messages_en.properties,  messages_ko.properties, messages.properties 파일만 등록하면 자동으로 인식된다

		메세지를 등록해놓으면 타임리프에서
		타임리프의 메시지 표현식 #{...}
		을 사용하여 스프링의 메시지를 편리하게 조회할 수 있다

		이렇게 properties 로 여러 언어를 등록하게되면
		사용브라우저의 언어에 따라 다르게 보여줄 수 있다

		해당 설정값에 따라 Accept-Language 정보가 http 헤더에 담겨 전송되는데
		MessageSource 에서는 Locale 정보를 알아야 언어 선택이 가능한데
		스프링에서는 이 Locale 설정값이 Accept-Language 정보에 따라 설정된다.

		스프링에서는 Locale 선택방식을 변경할 수 있도록 LocaleResolver 인터페이스를 제공하는데,
		스프링부트는 기본적으로 Accept-Language 를 활용하는 AcceptHeaderLocaleResolver 를 사용한다

	 */
	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

}
