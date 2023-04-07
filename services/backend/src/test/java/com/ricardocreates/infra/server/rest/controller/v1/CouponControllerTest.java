package com.ricardocreates.infra.server.rest.controller.v1;

import com.ricardocreates.infra.data.mongo.document.administrator.AdminRole;
import com.ricardocreates.infra.data.mongo.document.coupon.CouponDocument;
import com.ricardocreates.SpringTestContext;
import com.ricardocreates.infra.data.mongo.document.administrator.AdministratorDocument;
import com.ricardocreates.infra.data.mongo.document.coupon.DayOfWeekDocument;
import com.ricardocreates.infra.data.mongo.document.coupon.DiscountTypeDocument;
import com.ricardocreates.infra.data.mongo.document.coupon.LocationProductDocument;
import com.ricardocreates.infra.data.mongo.repository.administrator.MongoAdministratorClient;
import com.ricardocreates.infra.data.mongo.repository.coupon.MongoCouponClient;
import com.swagger.client.codegen.rest.model.CouponDto;
import com.swagger.client.codegen.rest.model.DayOfWeekDto;
import com.swagger.client.codegen.rest.model.LocationProductDto;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CouponControllerTest extends SpringTestContext {
    private static final String BASE_API = "/api/coupon";

    private static final ObjectId COUPON_ID = new ObjectId();
    private static final String COUPON_NAME = "name";
    private static final String COUPON_CODE = "code";
    private static final ObjectId ORGANISATION_ID = new ObjectId();
    private static final ObjectId LOCATION_ID = new ObjectId();

    @Autowired
    private MongoCouponClient mongoCouponClient;
    @Autowired
    private MongoAdministratorClient mongoAdministratorClient;
    @Autowired
    private WebTestClient webClient;

    @BeforeAll
    public static void setup() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @BeforeEach
    void setUp() {
        mongoCouponClient.deleteAll().block();
        mongoAdministratorClient.deleteAll().block();
        webClient = webClient.mutate()
                .responseTimeout(Duration.ofSeconds(360))
                .build();
    }

    @Test
    @DisplayName("should add coupon")
    void should_add_coupon() {
        // GIVEN
        CouponDto couponDto = mockCouponDto();

        // WHEN
        webClient.post()
                .uri(BASE_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(couponDto))
                .exchange()
                // THEN response
                .expectStatus().is2xxSuccessful()
        ;
        // THEN database
        CouponDocument couponDocument = mongoCouponClient.findById(COUPON_ID.toString()).block();
        assertThat(couponDocument).isNotNull();
        assertThat(couponDocument.getName()).isEqualTo(COUPON_NAME);
    }

    @Test
    @DisplayName("should not add coupon unique name")
    void should_not_add_coupon_uniqueName() {
        // GIVEN
        final String name = "name";
        final String couponToCreatedId = new ObjectId().toString();

        CouponDocument couponDoc = mockCouponDocument();
        couponDoc.setName(name);
        this.mongoCouponClient.save(couponDoc).block();
        CouponDto coupon = mockCouponDto();
        coupon.setId(couponToCreatedId);
        coupon.setName(name);
        // WHEN
        webClient.post()
                .uri(BASE_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(coupon))
                .exchange()
                .expectStatus().isBadRequest()
                // THEN response
                .expectBody(String.class)
                // THEN response
                .consumeWith(response -> {
                    System.out.println(response);
                    assertThat(response.getResponseBody()).contains("duplicated name");
                })
        ;
        // THEN database
        CouponDocument couponCreated = mongoCouponClient.findById(couponToCreatedId).block();
        assertThat(couponCreated).isNull();
    }

    @Test
    @DisplayName("should not add coupon unique code")
    void should_not_add_coupon_uniqueCode() {
        // GIVEN
        final String code = "code";
        final String couponToCreatedId = new ObjectId().toString();

        CouponDocument couponDoc = mockCouponDocument();
        couponDoc.setCode(code);
        this.mongoCouponClient.save(couponDoc).block();
        CouponDto coupon = mockCouponDto();
        coupon.setId(couponToCreatedId);
        coupon.setName(code);
        // WHEN
        webClient.post()
                .uri(BASE_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(coupon))
                .exchange()
                .expectStatus().isBadRequest()
                // THEN response
                .expectBody(String.class)
                // THEN response
                .consumeWith(response -> {
                    System.out.println(response);
                    assertThat(response.getResponseBody()).contains("duplicated code");
                })
        ;
        // THEN database
        CouponDocument couponCreated = mongoCouponClient.findById(couponToCreatedId).block();
        assertThat(couponCreated).isNull();
    }

    @Test
    @DisplayName("should not add coupon bad request")
    void should_not_add_coupon_badRequest() {
        // GIVEN
        CouponDto coupon = new CouponDto();
        // WHEN
        webClient.post()
                .uri(BASE_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(coupon))
                .exchange()
                // THEN response
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .consumeWith(response -> {
                    System.out.println(response);
                    assertThat(response.getResponseBody()).contains("shortDescription(NotNull)");
                    assertThat(response.getResponseBody()).contains("discount(NotNull)");
                    assertThat(response.getResponseBody()).contains("discountType(NotNull)");
                    assertThat(response.getResponseBody()).contains("active(NotNull)");
                    assertThat(response.getResponseBody()).contains("name(NotNull)");
                    assertThat(response.getResponseBody()).contains("dayOfWeek(NotNull)");
                    assertThat(response.getResponseBody()).contains("code(NotNull)");
                    assertThat(response.getResponseBody()).contains("activeTo(NotNull)");
                    assertThat(response.getResponseBody()).contains("activeFrom(NotNull)");
                })
        ;
    }

    @Test
    @DisplayName("should get coupon")
    void should_get_coupon() {
        // GIVEN
        CouponDocument coupon = mockCouponDocument();
        mongoCouponClient.save(coupon).block();
        // WHEN
        webClient.get()
                .uri(String.format("%s/%s", BASE_API, COUPON_ID))
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .exchange()
                // THEN response
                .expectStatus().is2xxSuccessful()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody()
                .jsonPath("id").isEqualTo(COUPON_ID.toString())
                .jsonPath("name").isEqualTo(COUPON_NAME)
        //.consumeWith(System.out::println)
        ;
    }


    @Test
    @DisplayName("should no get coupon not found")
    void should_no_get_coupon_notFound() {
        // GIVEN
        ObjectId notFoundId = new ObjectId();
        // WHEN
        webClient.get()
                .uri(String.format("%s/%s", BASE_API, notFoundId))
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .exchange()
                // THEN
                .expectStatus().isNotFound()
                .expectBody();
    }

    @Test
    @DisplayName("should remove coupon")
    void should_removeCoupon() {
        // GIVEN
        CouponDocument couponDocument = mockCouponDocument();
        mongoCouponClient.save(couponDocument).block();
        // WHEN
        webClient.delete()
                .uri(String.format("%s/%s", BASE_API, COUPON_ID))
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .exchange()
                // THEN
                .expectStatus().is2xxSuccessful()
                .expectBody()
        ;
        // THEN database
        var deletedAdmin = mongoCouponClient.findById(couponDocument.getId().toString()).block();
        assertThat(deletedAdmin).isNull();
    }

    @Test
    @DisplayName("should not remove coupon not found")
    void should_not_removeCoupon_notFound() {
        // GIVEN
        ObjectId id = new ObjectId();
        CouponDocument coupon = mockCouponDocument();
        coupon.setId(id);
        mongoCouponClient.save(coupon).block();
        //no coupon
        // WHEN
        webClient.delete()
                .uri(String.format("%s/%s", BASE_API, COUPON_ID))
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .exchange()
                // THEN
                .expectStatus().isNotFound()
                .expectBody()
        ;
        //THEN database
        var adminToNoDelete = mongoCouponClient.findById(id.toString()).block();
        var adminNotFound = mongoCouponClient.findById(COUPON_ID.toString()).block();
        assertThat(adminToNoDelete).isNotNull();
        assertThat(adminNotFound).isNull();
    }

    @Test
    @DisplayName("should update coupon")
    void should_update_coupon() {
        // GIVEN
        final String newName = "newLogin";
        final String newCode = "newCode";
        final boolean newActive = false;
        CouponDocument adminDocument = mockCouponDocument();
        mongoCouponClient.save(adminDocument).block();

        CouponDto adminUpdate = mockCouponDto();
        adminUpdate.setName(newName);
        adminUpdate.setCode(newCode);
        adminUpdate.setActive(newActive);
        // WHEN
        webClient.patch()
                .uri(BASE_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(adminUpdate))
                .exchange()
                // THEN response
                .expectStatus().is2xxSuccessful();
        // THEN database
        CouponDocument couponDocument = mongoCouponClient.findById(COUPON_ID.toString()).block();
        assertThat(couponDocument).isNotNull();
        assertThat(couponDocument.getName()).isEqualTo(newName);
        assertThat(couponDocument.getCode()).isEqualTo(newCode);
        assertThat(couponDocument.getActive()).isEqualTo(newActive);
    }

    @Test
    @DisplayName("should not update coupon unique name")
    void should_not_update_coupon_uniqueName() throws Exception {
        // GIVEN
        final String newName = "newLogin";
        final String newCode = "newCode";
        final boolean newActive = false;
        CouponDocument couponDocument = mockCouponDocument();
        mongoCouponClient.save(couponDocument).block();

        CouponDocument admin2Document = mockCouponDocument();
        admin2Document.setId(new ObjectId());
        admin2Document.setName(newName);
        mongoCouponClient.save(admin2Document).block();

        CouponDto couponDto = mockCouponDto();
        couponDto.setName(newName);
        couponDto.setCode(newCode);
        couponDto.setActive(newActive);
        // WHEN
        webClient.patch()
                .uri(BASE_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(couponDto))
                .exchange()
                // THEN response
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains("duplicated name");
                });
        // THEN database
        CouponDocument couponDoc = mongoCouponClient.findById(COUPON_ID.toString()).block();
        assertThat(couponDoc).isNotNull();
        assertThat(couponDoc.getName()).isNotEqualTo(newName);
        assertThat(couponDoc.getCode()).isNotEqualTo(newCode);
        assertThat(couponDoc.getActive()).isNotEqualTo(newActive);
    }

    @Test
    @DisplayName("should not update coupon unique code")
    void should_not_update_coupon_uniqueCode() throws Exception {
        // GIVEN
        final String newName = "newLogin";
        final String newCode = "newCode";
        final boolean newActive = false;
        CouponDocument couponDocument = mockCouponDocument();
        mongoCouponClient.save(couponDocument).block();

        CouponDocument admin2Document = mockCouponDocument();
        admin2Document.setId(new ObjectId());
        admin2Document.setCode(newCode);
        mongoCouponClient.save(admin2Document).block();

        CouponDto couponDto = mockCouponDto();
        couponDto.setName(newName);
        couponDto.setCode(newCode);
        couponDto.setActive(newActive);
        // WHEN
        webClient.patch()
                .uri(BASE_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(couponDto))
                .exchange()
                // THEN response
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains("duplicated code");
                });
        // THEN database
        CouponDocument couponDoc = mongoCouponClient.findById(COUPON_ID.toString()).block();
        assertThat(couponDoc).isNotNull();
        assertThat(couponDoc.getName()).isNotEqualTo(newName);
        assertThat(couponDoc.getCode()).isNotEqualTo(newCode);
        assertThat(couponDoc.getActive()).isNotEqualTo(newActive);
    }

    @Test
    @DisplayName("should not update coupon not found")
    void should_not_update_coupon_notFound() throws Exception {
        // GIVEN
        //final ObjectId idNotFound = new ObjectId();
        final CouponDto coupon = mockCouponDto();
        // WHEN
        webClient.patch()
                .uri(BASE_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(coupon))
                .exchange()
                // THEN response
                .expectStatus().isNotFound()
        ;
    }

    @Test
    @DisplayName("should not update coupon bad request")
    void should_not_update_coupon_badRequest() {
        // GIVEN
        CouponDto coupon = new CouponDto();
        // WHEN
        webClient.patch()
                .uri(BASE_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(coupon))
                .exchange()
                // THEN response
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .consumeWith(response -> {
                    System.out.println(response);
                    assertThat(response.getResponseBody()).contains("shortDescription(NotNull)");
                    assertThat(response.getResponseBody()).contains("discount(NotNull)");
                    assertThat(response.getResponseBody()).contains("discountType(NotNull)");
                    assertThat(response.getResponseBody()).contains("active(NotNull)");
                    assertThat(response.getResponseBody()).contains("name(NotNull)");
                    assertThat(response.getResponseBody()).contains("dayOfWeek(NotNull)");
                    assertThat(response.getResponseBody()).contains("code(NotNull)");
                    assertThat(response.getResponseBody()).contains("activeTo(NotNull)");
                    assertThat(response.getResponseBody()).contains("activeFrom(NotNull)");
                })
        ;
    }

    @Test
    @DisplayName("should find coupon")
    void should_find_coupon() throws Exception {
        // GIVEN
        //user
        AdministratorDocument adminDoc = mockAdministratorDocument(com.ricardocreates.domain.entity.administrator.AdminRole.ADMIN.name());
        this.mongoAdministratorClient.save(adminDoc).block();
        //coupon
        CouponDocument coupon = mockCouponDocument();
        mongoCouponClient.save(coupon).block();
        // WHEN
        webClient.get()
                .uri(BASE_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .exchange()
                // THEN response
                .expectStatus().is2xxSuccessful()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBodyList(CouponDto.class)
                .hasSize(1)
        ;
    }

    // SECURITY TEST
    @DisplayName("should add coupon according role")
    @ParameterizedTest
    @CsvSource({"ADMIN,200", "ADMIN_READ_ONLY,403", "MANAGEMENT,200", "NONE,401"})
    void should_add_coupon_according_role(String role, String expected) {
        // GIVEN
        com.ricardocreates.domain.entity.administrator.AdminRole adminRole = this.getRoleFromString(role);
        var bodyRequest = mockCouponDto();
        // WHEN
        webClient.post()
                .uri(BASE_API)
                .headers(http -> {
                    if (adminRole != null) {
                        http.setBearerAuth(this.getDefaultToken(adminRole));
                    }
                })
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bodyRequest))
                .exchange()
                // THEN response
                .expectStatus().isEqualTo(Integer.parseInt(expected));
    }

    @DisplayName("should update coupon according role")
    @ParameterizedTest
    @CsvSource({"ADMIN,200", "ADMIN_READ_ONLY,403", "MANAGEMENT,200", "NONE,401"})
    void should_update_coupon_according_role(String role, String expected) {
        // GIVEN
        com.ricardocreates.domain.entity.administrator.AdminRole adminRole = this.getRoleFromString(role);
        //coupon
        CouponDocument couponDocument = mockCouponDocument();
        mongoCouponClient.save(couponDocument).block();
        //request
        var bodyRequest = mockCouponDto();
        bodyRequest.setId(couponDocument.getId().toString());
        // WHEN
        webClient.patch()
                .uri(BASE_API)
                .headers(http -> {
                    if (adminRole != null) {
                        http.setBearerAuth(this.getDefaultToken(adminRole));
                    }
                })
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bodyRequest))
                .exchange()
                // THEN response
                .expectStatus().isEqualTo(Integer.parseInt(expected));
    }

    @DisplayName("should delete coupon according role")
    @ParameterizedTest
    @CsvSource({"ADMIN,200", "ADMIN_READ_ONLY,403", "MANAGEMENT,200", "NONE,401"})
    void should_delete_coupon_according_role(String role, String expected) {
        // GIVEN
        com.ricardocreates.domain.entity.administrator.AdminRole adminRole = this.getRoleFromString(role);
        //coupon
        CouponDocument couponDocument = mockCouponDocument();
        mongoCouponClient.save(couponDocument).block();
        // WHEN
        webClient.delete()
                .uri(String.format("%s/%s", BASE_API, couponDocument.getId()))
                .headers(http -> {
                    if (adminRole != null) {
                        http.setBearerAuth(this.getDefaultToken(adminRole));
                    }
                })
                .exchange()
                // THEN response
                .expectStatus().isEqualTo(Integer.parseInt(expected));
    }

    @DisplayName("should get coupon according role")
    @ParameterizedTest
    @CsvSource({"ADMIN,200", "ADMIN_READ_ONLY,200", "MANAGEMENT,200", "NONE,401"})
    void should_get_coupon_according_role(String role, String expected) {
        // GIVEN
        com.ricardocreates.domain.entity.administrator.AdminRole adminRole = this.getRoleFromString(role);
        //coupon
        CouponDocument couponDocument = mockCouponDocument();
        mongoCouponClient.save(couponDocument).block();
        // WHEN
        webClient.get()
                .uri(String.format("%s/%s", BASE_API, couponDocument.getId()))
                .headers(http -> {
                    if (adminRole != null) {
                        http.setBearerAuth(this.getDefaultToken(adminRole));
                    }
                })
                .exchange()
                // THEN response
                .expectStatus().isEqualTo(Integer.parseInt(expected));
    }

    private CouponDto mockCouponDto() {
        CouponDto coupon = new CouponDto();
        coupon.setId(COUPON_ID.toString());
        coupon.setName(COUPON_NAME);
        coupon.setCode(COUPON_CODE);
        coupon.setDiscountType(CouponDto.DiscountTypeEnum.AMOUNT);
        coupon.setActiveFrom("2022-12-29 14:20:00");
        coupon.setActiveTo("2022-12-30 14:20:00");
        coupon.setShortDescription("desc");
        coupon.setDiscount(new BigDecimal("1.0"));
        coupon.setActive(true);
        coupon.setDayOfWeek(mockDayOfWeekDto());
        return coupon;
    }

    private CouponDto mockCouponDtoWithLocationProduct(List<String> locationIds) {
        CouponDto coupon = mockCouponDto();
        coupon.setLocationProducts(mockLocationProductsDto(locationIds));
        return coupon;
    }

    private DayOfWeekDto mockDayOfWeekDto() {
        DayOfWeekDto day = new DayOfWeekDto();
        day.setMonday(true);
        return day;
    }

    private DayOfWeekDocument mockDayOfWeekDocument() {
        DayOfWeekDocument day = DayOfWeekDocument.builder().build();
        day.setMonday(true);
        return day;
    }

    private CouponDocument mockCouponDocument() {
        CouponDocument coupon = CouponDocument.builder().build();
        coupon.setId(COUPON_ID);
        coupon.setName(COUPON_NAME);
        coupon.setCode(COUPON_CODE);
        coupon.setDiscountType(DiscountTypeDocument.AMOUNT);
        coupon.setActiveFrom("2022-12-29 14:20:00");
        coupon.setActiveTo("2022-12-30 14:20:00");
        coupon.setShortDescription("desc");
        coupon.setDiscount(new BigDecimal("1.0"));
        coupon.setActive(true);
        coupon.setDayOfWeek(mockDayOfWeekDocument());
        return coupon;
    }

    private AdministratorDocument mockAdministratorDocument(String role) {
        AdministratorDocument admin = new AdministratorDocument();
        admin.setLogin(DEFAULT_LOGIN);
        admin.setPassword("test");
        admin.setRole(role != null ? AdminRole.valueOf(role) : null);
        return admin;
    }

    private CouponDocument mockCouponDocumentWithLocationProduct(List<String> locationIds) {
        CouponDocument coupon = mockCouponDocument();
        coupon.setLocationProducts(mockLocationProductsDocument(locationIds));
        return coupon;
    }

    private LocationProductDocument mockLocationProductDocument(String idLocation) {
        return LocationProductDocument.builder()
                .idLocation(idLocation)
                .build();
    }

    private List<LocationProductDocument> mockLocationProductsDocument(List<String> locationIds) {
        List<LocationProductDocument> list = new ArrayList<>();
        for (int index = 0; index < locationIds.size(); index++) {
            var locationId = locationIds.get(index);
            list.add(mockLocationProductDocument(locationId));
        }
        return list;
    }

    private LocationProductDto mockLocationProductDto(String idLocation) {
        var locProdDto = new LocationProductDto();
        locProdDto.setIdLocation(idLocation);
        return locProdDto;
    }

    private List<LocationProductDto> mockLocationProductsDto(List<String> locationIds) {
        List<LocationProductDto> list = new ArrayList<>();
        for (int index = 0; index < locationIds.size(); index++) {
            var locationId = locationIds.get(index);
            list.add(mockLocationProductDto(locationId));
        }
        return list;
    }
}
