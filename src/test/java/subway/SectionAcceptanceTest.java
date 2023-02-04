package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.response.LineAcceptanceTestUtils.*;
import static subway.response.StationAcceptanceTestUtils.createStation;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    private Long station1Id;
    private Long station2Id;
    private Long line1Id;

    /**
     * Given 지하철역과 노선 생성을 요청 하고
     */
    @BeforeEach
    void setup() {
        super.setUp();

        station1Id = createStation("강남역").jsonPath().getLong("id");
        station2Id = createStation("양재역").jsonPath().getLong("id");
        Map<String, Object> line1 = createLine("신분당선", "bg-red-600", station1Id, station2Id, 10);
        line1Id = createLineResponse(line1).jsonPath().getLong("id");
    }

    /**
     * given 새로운 역을 생성하고
     * when 지하철 구간을 생성하면
     * Then 지하철 구간을 조회 시 생성한 구간을 찾을 수 있다
     */
    @DisplayName("지하철 구간을 생성한다.")
    @Test
    void addSection() {
        //given
        Long station3Id = createStation("청계산입구역").jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> sectionResponse = createSectionResponse(line1Id, createSectionCreateParams(station2Id, station3Id));
        assertThat(sectionResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //then
        ExtractableResponse<Response> response = getLineResponse(line1Id);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(station1Id, station2Id, station3Id);

    }

    private ExtractableResponse<Response> createSectionResponse(Long lineId, Map<String, String> params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/line/{}/sections", lineId)
                .then().log().all().extract();
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", 6 + "");
        return params;
    }
}