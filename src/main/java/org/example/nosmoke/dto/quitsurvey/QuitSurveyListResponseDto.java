package org.example.nosmoke.dto.quitsurvey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class QuitSurveyListResponseDto {
    private final List<QuitSurveyResponseDto> surveys;
    private final int totalSurveys; // 설문 전체 수
    private final int successDays; // 성공 일수
    private final double successRate;  // 성공률
    private final double averageStressLevel; // 평균 스트레스 레벨
    private final double averageCravingLevel; // 평균 충동 레벨
}
