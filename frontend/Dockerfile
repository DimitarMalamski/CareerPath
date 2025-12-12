# -------- Build Stage --------
FROM node:20-alpine AS builder
WORKDIR /app

COPY package*.json ./
RUN npm ci --legacy-peer-deps

COPY . .
RUN npm run build

# -------- Run Stage --------
FROM nginx:1.27-alpine

RUN rm -rf /usr/share/nginx/html/*

# Correct Angular output path:
COPY --from=builder /app/dist/careerpath-frontend/browser /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
