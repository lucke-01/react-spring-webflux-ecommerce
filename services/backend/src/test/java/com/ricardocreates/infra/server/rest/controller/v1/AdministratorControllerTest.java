package com.ricardocreates.infra.server.rest.controller.v1;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.ricardocreates.SpringTestContext;
import com.ricardocreates.application.service.JWTUtil;
import com.ricardocreates.domain.entity.administrator.AdminRole;
import com.ricardocreates.infra.data.mongo.document.administrator.AdministratorDocument;
import com.ricardocreates.infra.data.mongo.repository.administrator.MongoAdministratorClient;
import com.swagger.client.codegen.rest.model.AdministratorDto;
import com.swagger.client.codegen.rest.model.RestorePasswordDto;
import com.swagger.client.codegen.rest.model.SendForgotPasswordDto;
import com.swagger.client.codegen.rest.model.UpdateAdministratorDto;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class AdministratorControllerTest extends SpringTestContext {
    private static final String ADMINISTRATOR_API = "/api/administrator";
    private static final String SEND_FORGOT_PASSWORD_API = "send-forgot-password";
    private static final String RESTORE_PASSWORD_API = "restore-password";
    private static final ObjectId ADMINISTRATOR_ID = new ObjectId();
    private static final String ADMINISTRATOR_LOGIN = "test";
    private static final String EMAIL_FORGOTTEN = "test@gmail.com";
    private static final String RESTORE_PASSWORD_LOGIN = "login";
    private static final String RESTORE_PASSWORD_NEW_PASSWORD = "newPassword";
    private static final String RESTORE_PASSWORD_PASSWORD_TOKEN = "passwordToken";
    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("test", "password"))
            .withPerMethodLifecycle(false);
    @Autowired
    private MongoAdministratorClient mongoAdministratorClient;
    @Autowired
    private JWTUtil jwtTokenProvider;
    @Autowired
    private WebTestClient webClient;

    @BeforeAll
    public static void setup() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @BeforeEach
    void setUp() {
        mongoAdministratorClient.deleteAll().block();
    }

    @Test
    @DisplayName("should add administrator")
    void should_add_administrator() throws Exception {
        // GIVEN
        AdministratorDto admin = mockAdministratorDto();

        // WHEN
        webClient.post()
                .uri(ADMINISTRATOR_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(admin))
                .exchange()
                // THEN response
                .expectStatus().is2xxSuccessful();
        // THEN database
        AdministratorDocument adminDoc = mongoAdministratorClient.findById(ADMINISTRATOR_ID.toString()).block();
        assertThat(adminDoc).isNotNull();
        assertThat(adminDoc.getLogin()).isEqualTo(ADMINISTRATOR_LOGIN);
    }

    @Test
    @DisplayName("should not add administrator unique email")
    void should_not_add_administrator_uniqueEmail() throws Exception {
        // GIVEN
        final String email = "test@gmail.com";
        final String adminToCreatedId = new ObjectId().toString();

        AdministratorDocument adminDoc = mockAdministratorDocument();
        adminDoc.setEmail(email);
        adminDoc.setLogin("login3");
        this.mongoAdministratorClient.save(adminDoc).block();
        AdministratorDto admin = mockAdministratorDto();
        admin.setId(adminToCreatedId);
        admin.setEmail(email);
        admin.setLogin("login2");
        // WHEN
        webClient.post()
                .uri(ADMINISTRATOR_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(admin))
                .exchange()
                .expectStatus().isBadRequest()
                // THEN response
                .expectBody(String.class)
                // THEN response
                .consumeWith(response -> {
                    System.out.println(response);
                    assertThat(response.getResponseBody()).contains("duplicated email");
                })
        ;
        // THEN database
        AdministratorDocument adminCreated = mongoAdministratorClient.findById(adminToCreatedId).block();
        assertThat(adminCreated).isNull();
    }

    @Test
    @DisplayName("should not add administrator unique login")
    void should_not_add_administrator_uniqueLogin() throws Exception {
        // GIVEN
        final String login = "login";
        final String adminToCreatedId = new ObjectId().toString();

        AdministratorDocument adminDoc = mockAdministratorDocument();
        adminDoc.setEmail("test@gmail.com");
        adminDoc.setLogin(login);
        this.mongoAdministratorClient.save(adminDoc).block();
        AdministratorDto admin = mockAdministratorDto();
        admin.setId(adminToCreatedId);
        admin.setEmail("test2@gmail.com");
        admin.setLogin(login);
        // WHEN
        webClient.post()
                .uri(ADMINISTRATOR_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(admin))
                .exchange()
                .expectStatus().isBadRequest()
                // THEN response
                .expectBody(String.class)
                // THEN response
                .consumeWith(response -> {
                    System.out.println(response);
                    assertThat(response.getResponseBody()).contains("duplicated login");
                })
        ;
        // THEN database
        AdministratorDocument adminCreated = mongoAdministratorClient.findById(adminToCreatedId).block();
        assertThat(adminCreated).isNull();
    }

    @Test
    @DisplayName("should not add administrator bad request")
    void should_not_add_administrator_badRequest() throws Exception {
        // GIVEN
        String id = "badObjectId";
        AdministratorDto admin = mockAdministratorDto();
        admin.setId(id);
        // WHEN
        webClient.post()
                .uri(ADMINISTRATOR_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(admin))
                .exchange()
                // THEN response
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("error").isEqualTo("Internal Server Error")
        ;
    }

    @Test
    @DisplayName("should get administrator")
    void should_get_administrator() throws Exception {
        // GIVEN
        AdministratorDocument admin = mockAdministratorDocument();
        mongoAdministratorClient.save(admin).block();
        // WHEN
        webClient.get()
                .uri(String.format("%s/%s", ADMINISTRATOR_API, ADMINISTRATOR_ID.toString()))
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .exchange()
                // THEN response
                .expectStatus().is2xxSuccessful()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody()
                .jsonPath("id").isEqualTo(ADMINISTRATOR_ID.toString())
                .jsonPath("login").isEqualTo(ADMINISTRATOR_LOGIN)
        //.consumeWith(System.out::println)
        ;
    }

    @Test
    @DisplayName("should no get administrator not found")
    void should_no_get_administrator_notFound() throws Exception {
        // GIVEN
        ObjectId notFoundId = new ObjectId();
        // WHEN
        webClient.get()
                .uri(String.format("%s/%s", ADMINISTRATOR_API, notFoundId))
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .exchange()
                // THEN
                .expectStatus().isNotFound()
                .expectBody();
    }

    @Test
    @DisplayName("should remove administrator")
    void should_removeAdministrator() throws Exception {
        // GIVEN
        AdministratorDocument admin = mockAdministratorDocument();
        mongoAdministratorClient.save(admin).block();
        // WHEN
        webClient.delete()
                .uri(String.format("%s/%s", ADMINISTRATOR_API, ADMINISTRATOR_ID))
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .exchange()
                // THEN
                .expectStatus().is2xxSuccessful()
                .expectBody()
        ;
        // THEN database
        var deletedAdmin = mongoAdministratorClient.findById(admin.getId().toString()).block();
        assertThat(deletedAdmin).isNull();
    }

    @Test
    @DisplayName("should not remove administrator not found")
    void should_not_removeAdministrator_notFound() throws Exception {
        // GIVEN
        ObjectId adminId = new ObjectId();
        AdministratorDocument admin = mockAdministratorDocument();
        admin.setId(adminId);
        mongoAdministratorClient.save(admin).block();
        //no administrator
        // WHEN
        webClient.delete()
                .uri(String.format("%s/%s", ADMINISTRATOR_API, ADMINISTRATOR_ID))
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .exchange()
                // THEN
                .expectStatus().isNotFound()
                .expectBody()
        ;
        //THEN database
        var adminToNoDelete = mongoAdministratorClient.findById(adminId.toString()).block();
        var adminNotFound = mongoAdministratorClient.findById(ADMINISTRATOR_ID.toString()).block();
        assertThat(adminToNoDelete).isNotNull();
        assertThat(adminNotFound).isNull();
    }

    @Test
    @DisplayName("should update administrator")
    void should_update_administrator() throws Exception {
        // GIVEN
        final String newLogin = "newLogin";
        final String newPassword = "newPassword";
        AdministratorDocument adminDocument = mockAdministratorDocument();
        mongoAdministratorClient.save(adminDocument).block();

        AdministratorDto adminUpdate = mockAdministratorDto();
        adminUpdate.setLogin(newLogin);
        adminUpdate.setPassword(newPassword);
        // WHEN
        webClient.patch()
                .uri(ADMINISTRATOR_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(adminUpdate))
                .exchange()
                // THEN response
                .expectStatus().is2xxSuccessful();
        // THEN database
        AdministratorDocument adminDoc = mongoAdministratorClient.findById(ADMINISTRATOR_ID.toString()).block();
        assertThat(adminDoc).isNotNull();
        assertThat(adminDoc.getLogin()).isEqualTo(newLogin);
        assertThat(adminDoc.getPassword()).isEqualTo("TLBt+RKc48dhcNAa9NQ8yW0WVbkeoHTjc/zh+eK+WqE=");
    }

    @Test
    @DisplayName("should not update administrator unique email")
    void should_not_update_administrator_uniqueEmail() throws Exception {
        // GIVEN
        final String newLogin = "newLogin";
        final String newEmail = "newEmail@gmail.com";
        final String newPassword = "newPassword";
        final boolean newActive = false;
        final boolean newSuperAdmin = false;
        AdministratorDocument adminDocument = mockAdministratorDocument();
        mongoAdministratorClient.save(adminDocument).block();

        AdministratorDocument admin2Document = mockAdministratorDocument();
        admin2Document.setId(new ObjectId());
        admin2Document.setEmail(newEmail);
        mongoAdministratorClient.save(admin2Document).block();

        UpdateAdministratorDto adminUpdate = mockUpdateAdministratorDto();
        adminUpdate.setEmail(newEmail);
        adminUpdate.setLogin(newLogin);
        adminUpdate.setPassword(newPassword);
        // WHEN
        webClient.patch()
                .uri(ADMINISTRATOR_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(adminUpdate))
                .exchange()
                // THEN response
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains("duplicated email");
                });
        // THEN database
        AdministratorDocument adminDoc = mongoAdministratorClient.findById(ADMINISTRATOR_ID.toString()).block();
        assertThat(adminDoc).isNotNull();
        assertThat(adminDoc.getLogin()).isNotEqualTo(newLogin);
        assertThat(adminDoc.getPassword()).isNotEqualTo("TLBt+RKc48dhcNAa9NQ8yW0WVbkeoHTjc/zh+eK+WqE=");
    }

    @Test
    @DisplayName("should not update administrator unique login")
    void should_not_update_administrator_uniqueLogin() throws Exception {
        // GIVEN
        final String newLogin = "newLogin";
        final String newEmail = "newEmail@gmail.com";
        final String newPassword = "newPassword";
        final boolean newActive = false;
        final boolean newSuperAdmin = false;
        AdministratorDocument adminDocument = mockAdministratorDocument();
        mongoAdministratorClient.save(adminDocument).block();

        AdministratorDocument admin2Document = mockAdministratorDocument();
        admin2Document.setId(new ObjectId());
        admin2Document.setLogin(newLogin);
        mongoAdministratorClient.save(admin2Document).block();

        UpdateAdministratorDto adminUpdate = mockUpdateAdministratorDto();
        adminUpdate.setEmail(newEmail);
        adminUpdate.setLogin(newLogin);
        adminUpdate.setPassword(newPassword);
        // WHEN
        webClient.patch()
                .uri(ADMINISTRATOR_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(adminUpdate))
                .exchange()
                // THEN response
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains("duplicated login");
                });
        // THEN database
        AdministratorDocument adminDoc = mongoAdministratorClient.findById(ADMINISTRATOR_ID.toString()).block();
        assertThat(adminDoc).isNotNull();
        assertThat(adminDoc.getLogin()).isNotEqualTo(newLogin);
        assertThat(adminDoc.getPassword()).isNotEqualTo("TLBt+RKc48dhcNAa9NQ8yW0WVbkeoHTjc/zh+eK+WqE=");
    }

    @Test
    @DisplayName("should not update administrator not found")
    void should_not_update_administrator_notFound() throws Exception {
        // GIVEN
        //final ObjectId idNotFound = new ObjectId();
        final AdministratorDto adminUpdate = mockAdministratorDto();
        // WHEN
        webClient.patch()
                .uri(ADMINISTRATOR_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(adminUpdate))
                .exchange()
                // THEN response
                .expectStatus().isNotFound()
        ;
    }

    @Test
    @DisplayName("should not update administrator bad request")
    void should_not_update_administrator_badRequest() throws Exception {
        // GIVEN
        AdministratorDocument adminDocument = mockAdministratorDocument();
        mongoAdministratorClient.save(adminDocument).block();

        AdministratorDto adminUpdate = mockAdministratorDto();
        adminUpdate.setId("badId");
        // WHEN
        webClient.patch()
                .uri(ADMINISTRATOR_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(adminUpdate))
                .exchange()
                // THEN response
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("should find administrator")
    void should_find_administrator() throws Exception {
        // GIVEN
        AdministratorDocument administrator = mockAdministratorDocument();
        mongoAdministratorClient.save(administrator).block();

        AdministratorDto administratorDto = mockAdministratorDto();
        // WHEN
        webClient.method(HttpMethod.GET)
                .uri(ADMINISTRATOR_API)
                .headers(http -> http.setBearerAuth(this.getDefaultToken()))
                .body(Mono.just(administratorDto), AdministratorDto.class)
                .exchange()
                // THEN response
                .expectStatus().is2xxSuccessful()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBodyList(AdministratorDto.class)
                .hasSize(1)
        ;
    }

    @Test
    @DisplayName("should send forgotPassword")
    void should_send_forgotPassword() throws Exception {
        // GIVEN
        SendForgotPasswordDto sendForgot = mockSendForgotPasswordDto();
        AdministratorDocument adminDocument = mockAdministratorDocument();
        mongoAdministratorClient.save(adminDocument).block();
        String url = String.format("%s/%s", ADMINISTRATOR_API, SEND_FORGOT_PASSWORD_API);
        // WHEN
        webClient.post()
                .uri(url)
                .body(Mono.just(sendForgot), SendForgotPasswordDto.class)
                .exchange()
                // THEN response
                .expectStatus().is2xxSuccessful();
        // THEN database
        AdministratorDocument adminDoc = mongoAdministratorClient.findById(adminDocument.getId().toString()).block();
        assertThat(adminDoc).isNotNull();
        assertThat(adminDoc.getForgetPasswordToken()).isNotNull();
    }

    @Test
    @DisplayName("should not send forgotPassword email null")
    void should_not_send_forgotPassword_badEmail() throws Exception {
        // GIVEN
        SendForgotPasswordDto sendForgot = mockSendForgotPasswordDto();
        sendForgot.setEmail(null);
        String url = String.format("%s/%s", ADMINISTRATOR_API, SEND_FORGOT_PASSWORD_API);
        // WHEN
        webClient.post()
                .uri(url)
                .body(Mono.just(sendForgot), SendForgotPasswordDto.class)
                .exchange()
                .expectStatus().is4xxClientError()
                // THEN response
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains("email(NotNull)");
                });
        // THEN database
    }

    @Test
    @DisplayName("should not send forgotPassword invalid email")
    void should_not_send_forgotPassword_invalidEmail() throws Exception {
        // GIVEN
        SendForgotPasswordDto sendForgot = mockSendForgotPasswordDto();
        sendForgot.setEmail("invalidEmail");
        String url = String.format("%s/%s", ADMINISTRATOR_API, SEND_FORGOT_PASSWORD_API);
        // WHEN
        webClient.post()
                .uri(url)
                .body(Mono.just(sendForgot), SendForgotPasswordDto.class)
                .exchange()
                .expectStatus().is4xxClientError()
                // THEN response
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains("email(Email)");
                });
        // THEN database
    }

    @Test
    @DisplayName("should not send forgotPassword email notFound")
    void should_not_send_forgotPassword_notFound() throws Exception {
        // GIVEN
        SendForgotPasswordDto sendForgot = mockSendForgotPasswordDto();
        AdministratorDocument adminDocument = mockAdministratorDocument();
        String url = String.format("%s/%s", ADMINISTRATOR_API, SEND_FORGOT_PASSWORD_API);
        // WHEN
        webClient.post()
                .uri(url)
                .body(Mono.just(sendForgot), SendForgotPasswordDto.class)
                .exchange()
                // THEN response
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("should restore password")
    void should_restorePassword() throws Exception {
        // GIVEN
        RestorePasswordDto restorePasswordDto = mockRestorePasswordDto();
        AdministratorDocument adminDocument = mockAdministratorDocument();
        adminDocument.setLogin(RESTORE_PASSWORD_LOGIN);
        adminDocument.setForgetPasswordToken(RESTORE_PASSWORD_PASSWORD_TOKEN);
        adminDocument.setPassword(null);
        mongoAdministratorClient.save(adminDocument).block();
        String url = String.format("%s/%s", ADMINISTRATOR_API, RESTORE_PASSWORD_API);
        // WHEN
        webClient.post()
                .uri(url)
                .body(Mono.just(restorePasswordDto), RestorePasswordDto.class)
                .exchange()
                // THEN response
                .expectStatus().is2xxSuccessful();
        // THEN database
        AdministratorDocument adminDoc = mongoAdministratorClient.findById(adminDocument.getId().toString()).block();
        assertThat(adminDoc).isNotNull();
        assertThat(adminDoc.getForgetPasswordToken()).isNull();
        assertThat(adminDoc.getPassword()).isNotNull();
    }

    @Test
    @DisplayName("should not restore password bad request")
    void should_not_restorePassword_badRequest() throws Exception {
        // GIVEN
        RestorePasswordDto restorePasswordDto = mockRestorePasswordDto();
        restorePasswordDto.setLogin("");
        restorePasswordDto.setNewPassword("");
        restorePasswordDto.setPasswordToken(null);
        String url = String.format("%s/%s", ADMINISTRATOR_API, RESTORE_PASSWORD_API);
        // WHEN
        webClient.post()
                .uri(url)
                .body(Mono.just(restorePasswordDto), RestorePasswordDto.class)
                .exchange()
                // THEN response
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains("login(Size)");
                    assertThat(response.getResponseBody()).contains("passwordToken(NotNull)");
                    assertThat(response.getResponseBody()).contains("newPassword(Size)");
                });
    }

    @Test
    @DisplayName("should not restore password not found login")
    void should_not_restorePassword_notFoundLogin() throws Exception {
        // GIVEN
        RestorePasswordDto restorePasswordDto = mockRestorePasswordDto();
        AdministratorDocument adminDocument = mockAdministratorDocument();
        adminDocument.setLogin("notFound");
        adminDocument.setForgetPasswordToken(RESTORE_PASSWORD_PASSWORD_TOKEN);
        adminDocument.setPassword(null);
        mongoAdministratorClient.save(adminDocument).block();
        String url = String.format("%s/%s", ADMINISTRATOR_API, RESTORE_PASSWORD_API);
        // WHEN
        webClient.post()
                .uri(url)
                .body(Mono.just(restorePasswordDto), RestorePasswordDto.class)
                .exchange()
                // THEN response
                .expectStatus().isNotFound();
        // THEN database
        AdministratorDocument adminDoc = mongoAdministratorClient.findById(adminDocument.getId().toString()).block();
        assertThat(adminDoc).isNotNull();
        assertThat(adminDoc.getForgetPasswordToken()).isNotNull();
        assertThat(adminDoc.getPassword()).isNull();
    }

    @Test
    @DisplayName("should not restore password not found token")
    void should_not_restorePassword_notFoundToken() throws Exception {
        // GIVEN
        RestorePasswordDto restorePasswordDto = mockRestorePasswordDto();
        AdministratorDocument adminDocument = mockAdministratorDocument();
        adminDocument.setLogin(RESTORE_PASSWORD_LOGIN);
        adminDocument.setForgetPasswordToken("badToken");
        adminDocument.setPassword(null);
        mongoAdministratorClient.save(adminDocument).block();
        String url = String.format("%s/%s", ADMINISTRATOR_API, RESTORE_PASSWORD_API);
        // WHEN
        webClient.post()
                .uri(url)
                .body(Mono.just(restorePasswordDto), RestorePasswordDto.class)
                .exchange()
                // THEN response
                .expectStatus().is4xxClientError();
        // THEN database
        AdministratorDocument adminDoc = mongoAdministratorClient.findById(adminDocument.getId().toString()).block();
        assertThat(adminDoc).isNotNull();
        assertThat(adminDoc.getForgetPasswordToken()).isNotNull();
        assertThat(adminDoc.getPassword()).isNull();
    }

    // SECURITY TEST
    @DisplayName("should add administrator according role")
    @ParameterizedTest
    @CsvSource({"ADMIN,200", "ADMIN_READ_ONLY,403", "MANAGEMENT,403", "NONE,401"})
    void should_add_administrator_according_role(String role, String expected) {
        // GIVEN
        AdminRole adminRole = this.getRoleFromString(role);
        AdministratorDto admin = mockAdministratorDto();
        // WHEN
        webClient.post()
                .uri(ADMINISTRATOR_API)
                .headers(http -> {
                    if (adminRole != null) {
                        http.setBearerAuth(this.getDefaultToken(adminRole));
                    }
                })
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(admin))
                .exchange()
                // THEN response
                .expectStatus().isEqualTo(Integer.parseInt(expected));
    }

    @DisplayName("should update administrator according role")
    @ParameterizedTest
    @CsvSource({"ADMIN,200", "ADMIN_READ_ONLY,403", "MANAGEMENT,403", "NONE,401"})
    void should_update_administrator_according_role(String role, String expected) {
        // GIVEN
        AdminRole adminRole = this.getRoleFromString(role);
        //administrator
        AdministratorDocument adminDoc = mockAdministratorDocument();
        this.mongoAdministratorClient.save(adminDoc).block();
        //request
        AdministratorDto admin = mockAdministratorDto();
        admin.setId(adminDoc.getId().toString());
        // WHEN
        webClient.patch()
                .uri(ADMINISTRATOR_API)
                .headers(http -> {
                    if (adminRole != null) {
                        http.setBearerAuth(this.getDefaultToken(adminRole));
                    }
                })
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(admin))
                .exchange()
                // THEN response
                .expectStatus().isEqualTo(Integer.parseInt(expected));
    }

    @DisplayName("should delete administrator according role")
    @ParameterizedTest
    @CsvSource({"ADMIN,200", "ADMIN_READ_ONLY,403", "MANAGEMENT,403", "NONE,401"})
    void should_delete_administrator_according_role(String role, String expected) {
        // GIVEN
        //administrator
        AdministratorDocument adminDoc = mockAdministratorDocument();
        this.mongoAdministratorClient.save(adminDoc).block();
        //request
        AdminRole adminRole = this.getRoleFromString(role);
        // WHEN
        webClient.delete()
                .uri(String.format("%s/%s", ADMINISTRATOR_API, adminDoc.getId()))
                .headers(http -> {
                    if (adminRole != null) {
                        http.setBearerAuth(this.getDefaultToken(adminRole));
                    }
                })
                .exchange()
                // THEN response
                .expectStatus().isEqualTo(Integer.parseInt(expected));
    }

    @DisplayName("should get administrator according role")
    @ParameterizedTest
    @CsvSource({"ADMIN,200", "ADMIN_READ_ONLY,403", "MANAGEMENT,403", "NONE,401"})
    void should_get_administrator_according_role(String role, String expected) {
        // GIVEN
        //administrator
        AdministratorDocument adminDoc = mockAdministratorDocument();
        this.mongoAdministratorClient.save(adminDoc).block();
        //request
        AdminRole adminRole = this.getRoleFromString(role);
        // WHEN
        webClient.get()
                .uri(String.format("%s/%s", ADMINISTRATOR_API, ADMINISTRATOR_ID))
                .headers(http -> {
                    if (adminRole != null) {
                        http.setBearerAuth(this.getDefaultToken(adminRole));
                    }
                })
                .exchange()
                // THEN response
                .expectStatus().isEqualTo(Integer.parseInt(expected));
    }

    @DisplayName("should find administrator according role")
    @ParameterizedTest
    @CsvSource({"ADMIN,200", "ADMIN_READ_ONLY,403", "MANAGEMENT,403", "NONE,401"})
    void should_find_administrator_according_role(String role, String expected) {
        // GIVEN
        AdminRole adminRole = this.getRoleFromString(role);
        // WHEN
        webClient.get()
                .uri(String.format("%s", ADMINISTRATOR_API))
                .headers(http -> {
                    if (adminRole != null) {
                        http.setBearerAuth(this.getDefaultToken(adminRole));
                    }
                })
                .exchange()
                // THEN response
                .expectStatus().isEqualTo(Integer.parseInt(expected));
    }

    private AdministratorDto mockAdministratorDto() {
        AdministratorDto admin = new AdministratorDto();
        admin.setId(ADMINISTRATOR_ID.toString());
        admin.setLogin(ADMINISTRATOR_LOGIN);
        admin.setEmail(EMAIL_FORGOTTEN);
        admin.setPassword("test");
        return admin;
    }

    private UpdateAdministratorDto mockUpdateAdministratorDto() {
        UpdateAdministratorDto admin = new UpdateAdministratorDto();
        admin.setId(ADMINISTRATOR_ID.toString());
        admin.setLogin(ADMINISTRATOR_LOGIN);
        admin.setEmail(EMAIL_FORGOTTEN);
        admin.setPassword("test");
        return admin;
    }

    private AdministratorDocument mockAdministratorDocument() {
        AdministratorDocument admin = new AdministratorDocument();
        admin.setId(ADMINISTRATOR_ID);
        admin.setLogin(ADMINISTRATOR_LOGIN);
        admin.setEmail(EMAIL_FORGOTTEN);
        admin.setPassword("test");
        return admin;
    }

    private SendForgotPasswordDto mockSendForgotPasswordDto() {
        SendForgotPasswordDto sendForgot = new SendForgotPasswordDto();

        sendForgot.setEmail(EMAIL_FORGOTTEN);

        return sendForgot;
    }

    private RestorePasswordDto mockRestorePasswordDto() {
        RestorePasswordDto restorePassword = new RestorePasswordDto();

        restorePassword.setLogin(RESTORE_PASSWORD_LOGIN);
        restorePassword.setNewPassword(RESTORE_PASSWORD_NEW_PASSWORD);
        restorePassword.setPasswordToken(RESTORE_PASSWORD_PASSWORD_TOKEN);

        return restorePassword;
    }
}
