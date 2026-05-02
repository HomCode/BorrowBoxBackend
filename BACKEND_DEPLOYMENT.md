# Spring Boot Backend Deployment Guide

**Deploy BorrowBox Backend to Production**

---

## 📋 Quick Start

### Build Locally First
```bash
cd C:\Users\homer\Desktop\BorrowBoxBackend\BorrowBoxBackend
mvn clean install
mvn spring-boot:run
```

Backend runs at: `http://localhost:8080/api`

---

## 🚀 Production Deployment Options

### Option 1: Render.com (Recommended for Free Tier)

#### Step 1: Prepare for Deployment
```bash
# Ensure pom.xml has these plugins:
# - spring-boot-maven-plugin (for creating JAR)
# - maven-compiler-plugin (Java 17+)

# Test locally first
mvn clean package
java -jar target/BorrowBoxBackend-0.0.1-SNAPSHOT.jar
```

#### Step 2: Push to GitHub
```bash
cd BorrowBoxBackend
git init
git add .
git commit -m "Initial: BorrowBox Backend Spring Boot"
git remote add origin https://github.com/YOUR_USERNAME/BorrowBoxBackend.git
git branch -M main
git push -u origin main
```

#### Step 3: Create Render Account & Deploy
1. Go to https://render.com
2. Click "New +" → "Web Service"
3. Connect your GitHub repository
4. Configure:

```
Name: borrowbox-backend
Environment: Java
Build Command: mvn clean install
Start Command: java -jar target/BorrowBoxBackend-0.0.1-SNAPSHOT.jar
Instance Type: Free (or Starter)
```

#### Step 4: Set Environment Variables
In Render dashboard, add these variables:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://your-supabase-host:6543/postgres?sslmode=require&prepareThreshold=0
SPRING_DATASOURCE_USERNAME=postgres.your_username
SPRING_DATASOURCE_PASSWORD=your_password_here
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver

SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect

SUPABASE_URL=https://your-project.supabase.co
SUPABASE_ANON_KEY=your_anon_key_here
SUPABASE_JWT_SECRET=your_jwt_secret_here
SUPABASE_SERVICE_ROLE_KEY=your_service_role_key_here

JWT_SECRET=your_very_long_random_secret_key_at_least_32_chars_minimum
JWT_EXPIRATION=86400000

SERVER_PORT=8080
LOGGING_LEVEL_COM_EXAMPLE_BORROWBOXBACKEND=DEBUG
LOGGING_LEVEL_ORG_HIBERNATE_SQL=DEBUG
```

**⚠️ NOTE: Get actual values from:**
- **Supabase**: Project Settings → API Keys
- **JWT Secret**: Generate random 32+ character string
- Set these in Render dashboard, NOT in this file

#### Step 5: Deploy
- Click "Deploy" button
- Wait 5-10 minutes for build and start
- Get URL: `https://borrowbox-backend-xxx.onrender.com`

---

### Option 2: Railway.app

#### Step 1: Create Railway Account
- Go to https://railway.app
- Sign up with GitHub

#### Step 2: Create New Project
1. Dashboard → New Project
2. Select "Deploy from GitHub Repo"
3. Select your BorrowBoxBackend repository

#### Step 3: Add PostgreSQL Database
1. Project → Add Service → PostgreSQL
2. Railway auto-provisions database
3. Variables auto-populated

#### Step 4: Set Java Runtime Variables
```
JAVA_VERSION=17
```

#### Step 5: Deploy
- Push to main branch
- Railway auto-builds and deploys
- Get URL from Railway dashboard

---

### Option 3: Heroku Alternative (Requires Credit Card)

#### Step 1: Install Heroku CLI
```bash
choco install heroku-cli  # Windows
```

#### Step 2: Login
```bash
heroku login
```

#### Step 3: Create App
```bash
heroku create borrowbox-backend
```

#### Step 4: Add PostgreSQL Add-on
```bash
heroku addons:create heroku-postgresql:hobby-dev
```

#### Step 5: Set Config Variables
```bash
heroku config:set SPRING_DATASOURCE_URL=jdbc:postgresql://your-supabase-host:6543/postgres?sslmode=require
heroku config:set SPRING_DATASOURCE_USERNAME=postgres.your_username
heroku config:set SPRING_DATASOURCE_PASSWORD=your_password_here
heroku config:set SUPABASE_URL=https://your-project.supabase.co
heroku config:set SUPABASE_ANON_KEY=your_anon_key
heroku config:set SUPABASE_JWT_SECRET=your_jwt_secret
heroku config:set JWT_SECRET=your_long_random_secret_32_chars
```

#### Step 6: Deploy
```bash
git push heroku main
```

---

## 🔧 Configuration Management

