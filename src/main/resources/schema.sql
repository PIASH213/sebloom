# DROP TABLE IF EXISTS progress;
# DROP TABLE IF EXISTS question;
#
# CREATE TABLE question (
#                           id BIGINT NOT NULL AUTO_INCREMENT,
#                           topic VARCHAR(255),
#                           bloom_level VARCHAR(255),
#                           question_text VARCHAR(500),
#                           option_a VARCHAR(255),
#                           option_b VARCHAR(255),
#                           option_c VARCHAR(255),
#                           option_d VARCHAR(255),
#                           correct_answer VARCHAR(255),
#                           PRIMARY KEY (id)
# );
#
# CREATE TABLE progress (
#                           id BIGINT NOT NULL AUTO_INCREMENT,
#                           topic VARCHAR(255),
#                           bloom_level VARCHAR(255),
#                           total_questions INT,
#                           correct_answers INT,
#                           attempt_date DATETIME,
#                           PRIMARY KEY (id)
# );