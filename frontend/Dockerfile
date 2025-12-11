# --- Build Stage ---
FROM node:20-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install --legacy-peer-deps
COPY . .
RUN npm run build

# --- Run Stage ---
FROM nginx:alpine
COPY --from=builder /app/dist/careerpath-frontend /usr/share/nginx/html
EXPOSE 80
