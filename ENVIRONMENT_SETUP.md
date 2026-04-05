# Environment Setup Guide

## 🚀 Quick Start

1. **Copy environment template:**
   ```bash
   cp .env.example .env
   ```

2. **Edit `.env` file with your actual values:**
   ```bash
   # Open .env and replace placeholder values
   GOOGLE_CLIENT_ID=your-actual-google-client-id
   GOOGLE_CLIENT_SECRET=your-actual-google-client-secret
   JWT_SECRET=your-secure-random-string-at-least-32-chars
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

## 🔐 Google OAuth2 Setup

### 1. Create Google Cloud Project
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable Google+ API and Google OAuth2 API

### 2. Create OAuth2 Credentials
1. Go to "APIs & Services" → "Credentials"
2. Click "Create Credentials" → "OAuth 2.0 Client IDs"
3. Choose "Web application"
4. Add authorized redirect URIs:
   - For development: `http://localhost:8080/login/oauth2/code/google`
   - For production: `https://yourdomain.com/login/oauth2/code/google`

### 3. Get Your Credentials
- **Client ID**: Copy the Client ID
- **Client Secret**: Copy the Client Secret

### 4. Configure Environment Variables
```bash
GOOGLE_CLIENT_ID=your-client-id-here
GOOGLE_CLIENT_SECRET=your-client-secret-here
```

## 🔑 JWT Secret Generation

### Option 1: Generate Secure Random String (Recommended)
```bash
# Linux/Mac
openssl rand -base64 32

# Windows PowerShell
[System.Web.Security.Membership]::GeneratePassword(32, 0)
```

### Option 2: Use Online Generator
- Visit: https://www.uuidgenerator.net/
- Generate a UUID and use it as your JWT secret

### Option 3: Use Default (Development Only)
The application.properties has a default JWT secret for development:
```
JWT_SECRET=MeG8P9cjmDkmeNTcJGgzAtiv/uZoTaehJ7dguz0PiNg=
```
⚠️ **Never use this in production!**

## 🌍 Environment Variables Reference

| Variable | Description | Required | Example |
|----------|-------------|----------|---------|
| `GOOGLE_CLIENT_ID` | Google OAuth2 Client ID | Yes | `123456789-abc...` |
| `GOOGLE_CLIENT_SECRET` | Google OAuth2 Client Secret | Yes | `GOCSPX-...` |
| `JWT_SECRET` | JWT signing secret (32+ chars) | Yes | `your-secure-random-string` |

## 🚀 Running in Different Environments

### Development (Local)
```bash
# .env file in project root
cp .env.example .env
# Edit .env with your values
mvn spring-boot:run
```

### Production (Docker)
```bash
docker run -e GOOGLE_CLIENT_ID=... -e GOOGLE_CLIENT_SECRET=... -e JWT_SECRET=... your-app
```

### Production (Cloud)
Set environment variables in your cloud platform:
- **Heroku**: `heroku config:set GOOGLE_CLIENT_ID=...`
- **AWS**: Environment variables in ECS/Lambda
- **Azure**: Application settings in App Service
- **GCP**: Environment variables in Cloud Run

## 🔒 Security Best Practices

### ✅ Do This:
- Use environment variables for secrets
- Generate strong random JWT secrets
- Keep .env files out of version control
- Use HTTPS in production
- Rotate secrets regularly

### ❌ Don't Do This:
- Hardcode secrets in application.properties
- Commit .env files to git
- Use weak JWT secrets
- Share secrets in code reviews
- Use the default JWT secret in production

## 🐛 Troubleshooting

### "Invalid client" error
- Check GOOGLE_CLIENT_ID is correct
- Verify redirect URI is configured in Google Console

### "Invalid JWT" error
- Ensure JWT_SECRET is set and matches between deployments
- Check JWT_SECRET is at least 32 characters

### "Environment variable not found" error
- Make sure .env file exists in project root
- Check variable names match exactly
- Restart the application after changing .env

### OAuth redirect not working
- Verify redirect URI in Google Console matches your domain
- Check if HTTPS is required for production

## 📚 Additional Resources

- [Google OAuth2 Setup Guide](https://developers.google.com/identity/protocols/oauth2)
- [Spring Boot External Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [JWT Best Practices](https://tools.ietf.org/html/rfc8725)

## ❓ Need Help?

1. Check this guide first
2. Verify your .env file has correct values
3. Test with the default JWT secret (development only)
4. Check application logs for specific error messages
5. Verify Google Cloud Console configuration
