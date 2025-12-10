-- SKILLS ------------------------------------------------------

INSERT INTO skills (name) VALUES
                              ('Java'),
                              ('Spring Boot'),
                              ('PostgreSQL'),
                              ('React'),
                              ('TypeScript'),
                              ('GraphQL'),
                              ('AWS'),
                              ('Docker'),
                              ('Terraform'),
                              ('CI/CD')
    ON CONFLICT DO NOTHING;


-- JOB LISTINGS ------------------------------------------------

INSERT INTO job_listings (
    id, recruiter_id, title, company, location, type, status,
    stack_summary, description, created_at
)
VALUES (
           '00000000-0000-0000-0000-000000000001',
           'rec-corewave-001',
           'Senior Java Backend Developer',
           'CoreWave Technologies',
           'Eindhoven, Netherlands',
           'FULL_TIME',
           'PUBLISHED',
           'Java, Spring Boot, PostgreSQL, Distributed Systems',
           $$
               CoreWave Technologies is a high-performance engineering company specializing in real-time data processing and large-scale distributed platforms. We are expanding our backend engineering division with a Senior Java Developer who thrives in complex system design and enjoys working on products that service more than 50 million requests per day.

As a Senior Backend Engineer, you will be a key contributor in reshaping our API platform and enabling new capabilities across analytics, event streaming, and real-time orchestration. You will collaborate closely with system architects, DevOps engineers, and product teams to deliver highly scalable backend services.

           What You'll Work On:
You will drive the development of microservices written in Java and Spring Boot, optimize existing database layers, and contribute to platform-wide architectural decisions. Expect to work with distributed tracing, high-volume message queues, and advanced caching strategies.

Core Responsibilities:
- Architect, design, and implement backend services using Java 17 and Spring Boot 3.
- Build scalable APIs consumed by millions of global clients.
- Optimize SQL queries and schema designs for PostgreSQL.
- Implement event-driven pipelines using Kafka and AWS SNS/SQS.
- Improve application resilience, observability, and performance.
- Conduct code reviews, mentor junior engineers, and contribute to engineering standards.

Required Skills:
- 4+ years professional experience with Java.
- Deep knowledge of Spring Boot, Spring Security, JPA/Hibernate.
- Strong understanding of relational databases (PostgreSQL preferred).
- Familiarity with distributed systems, caching, and scalability concepts.
- Experience with CI/CD pipelines and Docker-based deployments.

Bonus Points:
- Experience with AWS ECS, Lambda, or DynamoDB.
- Exposure to Terraform or Infrastructure as Code.
- Passion for performance tuning and JVM internals.

Why CoreWave?
We take pride in offering an engineering culture focused on autonomy, craftsmanship, and solving meaningful technical challenges. You’ll join a team where your ideas influence the roadmap and your work powers products used worldwide.
$$,
    NOW()
)
ON CONFLICT DO NOTHING;


-- SECOND JOB --------------------------------------------------

INSERT INTO job_listings (
    id, recruiter_id, title, company, location, type, status,
    stack_summary, description, created_at
)
VALUES (
    '00000000-0000-0000-0000-000000000002',
    'rec-synapse-001',
    'Mid-Level React Frontend Engineer',
    'Synapse Interactive',
    'Remote (EU)',
    'REMOTE',
    'PUBLISHED',
    'React, TypeScript, GraphQL, Design Systems',
    $$
Synapse Interactive is a UI-first product studio building beautiful, intuitive interfaces for next-generation enterprise tools. We partner with industry leaders in fintech, healthcare, and automation to create user experiences that feel effortless yet powerful.

We are hiring a React Engineer who is passionate about design systems, micro-frontend architecture, and building smooth, scalable applications.

### What You'll Work On
You will help rebuild our next-generation design system, contribute to complex UI modules, and integrate GraphQL-based APIs. Your work directly impacts the usability and performance of the platforms our customers rely on daily.

           ### Core Responsibilities
           - Develop responsive UI components using React 18 and TypeScript.
           - Collaborate with UX designers to bring high-fidelity mockups to life.
           - Consume GraphQL APIs and implement caching with Apollo Client.
           - Participate in frontend architecture decisions and performance optimizations.
           - Write clean, testable code with Jest and React Testing Library.
- Contribute to accessibility improvements (WCAG 2.1).

### Required Skills
- Strong foundation in React and TypeScript.
- Experience integrating REST or GraphQL APIs.
- Understanding of component-driven development & storybook workflows.
- Ability to structure frontend apps with maintainability in mind.

### Bonus Points
- Familiarity with micro-frontends or module federation.
- Animation experience with Framer Motion.
- Background working with Node.js build pipelines.

### Why Synapse?
We offer a highly collaborative environment where engineers and designers work as equals. You’ll be part of a culture that values creativity, responsibility, and delivering exceptional user experiences.
           $$,
           NOW()
       )
    ON CONFLICT DO NOTHING;


