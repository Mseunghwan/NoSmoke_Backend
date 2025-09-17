package org.example.nosmoke.service.quitsurvey;

import lombok.RequiredArgsConstructor;
import org.example.nosmoke.dto.quitsurvey.QuitSurveyRequestDto;
import org.example.nosmoke.entity.QuitSurvey;
import org.example.nosmoke.repository.QuitSurveyRepository;
import org.example.nosmoke.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuitSurveyService {
    private final QuitSurveyRepository quitSurveyRepository;
    private final UserRepository userRepository;

//    일일 금연 설문 저장 메서드
    @Transactional
    public QuitSurvey saveSurvey(Long userId, QuitSurveyRequestDto requestDto) {
        // User 확인
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. id = " + userId));

        QuitSurvey quitSurvey = new QuitSurvey(
                userId,
                requestDto.isSuccess(),
                requestDto.getStressLevel(),
                requestDto.getStressCause(),
                requestDto.getCravingLevel(),
                requestDto.getAdditionalNotes()
        );

        return quitSurveyRepository.save(quitSurvey);
    }

    // 특정 사용자 설문 기록 조회
    public List<QuitSurvey> findSurveyByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. id = " + userId));
        return quitSurveyRepository.findByUserId(userId);
    }
}
