package hello.hello_spring.domain;

import jakarta.persistence.*;

@Entity
public class Member {

    //@Id 어노테이션은 pk를 설정
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    
    //@Column(name = "name")으로 db에 저장된 칼럼명 지정해준다
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
