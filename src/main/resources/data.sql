-- ============================================
-- data.sql (PostgreSQL) — idempotent seed
-- ============================================

-- -----------------------------
-- COURSES
-- Requires unique constraint: courses(title)
-- -----------------------------
INSERT INTO courses (title, description, created_at, updated_at, published)
VALUES
  ('Java Basics', 'Intro to Java syntax, variables, loops', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
  ('Spring Boot Fundamentals', 'Building REST APIs with Spring Boot', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE)
ON CONFLICT (title) DO UPDATE
SET
  description = EXCLUDED.description,
  updated_at = CURRENT_TIMESTAMP,
  published = EXCLUDED.published;

-- -----------------------------
-- SECTIONS
-- Requires unique constraint: sections(course_id, order_index)
-- -----------------------------
WITH c AS (
  SELECT id, title
  FROM courses
  WHERE title IN ('Java Basics', 'Spring Boot Fundamentals')
),
java AS (
  SELECT id AS course_id FROM c WHERE title = 'Java Basics'
),
spring AS (
  SELECT id AS course_id FROM c WHERE title = 'Spring Boot Fundamentals'
)
INSERT INTO sections (title, content, course_id, order_index, created_at, updated_at)
SELECT
  s.title,
  s.content,
  s.course_id,
  s.order_index,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
FROM (
  -- Java Basics
  SELECT
    'Variables and Types'::text AS title,
    '# Variables and Types

In this section you learn about:
- primitive types
- reference types
- type casting
'::text AS content,
    (SELECT course_id FROM java) AS course_id,
    0 AS order_index

  UNION ALL

  SELECT
    'If / Else and Loops',
    '# If / Else and Loops

Topics:
- if / else
- for
- while
- do-while
',
    (SELECT course_id FROM java),
    1

  UNION ALL

  -- Spring Boot Fundamentals
  SELECT
    'Controllers in Spring Boot',
    '# Controllers in Spring Boot

- @RestController
- @GetMapping
- @PostMapping
',
    (SELECT course_id FROM spring),
    0

  UNION ALL

  SELECT
    'JPA & Spring Data',
    '# JPA & Spring Data

- @Entity
- @ManyToOne / @OneToMany
- JpaRepository
',
    (SELECT course_id FROM spring),
    1
) s
ON CONFLICT (course_id, order_index) DO UPDATE
SET
  title = EXCLUDED.title,
  content = EXCLUDED.content,
  updated_at = CURRENT_TIMESTAMP;
