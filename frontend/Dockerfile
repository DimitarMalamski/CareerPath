# -------- Build Stage --------
FROM node:20-alpine AS builder
WORKDIR /app

COPY package.json ./
RUN npm ci --legacy-peer-deps

COPY . .
RUN npm run build

# -------- Run Stage --------
FROM nginx:1.27-alpine

# Optional: remove default nginx assets
RUN rm -rf /usr/share/nginx/html/

# Copy built static files (Vite/React default output is /app/dist)
COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
