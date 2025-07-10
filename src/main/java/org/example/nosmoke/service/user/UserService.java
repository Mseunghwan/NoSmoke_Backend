package org.example.nosmoke.service.user;

import lombok.RequiredArgsConstructor;
import org.example.nosmoke.dto.user.UserLoginRequestDto;
import org.example.nosmoke.dto.user.UserLoginResponseDto;
import org.example.nosmoke.dto.user.UserSignupRequestDto;
import org.example.nosmoke.dto.user.UserSignupResponseDto;
import org.example.nosmoke.entity.User;
import org.example.nosmoke.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    // 회원가입
    @Transactional
    public UserSignupResponseDto signup(UserSignupRequestDto requestDto) {

        // 1. 중복 이메일 검증
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일 입니다.");
            // IllegalArgumentException은 적합하지 않은 인자를 메소드에 넘겨주었을때 발생시키는 예외
        }

        // 2. Dto를 Entity로 변환
        User user = new User(
                requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getPassword(), // 암호화 이후 시행하기에 우선은 getPassword로만
                0 // 초기 포인트
        );

        // 3. DB에 저장
        User savedUser = userRepository.save(user);

        // 4. Entity를 ResponseDto로 변환
        return new UserSignupResponseDto(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getPoint(),
                savedUser.getCreatedAt()
        );
    }

    // 로그인
    public UserLoginResponseDto login(UserLoginRequestDto requestDto) {
        // 1. 이메일로 사용자 조회
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. 비밀번호 검증
        if(!user.getPassword().equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        // 3. JWT 토큰 생성(아직 JWT 구현 안했으므로 temp_token으로 임시처리)
        String token = "temp_token_" + user.getId();

        // 4. ResponseDto 생성
        return new UserLoginResponseDto(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPoint()
        );

    }



    // 사용자 프로필 조회


    // 사용자 정보 수정


    // 포인트 수정


    // 사용자 존재 여부 확인

}
