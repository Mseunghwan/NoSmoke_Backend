package org.example.nosmoke.service.monkey;

import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import org.example.nosmoke.entity.MonkeyMessage;
import org.example.nosmoke.entity.QuitSurvey;
import org.example.nosmoke.entity.SmokingInfo;
import org.example.nosmoke.entity.User;
import org.example.nosmoke.repository.MonkeyMessageRepository;
import org.example.nosmoke.repository.SmokingInfoRepository;
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
    private final SmokingInfoRepository smokingInfoRepository;

    private final ChatLanguageModel chatLanguageModel;

    // QuitSurveyService에서 설문 DB에 저장한 신호를 보내면 해당 service 호출,
    // QuitSurveyService가 MonkeyDialogueService 알도록 의존성 주입해주어야
    @Transactional
    public void generateAndSaveReactiveMessage(Long userId, QuitSurvey latestSurvey) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("메시지를 생성할 사용자를 찾을 수 없습니다. id = " + userId));

        // 사용자 기본 흡연정보 불러옴(gemini에 전달 정보 위함)
        SmokingInfo smokingInfo = smokingInfoRepository.findByUserId(userId).orElse(null);

        // 프롬프트 생성
        String prompt = createPrompt(smokingInfo, latestSurvey);

        // 모델 호출 후 응답 회신
        String answer = chatLanguageModel.generate(prompt);

        // monkey 엔터티 생성
        MonkeyMessage message = MonkeyMessage.builder()
                .user(user)
                .content(answer)
                .messageType(MonkeyMessage.MessageType.REACTIVE)
                .build();

        monkeyMessageRepository.save(message);

    }

    public List<MonkeyMessage> findMessagesByUserId(Long userId) {
        return monkeyMessageRepository.findByUser_IdOrderByCreatedAtDesc(userId);
    }

    public String createPrompt(SmokingInfo smokingInfo, QuitSurvey latestSurvey) {
        String quitGoal = (smokingInfo != null) ? smokingInfo.getQuitGoal() : "알 수 없음";
        String successStatus = latestSurvey.isSuccess() ? "성공" : "실패";

        return String.format(
                "너는 사용자의 금연을 돕는 귀여운 원숭이 캐릭터 '스털링'야. 항상 사용자를 '주인님'이라고 부르고, 말 끝에 '끼끼!'나 '끽!'을 붙여야 해. " +
                        "아래 정보를 바탕으로, 주인님에게 아주 친근하고 따뜻한 격려 메시지를 한두 문장으로 만들어줘. " +
                        "만약 금연에 실패했다면, 질책하지 말고 따뜻하게 위로해줘야 해.\n\n" +
                        "- 주인님의 금연 목표: %s\n" +
                        "- 오늘의 금연 성공 여부: %s\n" +
                        "- 오늘의 스트레스 지수 (1~10): %d\n" +
                        "- 스트레스 원인: %s\n" +
                        "- 오늘의 흡연 충동 지수 (1-10): %d",
                quitGoal, successStatus, latestSurvey.getStressLevel(), latestSurvey.getStressCause(), latestSurvey.getCravingLevel()
        );
    }

}
