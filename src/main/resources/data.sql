-- Создаём пару курсов
INSERT INTO courses (title, description) VALUES
  ('Java Basics', 'Intro to Java syntax, variables, loops'),
  ('Spring Boot Fundamentals', 'Building REST APIs with Spring Boot');

-- Для этих курсов добавим секции.
-- Предполагаем, что ID у курсов стали 1 и 2 (пустая база → первые айди).
INSERT INTO sections (content, course_id) VALUES
('# Variables and Types

In this section you learn about:
- primitive types
- reference types
- type casting
', 1),
('# If / Else and Loops

Topics:
- if / else
- for
- while
- do-while
', 1),
('# Controllers in Spring Boot

- @RestController
- @GetMapping
- @PostMapping
', 2),
('# JPA & Spring Data

- @Entity
- @ManyToOne / @OneToMany
- JpaRepository
', 2);
