package ru.lightsoff.authentication.DTO;

import ru.lightsoff.authentication.Entities.User;

public class RegisterRequest {
        private String username;
        private String password;
        private String email;

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }


        public String getUsername() {
            return username;
        }

        public RegisterRequest withUsername(String username) {
            this.username = username;
            return this;
        }

        public RegisterRequest withPassword(String password) {
            this.password = password;
            return this;
        }

        public RegisterRequest withEmail(String email) {
            this.email = email;
            return this;
        }

    }