### application.properties Structure
```properties
# Datasource (Database Connection)
spring.datasource.url=...
spring.datasource.username=...
spring.datasource.password=...
spring.datasource.driver-class-name=org.postgresql.Driver

# Connection Pooling
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.maximum-pool-size=5
spring.datasource.hikari.minimum-idle=2

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=false  # Set to false in production for performance

# JWT
jwt.secret=your_secret_key
jwt.expiration=86400000

# Server
server.port=8080
server.servlet.context-path=/api

# Logging
logging.level.root=INFO
logging.level.com.example.BorrowBoxBackend=DEBUG
```

### Environment-Specific Configs
Create separate files:
- `application-development.properties` (local development)
- `application-production.properties` (production)

Then activate with:
```bash
java -jar app.jar --spring.profiles.active=production
```

---

## 📦 Packaging with Maven

### Build JAR
```bash
mvn clean package
```

Creates: `target/BorrowBoxBackend-0.0.1-SNAPSHOT.jar`

### Build with Specific Profile
```bash
mvn clean package -P production
```

### Skip Tests (if needed)
```bash
mvn clean package -DskipTests
```

---

## ✅ Deployment Checklist

- [ ] Local build successful (`mvn clean package`)
- [ ] All tests pass (`mvn test`)
- [ ] Code pushed to GitHub
- [ ] Environment variables configured on platform
- [ ] Database connection verified
- [ ] Application starts on platform
- [ ] API endpoints responding (curl test)
- [ ] Frontend can reach backend
- [ ] JWT tokens working
- [ ] Database migrations complete
- [ ] Logs accessible and clean

---

## 🧪 Testing After Deployment

### 1. Health Check
```bash
curl https://your-backend-url.onrender.com/api/health
```

### 2. Login Test
```bash
curl -X POST https://your-backend-url.onrender.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password"}'
```

### 3. Get Items
```bash
curl https://your-backend-url.onrender.com/api/items
```

### 4. Check Logs (Render)
Dashboard → Service → Logs tab

---

## 🐛 Troubleshooting

| Problem | Solution |
|---------|----------|
| Build fails | Check Java version (need 17+), run locally first |
| Database connection refused | Verify URL, port, credentials, SSL mode |
| CORS error | Backend CORS configured for localhost:3000 only |
| JWT errors | Check JWT_SECRET matches, token not expired |
| Out of memory | Increase memory: `java -Xmx512m -jar app.jar` |
| Port already in use | Change SERVER_PORT or kill process |

---

## 📊 Performance Optimization

### Java Settings
```bash
java -Xms256m -Xmx512m -jar app.jar
```

### Connection Pool Optimization
```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=1200000
```

### Database Indexing
Add indexes in Supabase for:
- `users.username` (unique lookup)
- `items.category` (filtering)
- `borrows.student_id` (student queries)
- `transactions.created_at` (date range queries)

---

## 🔐 Production Security

### Enable HTTPS
- Render: Automatic with Render domain
- Railway: Automatic with Railway domain
- Both: Can add custom domain with SSL

### Secrets Management
- Never commit `application.properties` with secrets
- Use environment variables only
- Rotate JWT_SECRET regularly
- Use strong database passwords

### Database Security
- Supabase: RLS (Row Level Security) enabled
- SSL connection: `?sslmode=require`
- Connection pooling limits connections

---

## 📈 Monitoring

### Logs (Render Dashboard)
```
View → Logs tab
```

### Error Tracking (Optional)
Add Sentry for error monitoring:
```xml
<dependency>
  <groupId>io.sentry</groupId>
  <artifactId>sentry-spring-boot-starter</artifactId>
  <version>6.28.0</version>
</dependency>
```

### Metrics
```properties
management.endpoints.web.exposure.include=health,metrics
management.endpoint.health.show-details=when-authorized
```

---

## 🔄 CI/CD Integration

### Automatic Deployment (GitHub → Render)
1. Connect GitHub repo to Render
2. Push to main branch
3. Render automatically:
   - Pulls code
   - Runs build
   - Deploys service
   - Starts application

No manual intervention needed!

---

## 📝 Production Deployment Walkthrough

```bash
# 1. Prepare code
cd BorrowBoxBackend
git status  # Ensure all changes committed

# 2. Build locally
mvn clean package

# 3. Push to GitHub
git push origin main

# 4. On Render Dashboard:
#    - Click "Deploy"
#    - Wait for build to complete
#    - Check logs for errors

# 5. Test deployment
curl https://borrowbox-backend-xxx.onrender.com/api/items

# 6. Update frontend with new URL
# Set REACT_APP_API_URL in Vercel env vars

# 7. Redeploy frontend
# Push to GitHub, Vercel auto-deploys
```

---

## 🎯 Production URLs

After deployment:
- **Backend API**: `https://borrowbox-backend-xxx.onrender.com/api`
- **Frontend**: `https://borrowbox-frontend-xxx.vercel.app`

Update frontend environment variables with backend URL!

---

**Ready for Production Deployment!** 🚀