-- THIRD JOB ---------------------------------------------------

INSERT INTO job_listings (
    id, recruiter_id, title, company, location, type, status,
    stack_summary, description, created_at
)
VALUES (
           '00000000-0000-0000-0000-000000000003',
           'rec-cloudforge-001',
           'DevOps & Cloud Infrastructure Engineer',
           'CloudForge Engineering',
           'Berlin, Germany',
           'HYBRID',
           'PUBLISHED',
           'AWS, Docker, Terraform, CI/CD, Observability',
           $$
               CloudForge is a cloud infrastructure consultancy specializing in automating deployments, securing large-scale systems, and building resilient cloud platforms for global customers. We partner with enterprise clients to modernize their infrastructure and embrace DevOps culture.

           We are now expanding our Cloud Engineering team with someone who loves automation, cloud architecture, and solving complex infrastructure challenges.

           ### Role Overview
           You will design, implement, and maintain AWS cloud infrastructure using Terraform, build CI/CD pipelines, and help teams adopt best practices around automation, monitoring, and reliability. You’ll also collaborate with backend teams to optimize Docker-based microservices.

           ### Core Responsibilities
           - Create reusable Terraform modules and manage infrastructure as code.
           - Build and maintain GitHub Actions / GitLab CI pipelines.
           - Architect secure and scalable AWS environments (ECS, EKS, RDS, IAM).
           - Improve observability using CloudWatch, Prometheus, and Grafana.
           - Conduct incident analysis and develop reliability improvements.
           - Harden security posture with role-based access, encryption, and auditing.

           ### Required Skills
           - Strong background with AWS services (ECS/EKS, VPC, IAM, RDS).
           - Hands-on experience with Terraform (or similar IaC tools).
           - Deep understanding of Docker and container orchestration.
           - CI/CD experience with GitHub Actions, GitLab CI, or Jenkins.

           ### Bonus Points
           - Experience with Kubernetes.
           - Background in SRE or Infrastructure Engineering.
           - Ability to write small automation scripts (Python/Bash).

           ### Why CloudForge?
           We are a team of cloud purists — everything we build is automated, reproducible, and scalable. You will join a culture where engineering quality matters and where you can directly influence architecture decisions across multiple client platforms.
           $$,
           NOW()
       )
    ON CONFLICT DO NOTHING;


-- JOB SKILLS --------------------------------------------------

INSERT INTO job_skills (job_id, skill_id)
VALUES
    -- CoreWave
    ('00000000-0000-0000-0000-000000000001', (SELECT id FROM skills WHERE name = 'Java')),
    ('00000000-0000-0000-0000-000000000001', (SELECT id FROM skills WHERE name = 'Spring Boot')),
    ('00000000-0000-0000-0000-000000000001', (SELECT id FROM skills WHERE name = 'PostgreSQL')),
    ('00000000-0000-0000-0000-000000000001', (SELECT id FROM skills WHERE name = 'Docker')),

    -- Synapse
    ('00000000-0000-0000-0000-000000000002', (SELECT id FROM skills WHERE name = 'React')),
    ('00000000-0000-0000-0000-000000000002', (SELECT id FROM skills WHERE name = 'TypeScript')),
    ('00000000-0000-0000-0000-000000000002', (SELECT id FROM skills WHERE name = 'GraphQL')),

    -- CloudForge
    ('00000000-0000-0000-0000-000000000003', (SELECT id FROM skills WHERE name = 'AWS')),
    ('00000000-0000-0000-0000-000000000003', (SELECT id FROM skills WHERE name = 'Docker')),
    ('00000000-0000-0000-0000-000000000003', (SELECT id FROM skills WHERE name = 'CI/CD')),
    ('00000000-0000-0000-0000-000000000003', (SELECT id FROM skills WHERE name = 'Terraform'))
    ON CONFLICT DO NOTHING;
