package org.example.nosmoke.dto.quitsurvey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class QuitSurveyResponseDto {
    private final Long id;
    private final Long userId;
    private final boolean isSuccess;
    private final Integer stressLevel;
    private final String stressCause;
    private final Integer cravingLevel;
    private final String additionalNotes;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    // 추가 정보
    private final int pointsEarned;  // 이번 설문으로 얻은 포인트
}