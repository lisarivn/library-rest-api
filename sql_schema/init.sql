CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS libraries (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    total_departments INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS departments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    library_id UUID REFERENCES libraries(id) ON DELETE CASCADE,
    department_name VARCHAR(255) NOT NULL,
    dean VARCHAR(255),
    spec BOOLEAN
);

CREATE TABLE IF NOT EXISTS subjects (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    department_id UUID REFERENCES departments(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    syllabus TEXT,
    credits INTEGER
);

CREATE TABLE IF NOT EXISTS literature (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    subject_id UUID REFERENCES subjects(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    publication_year INTEGER
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(250) NOT NULL, 
    role VARCHAR(50) NOT NULL
);

INSERT INTO libraries (id, name, total_departments) VALUES 
('11111111-1111-1111-1111-111111111111', 'Central University Library', 3);

INSERT INTO departments (id, library_id, department_name, dean, spec) VALUES 
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111', 'Computer Science', 'Dr. Smith', true),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '11111111-1111-1111-1111-111111111111', 'Physics', 'Dr. Johnson', true),
('cccccccc-cccc-cccc-cccc-cccccccccccc', '11111111-1111-1111-1111-111111111111', 'Cybersecurity', 'Mrs. Williams', true);

INSERT INTO subjects (id, department_id, name, syllabus, credits) VALUES 
('e1e1e1e1-e1e1-e1e1-e1e1-e1e1e1e1e1e1', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Java Enterprise', 'Spring Boot, Hibernate, Security', 6),
('e2e2e2e2-e2e2-e2e2-e2e2-e2e2e2e2e2e2', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Database Systems', 'SQL, PostgreSQL, Normalization', 5),
('f1f1f1f1-f1f1-f1f1-f1f1-f1f1f1f1f1f1', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Quantum Mechanics', 'Wave functions, Entanglement', 7),
('77777777-7777-7777-7777-777777777777', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 'Cryptography', 'AES, RSA, Digital Signatures', 4);

INSERT INTO literature (id, subject_id, name, author, publication_year) VALUES 
('1111cccc-cccc-cccc-cccc-cccccccccccc', 'e1e1e1e1-e1e1-e1e1-e1e1-e1e1e1e1e1e1', 'Spring in Action', 'Craig Walls', 2022),
('2222dddd-dddd-dddd-dddd-dddddddddddd', 'e2e2e2e2-e2e2-e2e2-e2e2-e2e2e2e2e2e2', 'Database Internals', 'Alex Petrov', 2019),
('3333eeee-eeee-eeee-eeee-eeeeeeeeeeee', 'f1f1f1f1-f1f1-f1f1-f1f1-f1f1f1f1f1f1', 'Quantum Theory', 'David Bohm', 1951),
('4444ffff-ffff-ffff-ffff-ffffffffffff', '77777777-7777-7777-7777-777777777777', 'Applied Cryptography', 'Bruce Schneier', 1996);

INSERT INTO users (username, password, role) 
VALUES 
	('lisa', '$2a$10$03EqUYjQX1dQ6jjxun5JkueqceDQOcTmzVVDd6mRzKn5WPW7nwaRa', 'ROLE_USER'),
	('admin', '$2a$10$ZRbKaMJX/SgC96Sow2.VJe048lwE6kHKj02U.ePH6lHXAtK0xP97a', 'ROLE_ADMIN')
ON CONFLICT (username) DO NOTHING;