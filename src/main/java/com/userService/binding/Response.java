package com.userService.binding;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Response {

    private String token;
    private String username;

    // Private constructor to prevent direct instantiation
    private Response(Builder builder) {
        this.token = builder.token;
        this.username = builder.username;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    // Setters (optional, if needed for other use cases)
    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Static Builder class
    public static class Builder {
        private String token;
        private String username;

        // Setter  methods for the builder
        public Builder jwtToken(String token) {
            this.token = token;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        // Build method that returns a JwtResponse instance
        public Response build() {
            return new Response(this);
        }
    }

    // Static builder() method for ease of use
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "Response { " +
                "token='" + token + '\'' +
                ", username='" + username + '\'' +
                " }";
    }
}
