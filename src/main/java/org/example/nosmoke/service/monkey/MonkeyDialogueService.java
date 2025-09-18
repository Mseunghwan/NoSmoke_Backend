package org.example.nosmoke.service.monkey;

import lombok.RequiredArgsConstructor;
import org.example.nosmoke.entity.MonkeyMessage;
import org.example.nosmoke.entity.QuitSurvey;
import org.example.nosmoke.entity.User;
import org.example.nosmoke.repository.MonkeyMessageRepository;
import org.example.nosmoke.repository.UserRepository;
import org.example.nosmoke.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MonkeyDialogueService {
    private final MonkeyMessageRepository monkeyMessageRepository;
    private final UserRepository userRepository;

    // QuitSurveyService에서 설문 DB에 저장한 신호를 보내면 해당 service 호출,
    // QuitSurveyService가 MonkeyDialogueService 알도록 의존성 주입해주어야
    @Transactional
    public void generateAndSaveReactiveMessage(Long userId, QuitSurvey latestSurvey) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("메시지를 생성할 사용자를 찾을 수 없습니다. id = " + userId));


    // AI 호출 전 간단한 규칙으로 메시지 내용 결정
        String content;

        if (latestSurvey.isSuccess()) {
           content = "오늘 금연 성공하셨네요!";
        }
        else {
            content = "오늘은 힘든 하루였나요? 괜찮아요! 내일 다시 금연 시작이에요!";
        }

        // monkey 엔터티 생성, 반응형 메시지 타입을로
        MonkeyMessage message = MonkeyMessage.builder()
                .user(user)
                .content(content)
                .messageType(MonkeyMessage.MessageType.REACTIVE)
                .build();

        monkeyMessageRepository.save(message);

    }

    public List<MonkeyMessage> findMessagesByUserId(Long userId) {
        return monkeyMessageRepository.findByUser_IdOrderByCreatedAtDesc(userId);
    }

}
