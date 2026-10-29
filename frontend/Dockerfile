# ---- Stage 1: build the React (Vite) app ----
FROM node:22-alpine AS build
WORKDIR /app

# Copy manifests first so `npm ci` is cached unless dependencies change
COPY package.json package-lock.json ./
RUN npm ci

COPY . .
RUN npm run build        # outputs to /app/dist

# ---- Stage 2: serve the static files with Nginx ----
FROM nginx:stable-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80