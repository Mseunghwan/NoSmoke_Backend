package org.example.nosmoke.dto.quitsurvey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.nosmoke.entity.QuitSurvey;

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
    private int pointsEarned;  // 이번 설문으로 얻은 포인트

    public QuitSurveyResponseDto(QuitSurvey quitSurvey) {
        this.id = quitSurvey.getId();
        this.userId = quitSurvey.getUserId();
        this.isSuccess = quitSurvey.isSuccess();
        this.stressLevel = quitSurvey.getStressLevel();
        this.stressCause = quitSurvey.getStressCause();
        this.cravingLevel = quitSurvey.getCravingLevel();
        this.additionalNotes = quitSurvey.getAdditionalNotes();
        this.createdAt = quitSurvey.getCreatedAt();
        this.modifiedAt = quitSurvey.getModifiedAt();
    }
}
