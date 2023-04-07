package com.ricardocreates.infra.data.mongo.document.administrator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document("administrator")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AdministratorDocument {

    @Id
    @Field("_id")
    ObjectId id;

    String login;

    String password;
    private String email;
    /**
     * random token send to user email and use to restore password
     */
    private String forgetPasswordToken;
    private Instant lastLogin;

    private AdminRole role;

    public List<AdminRole> getRoles() {
        return role != null ? List.of(role) : new ArrayList<>();
    }

}


