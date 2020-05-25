package org.zerock.security.domain;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.zerock.domain.MemberVO;

import lombok.Getter;

@Getter
public class CustomUser extends User {

	private static final long serialVersionUID = 1L;

	private MemberVO member; 

	//라이브러리 ...userdetail.User클래스를 상속하기 때문에 부모클래스 생성자를 호출해야만 정상적인 객체를 생성할수있음 
	public CustomUser(String username, String password,  
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	public CustomUser(MemberVO vo) { //MemberVO를 파라미터로 전달해서 User클래스에 맞게 생성자를 호출. 

		super(vo.getUserid(), vo.getUserpw(), vo.getAuthList().stream()
				.map(auth -> new SimpleGrantedAuthority(auth.getAuth())).collect(Collectors.toList())); //AuthVO인스턴스는 GrantedAuthorith객체로 변환해야 하므로 stream()과 map()을 이용해서 처리 

		this.member = vo;
	}
}
