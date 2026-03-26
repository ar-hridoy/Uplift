DELETE FROM users;

INSERT INTO users (id, full_name, username, email, password_hash, role, active, created_at)
VALUES
    (1, 'Admin User', 'admin', 'admin@uplift.com', '123456', 'ADMIN', true, NOW()),
    (2, 'Rider One', 'rider1', 'rider1@uplift.com', '123456', 'RIDER', true, NOW());
