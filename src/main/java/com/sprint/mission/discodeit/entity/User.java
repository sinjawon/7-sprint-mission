package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sprint.mission.discodeit.entity.base.BaseUpdateEntity;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "users")
public class User extends BaseUpdateEntity {

    @Column(length = 50, nullable = false, unique = true)
    private String username;
    @Column(length = 100, nullable = false, unique = true)
    private String email;
    @Column(length = 60, nullable = false)
    private String password;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id", columnDefinition = "uuid")
    private BinaryContent profile;

    @JsonManagedReference
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.PROTECTED)
    private UserStatus status;

    public User(String username, String email, String password, BinaryContent profile) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profile = profile;
    }


    public void update(String newUsername, String newEmail, String newPassword, BinaryContent newProfile) {

        if (newUsername != null && !newUsername.equals(this.username)) {
            this.username = newUsername;

        }
        if (newEmail != null && !newEmail.equals(this.email)) {
            this.email = newEmail;

        }
        if (newPassword != null && !newPassword.equals(this.password)) {
            this.password = newPassword;

        }
        if (newProfile != null) {
            this.profile = newProfile;

        }


    }
}
