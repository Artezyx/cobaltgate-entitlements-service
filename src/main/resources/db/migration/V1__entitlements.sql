CREATE TABLE subscriptions (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  company VARCHAR(140) NOT NULL,
  seat_limit INT NOT NULL,
  seats_used INT NOT NULL DEFAULT 0,
  version BIGINT NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT ck_seats_positive CHECK (seat_limit > 0),
  CONSTRAINT ck_seats_range CHECK (seats_used >= 0 AND seats_used <= seat_limit)
);
CREATE TABLE memberships (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  subscription_id BIGINT NOT NULL,
  email VARCHAR(240) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_membership_subscription FOREIGN KEY (subscription_id) REFERENCES subscriptions(id),
  CONSTRAINT uq_membership_email UNIQUE (subscription_id,email)
);
CREATE TABLE audit_log (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  subscription_id BIGINT NOT NULL,
  action VARCHAR(100) NOT NULL,
  detail VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_audit_subscription FOREIGN KEY (subscription_id) REFERENCES subscriptions(id)
);
CREATE INDEX ix_audit_subscription_time ON audit_log(subscription_id,created_at);
