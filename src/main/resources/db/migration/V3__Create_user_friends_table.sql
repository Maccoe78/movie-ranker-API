CREATE TABLE user_follows (
    user_id BIGINT NOT NULL,
    followed_user_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, followed_user_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (followed_user_id) REFERENCES users(id) ON DELETE CASCADE
);