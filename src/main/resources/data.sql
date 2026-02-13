-- -----------------------------
-- COURSES
-- -----------------------------
INSERT INTO courses (title, description, created_at, updated_at, published) VALUES
  ('Java Basics', 'Intro to Java syntax, variables, loops', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
  ('Spring Boot Fundamentals', 'Building REST APIs with Spring Boot', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE);

-- -----------------------------
-- SECTIONS
-- -----------------------------
INSERT INTO sections (title, content, course_id, order_index, created_at, updated_at) VALUES
(
  'Variables and Types',
  '# Variables and Types

In this section you learn about:
- primitive types
- reference types
- type casting
',
  (SELECT id FROM courses WHERE title = 'Java Basics'),
  1,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  'If / Else and Loops',
  '# If / Else and Loops

Topics:
- if / else
- for
- while
- do-while
',
  (SELECT id FROM courses WHERE title = 'Java Basics'),
  2,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  'Controllers in Spring Boot',
  '# Controllers in Spring Boot

- @RestController
- @GetMapping
- @PostMapping
',
  (SELECT id FROM courses WHERE title = 'Spring Boot Fundamentals'),
  1,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  'JPA & Spring Data',
  '# JPA & Spring Data

- @Entity
- @ManyToOne / @OneToMany
- JpaRepository
',
  (SELECT id FROM courses WHERE title = 'Spring Boot Fundamentals'),
  2,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);
